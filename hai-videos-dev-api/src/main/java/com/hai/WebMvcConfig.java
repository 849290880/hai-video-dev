package com.hai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.hai.controller.interceptor.MiniInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/META-INF/resources/")
				.addResourceLocations("file:E:/wxuploadfile/")
				.addResourceLocations("file:E:/wxBgm/");
	}
	
	
	
	@Bean(initMethod="init")
	public ZKCuratorClient createZKClient() {
		return new ZKCuratorClient();
	}
	
	/**
	 * 加入Spring容器当中
	 * @return
	 */
	@Bean
	public MiniInterceptor getMiniInterceptor() {
		return new MiniInterceptor();	
	}

	/**
	 * 注册拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getMiniInterceptor()).addPathPatterns("/user/**")
													 .addPathPatterns("/video/uploadVideo")
													 .addPathPatterns("/bgm/showBgm")
													 .excludePathPatterns("/user/showCarePeople");
		super.addInterceptors(registry);
	}
	
	
}
