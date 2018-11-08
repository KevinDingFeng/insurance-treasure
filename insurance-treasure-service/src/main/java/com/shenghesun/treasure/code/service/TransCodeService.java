package com.shenghesun.treasure.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.code.dao.TransCodeDao;
import com.shenghesun.treasure.system.code.TransCode;

@Service
public class TransCodeService {

	@Autowired
	private TransCodeDao transCodeDao;
	
	public List<TransCode> find(){
		return transCodeDao.findAll();
	}
}
