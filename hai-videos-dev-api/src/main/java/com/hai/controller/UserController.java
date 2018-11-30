package com.hai.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hai.pojo.Users;
import com.hai.pojo.UsersReport;
import com.hai.pojo.vo.UsersVO;
import com.hai.pojo.vo.VideoInfoVO;
import com.hai.service.UserReportService;
import com.hai.service.UserService;
import com.hai.util.JSONResult;
import com.hai.util.PageResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户相关业务的接口",tags="用户相关业务接口")
@RequestMapping("/user")
public class UserController extends BasicController{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserReportService userReportService;
	
	
	
	@ApiOperation(value="加载用户信息",notes="显示用户信息的接口")
	@ApiImplicitParam(name="userId",value="用户Id",required=true,dataType="String",paramType="query")
	@PostMapping("/showUserInfo")
	public JSONResult showUserInfo(String userId,String loginUserId,String isMe) {
		
		if("false".equals(isMe)) {
			
			logger.info("别人用户信息的查询的方法开始...");
			
			Users user=userService.queryUserByUserId(userId);
			UsersVO usersVo = new UsersVO();
			BeanUtils.copyProperties(user, usersVo);
			boolean isFollow = userService.queryIsFollow(userId, loginUserId);
			
			//组装数据,返回对象
			VideoInfoVO<UsersVO> videoInfoVO = new VideoInfoVO<UsersVO>();
			videoInfoVO.setFollow(isFollow);
			videoInfoVO.setData(usersVo);
			return JSONResult.ok(videoInfoVO);
		}
		logger.info("个人用户信息的查询的方法开始...");
		Users user=userService.queryUserByUserId(userId);
		UsersVO usersVo = new UsersVO();
		BeanUtils.copyProperties(user, usersVo);
		return JSONResult.ok(usersVo);
	}
	
	@ApiOperation(value="用户头像上传",notes="用户头像上传的接口")
	@ApiImplicitParam(name="userId",value="用户id",required=true,dataType="String",paramType="query")
	@PostMapping("/uploadface")
	public JSONResult uploadFace(String userId,@RequestParam("file")MultipartFile[] files) {
		logger.info("头像上传开始");
		if(userId==null) {
			return JSONResult.errorMsg("用户id不能为空");
		}
		//查询用户是否存在
		Users result=userService.checkUserExistByUserId(userId);
		if(result==null) {
			return JSONResult.errorMsg("该用户不存在");
		}
		logger.info("文件长度为:"+files.length);
		//图片不能为空
		if(files==null||files.length<1) {
			return JSONResult.errorMsg("图片不能为空");
		}
		if(StringUtils.isBlank(files[0].getContentType())) {
			return JSONResult.errorMsg("文件名不能为空");
		}
		//创建文件
		
		//数据库存储文件夹
		String imagePathDiretory = "/"+userId+"/face/";
		
		//图片名字
		String imageName=files[0].getOriginalFilename();
		//图片文件路径
		String imagePath=resource.getFilespace()+imagePathDiretory+imageName;
		
		//数据库存储位置
		String imagePathDB = imagePathDiretory + imageName;
		
		//文件对象
		File file = new File(imagePath);
		//创建文件夹
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		//将图片持久化
		FileOutputStream out=null;
		InputStream in=null;
		try {
			out = new FileOutputStream(file);
			in = files[0].getInputStream();
			IOUtils.copy(in, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(out!=null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//将图片保存或者更新到数据库
		Users user = new Users();
		user.setFaceImage(imagePathDB);
		user.setId(userId);
		userService.updateUserImageFaceByUserId(user);
		
		
		logger.info("头像上传成功");
		return JSONResult.ok(imagePathDB);
	}
	
	@ApiOperation(value="用户点赞",notes="用户点赞的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="usersId",value="被关注者id",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="fansId",value="关注者id",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="isFollow",value="是否关注",required=true,dataType="String",paramType="query"),
	})
	@PostMapping("/clickFans")
	public JSONResult clickFans(String usersId,String fansId,String isFollow) {
		
		
		System.out.println("usersId"+usersId);
		System.out.println("fansId"+fansId);
		System.out.println("isFollow"+isFollow);
		
		if("true".equals(isFollow)) {
			//关注
			userService.addUserFan(usersId, fansId);
			return JSONResult.ok();
		}
		//取消关注
		userService.reduceUserFan(usersId, fansId);
		return JSONResult.ok();
	}
	
	@ApiOperation(value="显示粉丝关注的人",notes="显示粉丝关注的人的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="fansId",value="粉丝id",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="pageNumber",value="当前的页数",required=true,dataType="int",paramType="query"),
	})
	@PostMapping("/showCarePeople")
	public JSONResult showCarePeple(String fansId,int pageNumber) {
		PageResult<Users> pr =userService.showCarePeople(fansId, pageNumber, PAGESIZE);
		
		return JSONResult.ok(pr);
	}
	
	@ApiOperation(value="用户举报视频",notes="用户举报视频的接口")
	@ApiImplicitParams({		
		@ApiImplicitParam(name="videoInfo",value="videoInfo的JSON字符",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="loginUserId",value="举报人的Id",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="reasonType",value="举报类型",required=true,dataType="String",paramType="query"),
		@ApiImplicitParam(name="reasonContent",value="举报详情",required=false,dataType="String",paramType="query")
	})
	@PostMapping("/reportUser")
	public JSONResult reportUser(@RequestBody UsersReport usersReport) {
		
		userReportService.addUserReport(usersReport);
		
		return JSONResult.ok();
	}
}
