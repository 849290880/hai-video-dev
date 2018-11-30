package com.hai.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hai.mapper.CommentsMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestComments {
	
	@Autowired
	private CommentsMapper commentsMapper;
	
	@Test
	public void testGetAllComments() {
		System.out.println(commentsMapper.selectAll().size());
		System.out.println("111");
	}
}
