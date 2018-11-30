package com.hai.mapper;

import java.util.List;

import com.hai.pojo.SearchRecords;
import com.hai.util.MyMapper;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
	
	public List<String> searchHotWord();
}