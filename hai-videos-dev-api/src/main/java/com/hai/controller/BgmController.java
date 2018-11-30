package com.hai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hai.service.BgmService;
import com.hai.util.JSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="BGM相关的接口",tags="bgm相关接口")
@RequestMapping("/bgm")
public class BgmController extends BasicController{

	@Autowired
	private BgmService bgmService;
	
	@ApiOperation(value="展示bgm列表",notes="bgm列表接口")
	@PostMapping("/showBgm")
	public JSONResult showBgmList() {
		logger.info("展示列表方法");
		return JSONResult.ok(bgmService.getBgmList());
	}
}
