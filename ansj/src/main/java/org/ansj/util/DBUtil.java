package org.ansj.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
   private DBUtil(){}
   static {
	   try{
		  Class.forName("com.mysql.jdbc.Driver");
	   }catch(ClassNotFoundException e){
		   e.printStackTrace();
		   System.out.println("驱动注册失败！");
	   }
   }
   public static Connection getConn(){
	   Connection conn = null;
	   try {
		conn = DriverManager.getConnection("jdbc:mysql:///machine","root","root");
	} catch (SQLException e) {
		e.printStackTrace();
		  System.out.println("创建连接失败！");
	}
	   return conn;
   }
   public static PreparedStatement getPstmt(Connection conn,String sql){
	   PreparedStatement pstmt = null;
	   try {
		pstmt = conn.prepareStatement(sql);
	} catch (SQLException e) {
		e.printStackTrace();
		  System.out.println("运输工具创建失败！");
	}
	   return pstmt;
   }
   public static ResultSet getRs(PreparedStatement pstmt){
	   ResultSet rs = null;
	   try {
		rs = pstmt.executeQuery();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   return rs;
   }
   public static void close(Connection conn,PreparedStatement pstmt,ResultSet rs){
	   if(rs!=null){
		   try {
			   rs.close();
			   rs = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   if(pstmt!=null){
			   try{
				   pstmt.close();
				   pstmt = null;
			   }catch(SQLException e){
				   e.printStackTrace();
			   }
		   }
		
	   }
   }
}
