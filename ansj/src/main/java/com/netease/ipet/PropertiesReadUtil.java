package com.netease.ipet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReadUtil {
	
	public static Properties get(String propertiesFilePath) throws IOException{
		InputStream is = new FileInputStream(propertiesFilePath);	
		Properties pro = new Properties();
		pro.load(is);
		return pro;
	}
	
	public static Properties getByFileName(String fileName) throws IOException{
		return get(getFilePath(fileName));
	}
	
	public static String getValue(String propertiesFilePath,String key) throws IOException{
		Properties pro = get(propertiesFilePath);
		return pro.getProperty(key);
	}
	
	public static String getRootPathValue(String fileName,String key) throws IOException{
		return getValue(getFilePath(fileName),key);
	}
	
	public static String getPath() {
		return PropertiesReadUtil.class.getClassLoader().getResource("").getPath();
	}
	
	public static String getFilePath(String fileFame) {
		String path = getPath() + fileFame;
		return path;
	}
}
