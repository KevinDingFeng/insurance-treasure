package com.shenghesun.treasure.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.base.dao.BaseTransportDao;
import com.shenghesun.treasure.system.dictionary.BaseTransport;

@Service
public class BaseTransportService {
	@Autowired
	private BaseTransportDao transportDao;
	
	public List<BaseTransport> findByBusinessCodeAndParentCode(String business,String parentCode){
		return transportDao.findByBusinessCodeAndParentCode(business, parentCode);
	}
	public List<BaseTransport> findByBusinessAndParentCodeAndCountry(String business,String parentCode,String city){
		return transportDao.findByBusinessCodeAndParentCodeAndCity(business, parentCode,city);
	}
	public List<BaseTransport> findByCode(String code,String city){
		return transportDao.findByCodeAndCity(code,city);
	}
	
}
