package com.hai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "com.hai")
@PropertySource("classpath:resource-prod.properties")
public class Resource {

	private String downLoadBgmPath;
	
	private String localbgmPath;
	
	private String connectserver;
	
	private String filespace;

	public String getDownLoadBgmPath() {
		return downLoadBgmPath;
	}

	public void setDownLoadBgmPath(String downLoadBgmPath) {
		this.downLoadBgmPath = downLoadBgmPath;
	}

	public String getLocalbgmPath() {
		return localbgmPath;
	}

	public void setLocalbgmPath(String localbgmPath) {
		this.localbgmPath = localbgmPath;
	}

	public String getConnectserver() {
		return connectserver;
	}

	public void setConnectserver(String connectserver) {
		this.connectserver = connectserver;
	}

	public String getFilespace() {
		return filespace;
	}

	public void setFilespace(String filespace) {
		this.filespace = filespace;
	}
	
}
