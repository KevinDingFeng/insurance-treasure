package com.shenghesun.treasure.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.order.dao.FundDetailsDao;
import com.shenghesun.treasure.system.order.FundDetails;

@Service
public class FundDetailsService {

	@Autowired
	private FundDetailsDao fundDetailsDao;

	public void save(FundDetails fd) {
		fundDetailsDao.save(fd);
	}
	public List<FundDetails> findByCompanyId(String companyid){
		return fundDetailsDao.findByCompanyId(companyid);
	}
}
