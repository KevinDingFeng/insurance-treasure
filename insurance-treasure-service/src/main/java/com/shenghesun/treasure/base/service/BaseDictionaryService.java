package com.shenghesun.treasure.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.base.dao.BaseDictionaryDao;
import com.shenghesun.treasure.system.dictionary.BaseDictionary;

@Service
public class BaseDictionaryService {

	@Autowired
	BaseDictionaryDao baseDictionaryDao;
	
	public List<BaseDictionary> findByType(List<String> type) {
		return baseDictionaryDao.findByTypeIn(type);
	}
	public List<BaseDictionary> findByParentCode(String parentCode) {
		return baseDictionaryDao.findByParentCode(parentCode);
	}
	public List<BaseDictionary> findByParentId(String parentId) {
		return baseDictionaryDao.findByParentId(parentId);
	}
}  
