package com.hai.service;

import com.hai.pojo.Comments;
import com.hai.pojo.vo.CommentsVO;
import com.hai.util.PageResult;

public interface CommentsService {
	
	/**
	 * 添加一条对视频评论的信息
	 * @param comments
	 */
	public void addComments(Comments comments);
	
	public PageResult<CommentsVO> showAllCommentsByVideoId(int pageNumber,String videoId,int pageSize);
}
