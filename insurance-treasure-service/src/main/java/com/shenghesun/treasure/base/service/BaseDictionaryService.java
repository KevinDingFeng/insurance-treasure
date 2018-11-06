package com.shenghesun.treasure.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.base.dao.BaseDictionaryDao;
import com.shenghesun.treasure.system.dictionary.BaseDictionary;

@Service
public class BaseDictionaryService {
	
	@Autowired
	private BaseDictionaryDao baseDictionaryDao;
	
	public List<BaseDictionary> find(){
		return baseDictionaryDao.findAll();
	}
	
	public List<BaseDictionary> findByType(String type){
		return baseDictionaryDao.findByType(type);
	}
	
	public List<BaseDictionary> findByCountryAndType(String country,String type){
		return baseDictionaryDao.findByCountryAndType(country, type);
	}
}
