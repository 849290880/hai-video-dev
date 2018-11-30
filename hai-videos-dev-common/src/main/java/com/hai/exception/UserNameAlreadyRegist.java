package com.hai.exception;


public class UserNameAlreadyRegist extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	
	public UserNameAlreadyRegist() {
		
	}
	
	public UserNameAlreadyRegist(String message) {
		super(message);
	}
}
