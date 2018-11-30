package com.hai.service;

import java.util.List;

import com.hai.pojo.Bgm;

public interface BgmService {
	
	/**
	 * @description: 查询展示所有的BGM
	 * @return
	 */
	public List<Bgm> getBgmList();
	
	/**
	 * @description: 根据bgmId查询BGM
	 * @return
	 */
	public Bgm getBgm(String audioId);
	
	
}
