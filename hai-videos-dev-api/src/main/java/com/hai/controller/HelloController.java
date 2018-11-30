package com.hai.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController extends BasicController{

	@RequestMapping("/hello")
	public String Hello() {
		return "Hello World";
	}
}
