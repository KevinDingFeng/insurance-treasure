package com.shenghesun.treasure.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.code.dao.CodeListDao;
import com.shenghesun.treasure.system.code.CodeList;

@Service
public class CodeListService {
	
	@Autowired
	CodeListDao codeListDao;
	
	public List<CodeList> findAll(){
		return codeListDao.findAll();
	}
	
	public CodeList findByKey(String key) {
		return codeListDao.findByCodeKey(key);
	}
}
