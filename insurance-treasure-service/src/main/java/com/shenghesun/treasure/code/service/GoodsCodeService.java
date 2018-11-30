package com.shenghesun.treasure.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.code.dao.GoodsCodeDao;
import com.shenghesun.treasure.system.code.GoodsCode;

@Service
public class GoodsCodeService {

	@Autowired
	private GoodsCodeDao goodsCodeDao;
	
	public List<GoodsCode> find(){
		return goodsCodeDao.findAll();
	}
	public GoodsCode findByGoodsCode(String goodsCode) {
		return goodsCodeDao.findByGoodsCode(goodsCode);
	}
}
