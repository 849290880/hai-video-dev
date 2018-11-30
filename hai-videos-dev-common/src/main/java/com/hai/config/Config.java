package com.hai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="config")
public class Config {
	
	private static String FileAbsolutionPath;
	
	

	public static String getFileAbsolutionPath() {
		return FileAbsolutionPath;
	}

	public static void setFileAbsolutionPath(String fileAbsolutionPath) {
		FileAbsolutionPath = fileAbsolutionPath;
	}
	
	
}
