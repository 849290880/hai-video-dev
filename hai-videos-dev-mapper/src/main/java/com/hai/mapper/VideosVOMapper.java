package com.hai.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hai.pojo.Videos;
import com.hai.pojo.vo.VideosVO;
import com.hai.util.MyMapper;

public interface VideosVOMapper extends MyMapper<Videos> {
	
	public List<VideosVO> selectAllVideosVO(@Param("word") String word);
	
	public void addCounts(String videoId);
	
	public void reduceCounts(String videoId);
	
	public List<VideosVO> selectAllUserLikeVideosVO(String publisherId);
	
	public List<VideosVO> selectVideoVOByUserId(String userId);
 }