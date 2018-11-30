package com.hai.service;

import com.hai.pojo.Reply;
import com.hai.pojo.vo.ReplyVO;
import com.hai.util.PageResult;

public interface ReplyService {
	
	public void addReply(Reply reply);
	
	public PageResult<ReplyVO> showReplyByCommentsId(String commentId,Integer pageNumber,Integer pageSize);
}
