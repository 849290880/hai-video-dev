package com.hai.mapper;

import java.util.List;

import com.hai.pojo.Users;
import com.hai.util.MyMapper;

public interface UsersMapper extends MyMapper<Users> {
	
	public void addReciveLikeCounts(String userId);
	
	public void reduceReciveLikeCounts(String userId);
	
	public List<Users> selectUsersByFansId(String fansId);
	
	public void reduceFansCounts(String publisherId);
	
	public void addFansCounts(String publisherId);
	
}