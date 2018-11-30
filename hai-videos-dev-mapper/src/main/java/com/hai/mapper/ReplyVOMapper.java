package com.hai.mapper;

import java.util.List;

import com.hai.pojo.Reply;
import com.hai.pojo.vo.ReplyVO;
import com.hai.util.MyMapper;

public interface ReplyVOMapper extends MyMapper<Reply> {

	List<ReplyVO> selectReplyUserByCommentId(String commentId);
	
}