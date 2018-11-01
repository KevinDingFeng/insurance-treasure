package com.shenghesun.treasure.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.order.dao.FundDetailsDao;
import com.shenghesun.treasure.system.order.FundDetails;

@Service
public class FundDetailsService {

	@Autowired
	private FundDetailsDao fundDetailsDao;

	public void save(FundDetails fd) {
		// TODO Auto-generated method stub
		fundDetailsDao.save(fd);
	}
	
}
