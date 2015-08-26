package org.ansj.demo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.util.DBUtil;

/**
 * 关键词提取的例子
 * @author ansj
 *
 */
public class KeyWordCompuerDemo {
	//数据清洗：可以配置一些，垃圾词汇，遇到这些词汇，不需要进行标签匹配。
 private final static 	List<String> useless = new ArrayList<String>();
 private static Connection conn = null;
	static{
		useless.add("怎么办");
		useless.add("如何");
		useless.add("什么"); 
		useless.add("哪些"); 
		useless.add("是否"); 
		useless.add("应该"); 
		useless.add("一只"); 
		useless.add("突然"); 
		useless.add("原因"); 
		useless.add("怎样"); 
		useless.add("还是"); 
		useless.add("可以"); 
		useless.add("怎么"); 
		useless.add("是不是"); 
		useless.add("能否"); 
		useless.add("没有"); 
		useless.add("我家"); 
		useless.add("一些"); 
		useless.add("几个"); 
		useless.add("有用"); 
		useless.add("不好"); 
		useless.add("这个"); 
		useless.add("不能"); 
		useless.add("多月"); 
		useless.add("可不可以"); 
		useless.add("什么样"); 
		useless.add("一天"); 
		useless.add("三个月"); 
		useless.add("到底"); 
		useless.add("自己"); 
		useless.add("有用吗"); 
		useless.add("时候"); 
		conn = DBUtil.getConn();
		
	}
	public static void main(String[] args) {
		readFile("E:\\data\\wmr-utf8.txt");
	//	readFile("E:\\data\\wxl-utf8.txt");
	//	readFile("E:\\data\\wyl-utf8.txt");
	//	readFile("E:\\data\\wys-utf8.txt");
	}
	public static void readFile(String filename) {
		try {
			InputStream fis = new FileInputStream(filename);
			InputStreamReader isr;
			isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String str = null;
			int i = 0;
			String title = "";
			String ta  = "";
			while ((str = br.readLine()) != null) {
				i++;
				if (!str.contains("--")) {
					
					if (str.indexOf("提问") == 0) {
						title = "'"+str.substring(3)+"'";
					} else if (str.indexOf("问题详情") == 0) {
						//System.out.println("detail:" + str.substring(5));
					} else if (str.indexOf("答案") == 0) {
						//System.out.println("answer" + str.substring(3));
					} else if (str.indexOf("已采纳答案") == 0) {
					//	System.out.println("accept answer:" + str.substring(6));
					} else if (str.indexOf("标签") == 0) {
						ta = str.substring(3);
						/*String tag = str.substring(3);
						String[] tags = tag.split("，");
						System.out.print("tag:" );
						for(String t:tags){
							System.out.print(t+" ");
						}
						System.out.println();
						*/
					}
				}else if (str.contains("--")) {
					KeyWordComputer kwc = new KeyWordComputer(20);
					//提取关键词
					List<Keyword> result = kwc.computeArticleTfidf(title, null);
					
					
            String tags[] = ta.split("[ |，]");
          System.out.println(result+"对应的标签："+ta);
			for(Keyword k : result){
                 String key = k.getName();
                 if(!useless.contains(key)){ 
                 for(String s : tags){
                	 String sql = "select * from keyword_tag where keyword=? and tag=?";
                	 String sql1 = "update keyword_tag set num=? where id=?";
                	 String sql2 = "insert into keyword_tag values(null,?,?,1)";
                	PreparedStatement pstmt = DBUtil.getPstmt(conn, sql);
                     try {
						pstmt.setString(1, key);
						pstmt.setString(2, s);
						ResultSet rs = DBUtil.getRs(pstmt);
						if(rs.next()){  //已經存在，則去除num 加1
							int num = rs.getInt("num");
							num++;
							PreparedStatement pstmt1 = DBUtil.getPstmt(conn, sql1);
							pstmt1.setInt(1, num);
							pstmt1.setInt(2, rs.getInt(1));
							 pstmt1.executeUpdate();
						}else{
							PreparedStatement pstmt2 = DBUtil.getPstmt(conn, sql2);
							pstmt2.setString(1, key);
							pstmt2.setString(2, s);
							pstmt2.executeUpdate();
						}
						 System.out.println(key+","+s);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                     
                	
                 }}
			}
            
					//Collections.sort(result);
					//System.out.println(result+"对应的标签："+ta);
				}
				
				
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
	}
}
