package org.ansj.demo;

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

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.util.DBUtil;

public class KeyWordTestDemo {
	//数据清洗：可以配置一些，垃圾词汇，遇到这些词汇，不需要进行标签匹配。
 private final static 	List<String> useless = new ArrayList<String>();
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
		useless.add("两个"); 
		useless.add("比较"); 
		useless.add("自己"); 
		useless.add("有用吗"); 
		useless.add("时候"); 
		useless.add("买的"); 
		useless.add("买得"); 
		useless.add("建议"); 
		
	}
  public static void main(String[] args) {
	  Map<String,Integer> map = new HashMap<String,Integer>();
	  
	  KeyWordComputer kwc = new KeyWordComputer(20);
		//提取关键词
//	  String title = "我家松狮两个月，买得时候说打了血清，还没有打疫苗。能否给些建议，什么牌子得疫苗比较好？";
	  String title = "狗和猫必须接种疫苗吗？";
//	  String title = "怎样挑选健康犬？？";
	//  String title = "如何为猫防跳蚤？";
		List<Keyword> result = kwc.computeArticleTfidf(title, null);
		System.out.println(result);
		for(Keyword k : result){
			if(!useless.contains(k.getName())){
				 Map<String,Integer> m = recommentTag(k.getName());
				 for(Map.Entry<String, Integer> ma : m.entrySet()){
					String str = ma.getKey();
					 if(map.containsKey(str)){
							Integer v = map.get(str);
							v = v+ma.getValue();
							map.put(str, v);
						}else{
						map.put(str,ma.getValue());
						}
						
					}
				 
		}
			}
		if(map.size()>0){
		//根据value排序
	      List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
	        //然后通过比较器来实现排序
	        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
	            //降序排序
	            public int compare(Entry<String, Integer> o1,
	                    Entry<String, Integer> o2) {
	                return o2.getValue().compareTo(o1.getValue());
	            }
	            
	        });
	        
	        for(Map.Entry<String,Integer> mapping:list){ 
	               System.out.println(mapping.getKey()+":"+mapping.getValue()); 
	          }
	        }else{
	        	  System.out.println("没有推荐的标签，推荐默认标签");
	          }
}
  
 public static  Map<String,Integer>  recommentTag(String key){
	 Map<String,Integer> map = new HashMap<String,Integer>();
	 
	Connection conn =  DBUtil.getConn();
	String sql = "select * from keyword_tag where keyword=? order by num desc";
	PreparedStatement pstmt = DBUtil.getPstmt(conn, sql);
	try {
		pstmt.setString(1, key);
		ResultSet rs = DBUtil.getRs(pstmt);
		System.out.println("关键词："+key);
		while(rs.next()){
			map.put(rs.getString("tag"),Integer.parseInt(rs.getString("num")));
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
return map;
  }
}
