package com.hai.service.impl;

import java.util.Date;
import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hai.mapper.CommentsMapper;
import com.hai.mapper.CommentsVOMapper;
import com.hai.pojo.Comments;
import com.hai.pojo.vo.CommentsVO;
import com.hai.service.CommentsService;
import com.hai.util.PageResult;

@Service
public class CommentsServiceImpl implements CommentsService {
	
	@Autowired
	private CommentsMapper commentsMapper;
	
	@Autowired
	private CommentsVOMapper commentsVOMapper;
	
	@Autowired
	private Sid sid;


	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void addComments(Comments comments) {
		comments.setId(sid.nextShort());
		comments.setCreateTime(new Date());
		commentsMapper.insert(comments);
	}


	@Override
	public PageResult<CommentsVO> showAllCommentsByVideoId(int pageNumber, String videoId,int pageSize) {
		
		PageHelper.startPage(pageNumber, pageSize);
//		Example commentExample = new Example(Comments.class);
//		Criteria commentCriteria = commentExample.createCriteria();
//		commentCriteria.andEqualTo("videoId", videoId);
//		List<Comments> list = commentsMapper.selectByExample(commentExample);
		
		List<CommentsVO> list = commentsVOMapper.selectCommentVOByVideoId(videoId);
		
		PageInfo<CommentsVO> pageInfo = new PageInfo<>(list);
		PageResult<CommentsVO> pr =new PageResult<>();
		pr.setCurrentPage(pageNumber);
		pr.setObject(list);
		pr.setPageTotal(pageInfo.getPages());
		pr.setRecordNumber(pageInfo.getTotal());
		return pr;
	}

}
