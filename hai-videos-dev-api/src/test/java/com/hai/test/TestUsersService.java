package com.hai.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hai.pojo.Users;
import com.hai.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUsersService {
	
	@Autowired
	private UserService us;
	
	@Test
	public void testQuerryUserByUserName() {
		Users user = new Users();
		user.setUsername("哈哈");
		user.setPassword("1111");
		boolean exist=us.querryUserByUserName(user.getUsername());
		System.out.println(exist);
	}
	
	@Test
	public void testAddUser() {
		Users user = new Users();
		user.setUsername("哈哈");
		user.setPassword("1111");
		us.addUser(user);
	}
}
