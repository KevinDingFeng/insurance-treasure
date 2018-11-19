package com.shenghesun.treasure.cpic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.cpic.dao.ApprovlDao;
import com.shenghesun.treasure.system.cpic.Approvl;

@Service
public class ApprovlService {

	@Autowired
	private ApprovlDao approvlDao;
	
	public Approvl save(Approvl approvl) {
		return approvlDao.save(approvl);
	}
	
	public Approvl findByApplyId(String applyId) {
		return approvlDao.findByApplyId(applyId);
	}
	public Approvl findByOrderNo(String orderNo) {
		return approvlDao.findByOrderNo(orderNo);
	}
}
