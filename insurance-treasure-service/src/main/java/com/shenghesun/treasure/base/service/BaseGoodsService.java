package com.shenghesun.treasure.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.base.dao.BaseGoodsDao;
import com.shenghesun.treasure.system.dictionary.BaseGoods;

@Service
public class BaseGoodsService {

	@Autowired
	BaseGoodsDao baseDictionaryDao;
	
	public List<BaseGoods> findByType(List<String> type) {
		return baseDictionaryDao.findByTypeIn(type);
	}
	public List<BaseGoods> findByParentCode(String parentCode) {
		return baseDictionaryDao.findByParentCode(parentCode);
	}
	public List<BaseGoods> findByParentId(String parentId) {
		return baseDictionaryDao.findByParentId(parentId);
	}
	public List<BaseGoods> findByParentCodeAndParentId(String code,String id) {
		return baseDictionaryDao.findByParentCodeAndParentId(code, id);
	}
}  
