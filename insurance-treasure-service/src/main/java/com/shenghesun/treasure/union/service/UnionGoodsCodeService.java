package com.shenghesun.treasure.union.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.union.code.UnionGoodsCode;
import com.shenghesun.treasure.union.dao.UnionGoodsCodeDao;

@Service
public class UnionGoodsCodeService {
	
	@Autowired
	private UnionGoodsCodeDao unionGoodsCodeDao;
	
	public List<UnionGoodsCode> findAll(){
		return unionGoodsCodeDao.findAll();
	}
}
