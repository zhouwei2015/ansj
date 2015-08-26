package com.netease.ipet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.util.DBUtil;
import org.ansj.util.FilterModifWord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class TagServlet extends HttpServlet {
	private static Logger log = LogManager.getLogger(TagServlet.class);
	private static final long serialVersionUID = 1L;
	private static List<String> useless = new ArrayList<String>();

	@Override
	public void init() throws ServletException {
		log.info("-------------init-----初始化过滤词-----");
		Connection conn = DBUtil.getConn();
		String sql = "select * from stopword where status!=0";
		PreparedStatement pstmt = DBUtil.getPstmt(conn, sql);
		ResultSet rs = DBUtil.getRs(pstmt);
		try {
			while (rs.next()) {
				useless.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 插入停止词
		FilterModifWord.insertStopWords(useless);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	String title = new String(request.getParameter("title").getBytes(
				"ISO-8859-1"), "UTF-8");
	/*String title = request.getParameter("title");*/
		log.info("----------请求的问题：" + title);

		KeyWordComputer kwc = new KeyWordComputer(20);
		// 提取关键词
		List<Keyword> result = kwc.computeArticleTfidf(title, null);
		log.info("----------提取后的关键词：" + result);
		recommend(result, response);
	}

	public void recommend(List<Keyword> result, HttpServletResponse response) {
		String jsonStr = "{\"tag\":[";
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Keyword k : result) {
			Map<String, Integer> m = recommentTag(k.getName());
			for (Map.Entry<String, Integer> ma : m.entrySet()) {
				String str = ma.getKey();
				if (map.containsKey(str)) {
					Integer v = map.get(str);
					v = v + ma.getValue();
					map.put(str, v);
				} else {
					map.put(str, ma.getValue());
				}

			}

		}
		if (map.size() > 0) {
			// 根据value排序
			List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
					map.entrySet());
			// 然后通过比较器来实现排序
			Collections.sort(list,
					new Comparator<Map.Entry<String, Integer>>() {
						// 降序排序
						public int compare(Entry<String, Integer> o1,
								Entry<String, Integer> o2) {
							return o2.getValue().compareTo(o1.getValue());
						}

					});
			for (Map.Entry<String, Integer> mapping : list) {
				jsonStr = jsonStr + "\"" + mapping.getKey() + "\",";
				log.info(mapping.getKey() + ":" + mapping.getValue());
			}
			jsonStr = jsonStr.substring(0, jsonStr.lastIndexOf(','));
			jsonStr += "]}";
			log.info("推荐结果：" + jsonStr);
			ReturnUtil.writeBack(response, jsonStr);

		} else {
			log.info("没有推荐的标签，推荐默认标签");
			jsonStr = "{\"tag\":[\"默认,1\",\"标签,2\",\"推荐,3\",\"结果,4\"]}";
			log.info(jsonStr);
			ReturnUtil.writeBack(response, jsonStr);
		}
	}
/*
 * map  key  (tag,id)  value(num)
 *
 **/
	public Map<String, Integer> recommentTag(String key) {
		Map<String, Integer> map = new HashMap<String, Integer>();

		Connection conn = DBUtil.getConn();
		String sql = "select * from keyword_tag where keyword=? order by num desc";
		PreparedStatement pstmt = DBUtil.getPstmt(conn, sql);
		try {
			pstmt.setString(1, key);
			ResultSet rs = DBUtil.getRs(pstmt);
			log.info("关键词：" + key);
			while (rs.next()) {
				map.put(rs.getString("tag")+","+rs.getInt("id"),
						Integer.parseInt(rs.getString("num")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
}
