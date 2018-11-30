package com.hai.util;

/**
 * 
 * @author Administrator
 *
 * @param <T>
 * 
 * 	状态码:
 *  200 表示成功
 * 	500 表示错误
 * 	404 找不到资源
 */
public class ResponseResult <T>{

	private Integer status;
	private String message;
	private T data;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}
