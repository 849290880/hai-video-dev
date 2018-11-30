package com.hai.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hai.mapper.UsersFansMapper;
import com.hai.mapper.UsersMapper;
import com.hai.pojo.Users;
import com.hai.pojo.UsersFans;
import com.hai.service.UserService;
import com.hai.util.PageResult;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


@Service
public class UserServiceImpl implements UserService{
		
	@Resource
	private UsersMapper usersMapper;
	
	@Autowired
	private UsersFansMapper usersFansMapper;
	
	@Autowired
	private Sid sid;
	
	/**
	 * 	添加一个用户
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void addUser(Users user) {
		String id = sid.nextShort();
		user.setId(id);
		usersMapper.insert(user);
	}

	/**
	 *   根据用户名查询用户
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public boolean querryUserByUserName(String username) {
		Users user = new Users();
		user.setUsername(username);
		Users result = usersMapper.selectOne(user);
		return result==null?false:true;
	}

	/**
	 *   检验密码是否正确
	 */
	public Users checkPassword(Users user) {
		//1.根据用户名查询用户
		Users condition = new Users();
		condition.setUsername(user.getUsername());
		Users result=usersMapper.selectOne(condition);
		
		return result;
	}
	
	@Override
	public Users checkUserExistByUserId(String userid) {
		Users user = new Users();
		user.setId(userid);
		Users result=usersMapper.selectOne(user);
		return result;
	}

	@Override
	public void updateUserImageFaceByUserId(Users user) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id", user.getId());
		usersMapper.updateByExampleSelective(user, userExample);
	}

	@Override
	public Users queryUserByUserId(String userid) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id",userid);
		Users user=usersMapper.selectOneByExample(userExample);
		return user;
	}

	@Override
	public boolean queryIsFollow(String publisherId, String loginUserId) {
		boolean result=false;
		Example example = new Example(UsersFans.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId",publisherId).andEqualTo("fanId",loginUserId);
		UsersFans usersFans=usersFansMapper.selectOneByExample(example);
		if(usersFans!=null) {
			result=true;
		}
		return result;
	}

	/**
	 * user表粉丝数加1,user_fans表添加关注记录
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void addUserFan(String publisherId, String loginUserId) {
		UsersFans usersFans = new UsersFans();
		usersFans.setId(sid.nextShort());
		usersFans.setUserId(publisherId);
		usersFans.setFanId(loginUserId);
		usersFansMapper.insert(usersFans);
		usersMapper.addFansCounts(publisherId);
	}

	/**
	 * user表更新数据,user_fans表减少关注记录
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void reduceUserFan(String publisherId, String loginUserId) {
		Example example = new Example(UsersFans.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("userId",publisherId).andEqualTo("fanId", loginUserId);
		usersFansMapper.deleteByExample(example);
		usersMapper.reduceFansCounts(publisherId);
	}

	@Override
	public PageResult<Users> showCarePeople(String fansId,int pageNumber,int pageSize) {
		
		PageHelper.startPage(pageNumber, pageSize);
		
		List<Users> list=usersMapper.selectUsersByFansId(fansId);
		
		PageInfo<Users> pageInfo = new PageInfo<>(list);
		PageResult<Users> pr = new PageResult<Users>();
		pr.setCurrentPage(pageNumber);
		pr.setObject(list);
		pr.setPageTotal(pageInfo.getPages());
		pr.setRecordNumber(pageInfo.getTotal());
		
		System.out.println(list.size());
		
		return pr;
	}

}
