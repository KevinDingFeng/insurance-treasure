package com.shenghesun.treasure.cpic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.cpic.dao.InsurancesStatusDao;
import com.shenghesun.treasure.system.cpic.InsurancesStatus;

@Service
public class InsurancesStatusService {
	@Autowired
	InsurancesStatusDao insurancesStatusDao;
	public List<InsurancesStatus> findAll(){
		return (List<InsurancesStatus>) insurancesStatusDao.findAll();
	}
}
