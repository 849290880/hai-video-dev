package com.hai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hai.pojo.Comments;
import com.hai.pojo.vo.CommentsVO;
import com.hai.service.CommentsService;
import com.hai.util.JSONResult;
import com.hai.util.PageResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户评论的接口",tags={"用户评论的接口"})
@RequestMapping("/comment")
public class CommentController extends BasicController{
	
	@Autowired
	private CommentsService commentsService;

	@PostMapping("/commentVideo")
	@ApiOperation(value="用户评论视频的接口",notes="用户评论视频的接口")
	public JSONResult commentVideo(@RequestBody Comments comments) {
		commentsService.addComments(comments);
		return JSONResult.ok();
	}
	
	@PostMapping("/showCommentVideo")
	@ApiOperation(value="展示用户评论的接口",notes="展示用户评论的接口")
	public JSONResult showCommentVideo(int pageNumber,String videoId) {
		PageResult<CommentsVO> pr = commentsService.showAllCommentsByVideoId(pageNumber, videoId, PAGESIZE);
		return JSONResult.ok(pr);
	}
}
