package com.shenghesun.treasure.union.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.union.UnionPackageCode;
import com.shenghesun.treasure.union.dao.UnionPackageCodeDao;

@Service
public class UnionPackageCodeService {
	
	@Autowired
	private UnionPackageCodeDao unionPackageCodeDao;
	
	public List<UnionPackageCode> findAll(){
		return unionPackageCodeDao.findAll();
	}
}
