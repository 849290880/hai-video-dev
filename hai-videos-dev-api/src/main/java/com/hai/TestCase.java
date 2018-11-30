package com.hai;

import java.io.InputStream;
import java.util.Properties;

public class TestCase {
	
	public static void main(String[] args) throws Exception {
		Properties prop = new Properties();
		InputStream in = TestCase.class.getClassLoader().getResourceAsStream("resource.properties");
		System.out.println(in);
		prop.load(in);
		String property = prop.getProperty("com.hai.downLoadBgmPath");
		System.out.println(property);
	}
}
