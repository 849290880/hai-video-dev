package com.hai.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hai.mapper.BgmMapper;
import com.hai.pojo.Bgm;
import com.hai.service.BgmService;

@Service
public class BgmServiceImpl implements BgmService {

	@Autowired
	private BgmMapper bgmMapper;
	
	@Override
	public List<Bgm> getBgmList() {
		return bgmMapper.selectAll();
	}

	@Override
	public Bgm getBgm(String audioId) {
		Bgm bgm = new Bgm();
		bgm.setId(audioId);
		return bgmMapper.selectOne(bgm);
	}

}
