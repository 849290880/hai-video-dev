package com.hai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hai.pojo.Reply;
import com.hai.pojo.vo.ReplyVO;
import com.hai.service.ReplyService;
import com.hai.util.JSONResult;
import com.hai.util.PageResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/reply")
@Api(value="评论回复的接口",tags={"评论回复的接口"})
public class ReplyController extends BasicController{

	@Autowired
	private ReplyService replyService;
	
	@PostMapping("/replyOther")
	public JSONResult replyOther(@RequestBody Reply reply) {
		replyService.addReply(reply);
		return JSONResult.ok();
	}
	
	@PostMapping("/showReply")
	@ApiOperation(value="根据评论Id分页查询回复的接口",notes="根据评论Id分页查询回复的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="commentId",value="评论id",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="pageNumber",value="当前的页数",required=true,dataType="int",paramType="query"),
	})
	public JSONResult showReply(String commentId,Integer pageNumber) {
		PageResult<ReplyVO> showReplyByCommentsId = replyService.showReplyByCommentsId(commentId, pageNumber, 2);
		return JSONResult.ok(showReplyByCommentsId);
	}
}
