package com.hai;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @Description WarStartApplication 继承SpringBootServletInitializer就会以web.xml的形式去启动
 */
public class WarStartApplication extends SpringBootServletInitializer{

	/**
	 * @Description 重写 configure
	 */
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		//使用builder.sources() 指向启动类
		return builder.sources(Application.class);
	}
	
}
