package com.shenghesun.treasure.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.company.dao.CompanyMessageDao;
import com.shenghesun.treasure.system.company.CompanyMessage;

@Service
public class CompanyMessageService {

	@Autowired
	CompanyMessageDao companyMessageDao;
	public CompanyMessage findById(Long id) {
		return companyMessageDao.findOne(id);
	}

	public CompanyMessage save(CompanyMessage companyMessage) {
		return companyMessageDao.save(companyMessage);
	}

	public String maxCompanyNo() {
		return companyMessageDao.findByCustomerMax();
	}
}
