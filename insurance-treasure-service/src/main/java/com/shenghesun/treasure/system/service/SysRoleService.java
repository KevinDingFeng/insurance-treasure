package com.shenghesun.treasure.system.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.dao.SysRoleDao;
import com.shenghesun.treasure.system.entity.SysRole;

@Service
public class SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;
	
	public Set<SysRole> findByLevel(int id) {
		return sysRoleDao.findByLevel(id);
	}
}
