package com.hai.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hai.mapper.SearchRecordsMapper;
import com.hai.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchRecordsMapper searchRecordsMapper;
	
	@Override
	public List<String> queryHotSearch() {
		
		return searchRecordsMapper.searchHotWord();
	}

	
}
