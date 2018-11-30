package com.hai.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hai.Resource;
import com.hai.util.RedisOperator;

public class BasicController {
	
	@Autowired
	public RedisOperator redisOperator;
	
	@Autowired
	public Resource resource;
	
	public final Logger logger=LoggerFactory.getLogger(getClass());
	
	public final static String USER_REDIS_SESSION = "user_redis_session";
	
	//每一页的长度
	public static final int PAGESIZE=5;
}
