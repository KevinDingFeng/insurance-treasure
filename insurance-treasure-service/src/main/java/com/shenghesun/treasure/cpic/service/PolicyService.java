package com.shenghesun.treasure.cpic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.cpic.dao.PolicyDao;
import com.shenghesun.treasure.system.cpic.Policy;

@Service
public class PolicyService {
	
	@Autowired
	private PolicyDao policyDao;
	
	public Policy save(Policy policy){
		return policyDao.save(policy);
	}
	public Policy findByApplyNo(String applyNo) {
		return policyDao.findByApplyno(applyNo);
	}
}
