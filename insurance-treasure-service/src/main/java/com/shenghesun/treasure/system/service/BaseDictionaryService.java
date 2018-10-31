package com.shenghesun.treasure.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.dao.BaseDictionaryDao;
import com.shenghesun.treasure.system.dictionary.BaseDictionary;

@Service
public class BaseDictionaryService {

	@Autowired
	BaseDictionaryDao baseDictionaryDao;
	
	public List<BaseDictionary> findById(String type) {
		return baseDictionaryDao.findByType(type);
	}
	public List<BaseDictionary> findByParentCode(String parentCode) {
		return baseDictionaryDao.findByParentCode(parentCode);
	}
}  
