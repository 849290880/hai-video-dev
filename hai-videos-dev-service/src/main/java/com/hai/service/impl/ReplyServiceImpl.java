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
import com.hai.mapper.ReplyMapper;
import com.hai.mapper.ReplyVOMapper;
import com.hai.pojo.Reply;
import com.hai.pojo.vo.ReplyVO;
import com.hai.service.ReplyService;
import com.hai.util.PageResult;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper replyMapper;
	
	@Autowired
	private ReplyVOMapper replyVOmapper;
	
	@Autowired
	private Sid sid;
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void addReply(Reply reply) {
		reply.setId(sid.nextShort());
		reply.setCreateTime(new Date());
		replyMapper.insert(reply);
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public PageResult<ReplyVO> showReplyByCommentsId(String commentId,Integer pageNumber,Integer pageSize) {
		PageHelper.startPage(pageNumber, pageSize);
		List<ReplyVO> list=replyVOmapper.selectReplyUserByCommentId(commentId);
		PageInfo<ReplyVO> pageInfo = new PageInfo<>(list);
		PageResult<ReplyVO> pr =new PageResult<>();
		pr.setCurrentPage(pageNumber);
		pr.setObject(list);
		pr.setPageTotal(pageInfo.getPages());
		pr.setRecordNumber(pageInfo.getTotal());
		return pr;
		
	}

}
