package com.hai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hai.service.SearchService;
import com.hai.util.JSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value="搜索的业务",tags="搜索相关业务接口")
@RestController
@RequestMapping("/search")
public class SearchController extends BasicController{
	
	@Autowired
	private SearchService searchService;
	
	@PostMapping("/hot")
	@ApiOperation(value="查询热点",notes="查询热点的接口")
	public JSONResult queryHotPoint() {
		
		return JSONResult.ok(searchService.queryHotSearch());
	}
	
	@PostMapping("/search")
	@ApiOperation(value="用户搜索",notes="用户搜索的接口")
	@ApiImplicitParam(name="word",value="用户搜索的词语",required=true,dataType="String",paramType="query")
	public JSONResult search(@RequestParam("word") String word,String pageNumber) {
		
		return JSONResult.ok();
	}
}
