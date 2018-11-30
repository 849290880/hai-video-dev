package com.hai.service;

import com.hai.pojo.Users;
import com.hai.util.PageResult;

public interface UserService {
	
	/**
	 * 	添加一个用户
	 */
	public void addUser(Users user);
	
	/**
	 * 根据用户id查询用户
	 * @param id
	 * @return
	 */
	public Users queryUserByUserId(String id);
	
	/**
	 *  根据用户名查询用户是否存在
	 */
	public boolean querryUserByUserName(String username) ;

	/**
	 * 检验密码是否正确
	 * @param user
	 * @return
	 */
	public Users checkPassword(Users user);
	
	/**
	 *    根据用户id查询用户是否存在
	 * @param userid
	 * @return
	 */
	public Users checkUserExistByUserId(String userid);
	
	/**
	 * @Description:根据用户Id保存用户上传图片的信息
	 * @param user
	 */
	public void updateUserImageFaceByUserId(Users user);
	
	/**
	 * @Description 查询登录者是否关注发布者
	 * @param publisherId
	 * @param loginUserId
	 * @return
	 */
	public boolean queryIsFollow(String publisherId,String loginUserId);
	
	/**
	 * 点赞添加关注
	 * @param publisherId
	 * @param loginUserId
	 */
	public void addUserFan(String publisherId,String loginUserId);
	
	/**
	 * 取消关注
	 * @param publisherId
	 * @param loginUserId
	 */
	public void reduceUserFan(String publisherId,String loginUserId);
	
	/**
	 * 根据粉丝的id分页查询得到他的关注的人
	 * @param fansId
	 * @return
	 */
	public PageResult<Users> showCarePeople(String fansId,int pageNumber,int pageSize);
}
