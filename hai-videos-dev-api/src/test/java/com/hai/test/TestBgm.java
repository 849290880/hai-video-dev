package com.hai.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hai.mapper.BgmMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestBgm {
	
	@Autowired
	private BgmMapper bgmMapper;
	
	@Test
	public void testGetAllBgm() {
		System.out.println(bgmMapper.selectAll().size());
		System.out.println("111");
	}
}
