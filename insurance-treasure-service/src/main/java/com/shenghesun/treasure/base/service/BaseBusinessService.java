package com.shenghesun.treasure.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.base.dao.BaseBusinessDao;
import com.shenghesun.treasure.system.dictionary.BaseBusiness;

@Service
public class BaseBusinessService {

	@Autowired
	private BaseBusinessDao baseBusinessDao;
	
	public List<BaseBusiness> find(){
		return baseBusinessDao.findAll();
	}
}
