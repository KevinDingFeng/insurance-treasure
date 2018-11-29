package com.shenghesun.treasure.union.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.union.UnionTransCode;
import com.shenghesun.treasure.union.dao.UnionTransCodeDao;

@Service
public class UnionTransCodeService {

	@Autowired
	private UnionTransCodeDao unionTransCodeDao;
	
	public List<UnionTransCode> findAll(){
		return unionTransCodeDao.findAll();
	}
}
