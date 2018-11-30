package com.hai.mapper;

import java.util.List;

import com.hai.pojo.Comments;
import com.hai.pojo.vo.CommentsVO;
import com.hai.util.MyMapper;

public interface CommentsVOMapper extends MyMapper<Comments> {
	
	public List<CommentsVO> selectCommentVOByVideoId(String videoId);
}