package com.hai.controller;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hai.pojo.Users;
import com.hai.pojo.vo.UsersVO;
import com.hai.service.UserService;
import com.hai.util.JSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户注册登录的接口",tags={"注册和登录的接口"})
public class RegistLoginController extends BasicController {
	
	
	@Autowired
	private UserService userService;
	
	/**
	 * @Description 注册一个用户  
	 * @return	状态码
	 */
	@PostMapping("/regist")
	@ApiOperation(value="用户注册",notes="用户注册接口")
	public JSONResult regist(@RequestBody Users user) {
		logger.info("进入用户注册方法");
		//1.用户名和密码不能为空
		if(user==null||StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			logger.info("用户名和密码为空");
			return JSONResult.errorMsg("用户名和密码不能为空");
		}
		//2.用户名不能重复
		boolean exist=userService.querryUserByUserName(user.getUsername());
		if(exist) {
			logger.info("用户名已经存在");
			return JSONResult.errorMsg("用户名已经存在");
		}
		//3.保存用户，注册信息
		user.setNickname(user.getUsername());
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		user.setFansCounts(0);
		user.setReceiveLikeCounts(0);
		user.setFollowCounts(0);
		userService.addUser(user);
		
		//隐藏密码,不返回给前端
		user.setPassword("");
		
		UsersVO usersVo = setUsersTokenAndSaveToRedis(user);
		
		logger.info("注册成功");
		return JSONResult.ok(usersVo);
	}
	
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	@PostMapping("/login")
	@ApiOperation(value="用户登录",notes="用户登录接口")
	public JSONResult login(@RequestBody Users user) {
		logger.info("进入用户登录方法");
		
		//1.检查用户是否存在
		boolean exist=userService.querryUserByUserName(user.getUsername());
		if(!exist) {
			logger.info("用户名或者密码不正确");
			return JSONResult.errorMsg("用户名或者密码不正确");
		}
		//2.检查输入密码是否正确
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		
		
		Users result=userService.checkPassword(user);
		
		
		if(result==null || !user.getPassword().equals(result.getPassword())) {
			logger.info("密码错误");
			return JSONResult.errorMsg("密码错误");
		}
		
		//保存到redis中
		UsersVO usersVo = setUsersTokenAndSaveToRedis(result);
		
		logger.info("登录成功");
		
		//3.登录成功
		return JSONResult.ok(usersVo);
	}
	
	
	/**
	 * @Description 创建Otken码并将token码和用户id存入redis中，并设置usersVo中的token值返回给客户端
	 * @param result
	 * @return
	 */
	public UsersVO setUsersTokenAndSaveToRedis(Users result) {
		//生成token码,并以"user_redis_session:userid:usertoken"的方式存入redis中
		String usertoken = UUID.randomUUID().toString();
		redisOperator.set(USER_REDIS_SESSION+":"+result.getId(), usertoken,1000*60*24);
		
		//返回token码回客户端
		UsersVO userVo = new UsersVO();
		
		BeanUtils.copyProperties(result, userVo);
		userVo.setUserToken(usertoken);
		return userVo;
	}
	
	/**
	 * @description 根据用户Id删除token码
	 * @param userId
	 * @return
	 */
	@PostMapping("/logout")
	@ApiOperation(value="用户注销",notes="用户注销接口")
	@ApiImplicitParam(name="userId",value="用户id",required=true,dataType="String",paramType="query")
	public JSONResult logout(@RequestParam("userId")String userId) {
		logger.info("用户开始注销");
		logger.info("userId:"+userId);
		redisOperator.del(USER_REDIS_SESSION+":"+userId);
		logger.info("用户已经注销");
		return JSONResult.ok("用户已经注销了");
	}
}
