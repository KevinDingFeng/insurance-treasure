package com.shenghesun.treasure.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.dao.SysUserTypeDao;
import com.shenghesun.treasure.system.entity.SysUserType;

@Service
public class SysUserTypeService {
	@Autowired
	SysUserTypeDao sysUserTypeDao;
	
	public List<SysUserType> findAll(){
		return sysUserTypeDao.findAll();
	}
	public SysUserType findByAccount(String account){
		return sysUserTypeDao.findByAccount(account);
	}
}
