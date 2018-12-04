
package com.shenghesun.treasure.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.base.dao.BaseCityDao;
import com.shenghesun.treasure.system.dictionary.BaseCity;

@Service
public class BaseCityService {

	@Autowired
	private BaseCityDao baseCityDao;
	
	public List<BaseCity> find(){
		return baseCityDao.findAll();
	}
	
	public List<BaseCity> findLike(String city){
		return baseCityDao.findByNameLike("%"+city+"%");
	}
}
