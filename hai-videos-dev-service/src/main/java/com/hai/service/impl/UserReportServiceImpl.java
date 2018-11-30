package com.hai.service.impl;

import java.util.Date;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hai.mapper.UsersReportMapper;
import com.hai.pojo.UsersReport;
import com.hai.service.UserReportService;

@Service
public class UserReportServiceImpl implements UserReportService {

	@Autowired
	private Sid sid;
	
	@Autowired
	private UsersReportMapper usersReportMapper;
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void addUserReport(UsersReport usersReport) {
		usersReport.setId(sid.nextShort());
		usersReport.setCreateDate(new Date());
		usersReportMapper.insert(usersReport);
	}

}
