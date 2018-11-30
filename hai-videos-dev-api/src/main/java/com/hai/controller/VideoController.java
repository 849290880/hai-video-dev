package com.hai.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.hai.Resource;
import com.hai.pojo.Bgm;
import com.hai.pojo.Users;
import com.hai.pojo.Videos;
import com.hai.pojo.vo.VideoInfoVO;
import com.hai.pojo.vo.VideosVO;
import com.hai.service.BgmService;
import com.hai.service.UserService;
import com.hai.service.VideoService;
import com.hai.util.JSONResult;
import com.hai.util.JsonUtils;
import com.hai.util.PageResult;
import com.hai.util.VideoUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value="视频相关的业务",tags="视频相关业务接口")
@RestController
@RequestMapping("/video")
public class VideoController extends BasicController{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BgmService bgmService;
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private Resource resource;
	
	@Autowired
	private Sid sid;
	
	@ApiOperation(value="上传视频",notes="上传视频的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户Id",required=true,dataType="String",paramType="form"),
		@ApiImplicitParam(name="audioId",value="音频Id",required=false,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoDesc",value="视频描述",required=false,dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoSeconds",value="视频长度",required=true,dataType="float",paramType="form"),
		@ApiImplicitParam(name="videoWidth",value="视频宽度",required=true,dataType="int",paramType="form"),
		@ApiImplicitParam(name="videoHeight",value="视频高度",required=true,dataType="int",paramType="form")
	})
	@PostMapping(value="/uploadVideo",headers="content-type=multipart/form-data")
	public JSONResult upLoadVedio(String userId,String audioId,String videoDesc,
			float videoSeconds,int videoWidth,int videoHeight,
			@ApiParam(value="视频",required=true)
			MultipartFile file) {
		logger.info("上传视频方法开始...");
		if(userId==null) {
			return JSONResult.errorMsg("用户id不能为空");
		}
		//查询用户是否存在
		Users result=userService.checkUserExistByUserId(userId);
		if(result==null) {
			return JSONResult.errorMsg("该用户不存在");
		}
		
		//图片不能为空
		if(file==null) {
			return JSONResult.errorMsg("视频不能为空");
		}
		if(StringUtils.isBlank(file.getOriginalFilename())) {
			return JSONResult.errorMsg("文件名不能为空");
		}

		String videoName = file.getOriginalFilename();
		
		//封面图片的名字 
		String coverName=videoName.split("\\.")[0]+"cover.jpg";
		
		//保存在数据库的视频的相对路径
		String videoPathDB="/"+userId+"/video/"+videoName;
		//保存在数据库的视频封面的相对路径
		String coverPath = "/"+userId+"/video/"+coverName;
		
		//创建文件目录
		File videoFile = new File(resource.getFilespace()+videoPathDB);
		if(!videoFile.getParentFile().exists()) {
			videoFile.getParentFile().mkdirs();
		}
		
		//保存到文件夹下
		FileOutputStream out =null;
		try {
			InputStream in = file.getInputStream();
			out= new FileOutputStream(videoFile);
			IOUtils.copy(in, out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//视频存储的位置
		String videoAbsolutePath=resource.getFilespace()+videoPathDB;
		//假如有bgmId就合并音乐
		if(StringUtils.isNotBlank(audioId)) {
			logger.info("视频与音频结合开始...");
			//根据audioId查询出audioPath
			Bgm bgm=bgmService.getBgm(audioId);
			//bgm存储位置
			String bgm_path = resource.getDownLoadBgmPath()+bgm.getPath();
			
			logger.info("bgm的位置:"+bgm_path);
			
			
			
			
			logger.info("视频的位置:"+videoAbsolutePath);
			//bgm与视频合成
			try {
				VideoUtils.convertor(videoAbsolutePath, bgm_path, videoAbsolutePath, videoSeconds);
			} catch (Exception e) {
				logger.info("视频合成失败");
				e.printStackTrace();
			}
			logger.info("视频与音频结合结束");
		}
		
		//图片保持的绝对路径
		String coverAbsolutePath = resource.getFilespace()+coverPath;
		//视频保持的绝对路径
//		String vedioAbsolutePath = resource.getFilespace()+videoPathDB;
		
		//使用ffmpeg截取图片,保存在文件中
		VideoUtils.fetchCover(videoAbsolutePath, coverAbsolutePath);
		
		//更新数据库
		Videos video = new Videos();
		video.setId(sid.nextShort());
		video.setUserId(userId);
		video.setAudioId(audioId);
		video.setVideoDesc(videoDesc);
		video.setVideoPath(videoPathDB);
		video.setVideoSeconds(videoSeconds);
		video.setVideoWidth(videoWidth);
		video.setVideoHeight(videoHeight);
		video.setCoverPath(coverPath);
		video.setStatus(1);
		video.setLikeCounts(0l);
		video.setCreateTime(new Date());
		
		System.out.println("#############");
		System.out.println("视频对象:"+video);
		System.out.println("#############");
		
		videoService.addVideo(video);
		
		logger.info("上传视频方法结束");
		return JSONResult.ok();
	}
	
	/**
	 * isSearch 1模糊 0正常搜索
	 * @param pageNumber
	 * @param word
	 * @param isSearch 
	 * @return
	 */
	@PostMapping("/showVideo")
	@ApiOperation(value="分页查询视频展现",notes="分页查询视频展现的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="pageNumber",value="当前的页码",required=true,dataType="int",paramType="query"),
		@ApiImplicitParam(name="word",value="用户搜索词",required=false,dataType="String",paramType="query"),
		@ApiImplicitParam(name="isSearch",value="搜索标记",required=true,dataType="String",paramType="query")
	})
	public JSONResult showVideo(Integer pageNumber,String word,String isSearch) {
		
		logger.info("显示视频的方法开始....");
		logger.info("word"+word);
		logger.info("isSearch"+isSearch);
		
		if(pageNumber==null) {
			pageNumber = 1;
		}
		PageResult<VideosVO> pr=videoService.queryAll(pageNumber, PAGESIZE,isSearch,word);
		return JSONResult.ok(pr);
	}
	
	@ApiOperation(value="点赞视频",notes="点赞视频的接口")
	@ApiImplicitParam(name="loginUserId",value="登录者的Id",required=true,paramType="query",dataType="String")
	@PostMapping("/likeVideo")
	public JSONResult likeVideo(String videoInfo,String loginUserId) {
//	public JSONResult likeVideo(HttpServletRequest request) {
		
//		String loginUserId=request.getParameter("loginUserId");
//		String videoInfo=request.getParameter("videoInfo");
		
		VideosVO videoInfoObject=JsonUtils.jsonToPojo(videoInfo, VideosVO.class);
		
		logger.info("videoInfo:"+videoInfo);
		logger.info("loginUserId:"+loginUserId);
		
		videoService.likeVedio(videoInfoObject, loginUserId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value="取消点赞",notes="取消点赞的接口")
	@ApiImplicitParam(name="loginUserId",value="登录者的Id",required=true,paramType="query",dataType="String")
	@PostMapping("/unlikeVideo")
	public JSONResult unlikeVideo(String videoInfo,String loginUserId) {
		
		logger.info("videoInfo:"+videoInfo);
		logger.info("loginUserId:"+loginUserId);
		
		VideosVO videoInfoObject=JsonUtils.jsonToPojo(videoInfo, VideosVO.class);
		
		videoService.unlikeVedio(videoInfoObject, loginUserId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value="显示视频页面信息",notes="显示视频页面信息的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="loginUserId",value="登录者的Id",required=true,paramType="query",dataType="String"),
		@ApiImplicitParam(name="videoId",value="视频的Id",required=true,paramType="query",dataType="String")
	})
	@PostMapping("/showVideoInfo")
	public JSONResult showVideoInfo(String loginUserId,String videoId) {
		
		VideoInfoVO<Void> videoInfoVO=videoService.showVideoInfo(loginUserId, videoId);
		
		return JSONResult.ok(videoInfoVO);
	}
	
	
	@ApiOperation(value="显示某个用户作品",notes="显示某个用户作品的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="publisherId",value="发布者的Id",required=true,paramType="query",dataType="String"),
		@ApiImplicitParam(name="pageNumber",value="页数",required=true,paramType="query",dataType="int")
	})
	@PostMapping("/showUserVideo")
	public JSONResult showVideoByUserId(String publisherId,int pageNumber) {
		PageResult<VideosVO> pr=videoService.showUserVideo(publisherId, pageNumber, PAGESIZE+1);
		return JSONResult.ok(pr);
	}
	
	@ApiOperation(value="显示某个用户收藏",notes="显示某个用户收藏的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="publisherId",value="发布者的Id",required=true,paramType="query",dataType="String"),
		@ApiImplicitParam(name="pageNumber",value="页数",required=true,paramType="query",dataType="int")
	})
	@PostMapping("/showUserCareVideo")
	public JSONResult showUserCareVideo(String publisherId,int pageNumber) {
		PageResult<VideosVO> pr=videoService.showUserCareVideo(publisherId, pageNumber, PAGESIZE+1);
		return JSONResult.ok(pr);
	}
}
