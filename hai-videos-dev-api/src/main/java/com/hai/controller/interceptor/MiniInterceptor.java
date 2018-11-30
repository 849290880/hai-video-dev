package com.hai.controller.interceptor;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hai.util.JSONResult;
import com.hai.util.JsonUtils;
import com.hai.util.RedisOperator;

public class MiniInterceptor implements HandlerInterceptor {

	@Autowired
	public RedisOperator redisOperator;
	
	
	public final static String USER_REDIS_SESSION = "user_redis_session";
	
	/**
	 * 请求到达controller前,对请求进行处理
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/**
		 * 返回false:请求被拦截,返回
		 * 返回true: 请求OK，可以继续执行，放行
		 */
		String userId = request.getHeader("userId");
		String userToken=request.getHeader("userToken");
		String token=redisOperator.get(USER_REDIS_SESSION+":"+userId);
		System.out.println("userId"+userId);
		System.out.println("userToken"+userToken);
		System.out.println("token"+token);
		if(StringUtils.isBlank(userId)||StringUtils.isEmpty(userToken)) {
			//没有登录
			String msg = "请登录~~~";
			returnErrorJSON(request,response,JSONResult.errorTokenMsg(msg));
			return false;
		}else if(StringUtils.isEmpty(token)) {
			//登录过期
			String msg = "登录过期啦~";
			returnErrorJSON(request,response,JSONResult.errorTokenMsg(msg));
			return false;
		}else if(!userToken.equals(token)) {
			//登录错误
			String msg = "被别人挤了";
			returnErrorJSON(request,response,JSONResult.errorTokenMsg(msg));
			return false;
		}
		
		System.out.println("拦截到了,哈哈哈哈");
		return true;
	}

	/**
	 * controller处理请求之后,页面渲染之前
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * controller处理之后,页面渲染之后
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
	
	/**
	 * 组成JSON格式数据发送给客户端
	 * @param request
	 * @param response
	 * @param msg
	 */
	public void returnErrorJSON(HttpServletRequest request, HttpServletResponse response,JSONResult jsonResult) {
		OutputStream out = null;
		try {
			response.setContentType("text/json");
			response.setCharacterEncoding("utf-8");
			out=response.getOutputStream();
			out.write(JsonUtils.objectToJson(jsonResult).getBytes("utf-8"));
		} catch (Exception e) {
			if(out!=null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
