package com.shenghesun.treasure.system.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.dao.SysUserDao;
import com.shenghesun.treasure.system.entity.SysUser;

@Service
@Transactional
public class SysUserService {
	
	@Autowired
	private SysUserDao userDao;

	//@Cacheable(cacheNames = "sysUsers", key = "#account")
	public SysUser findByAccount(String account) {
		return userDao.findByAccount(account);
	}

	public SysUser findById(Long id) {
		return userDao.getOne(id);
	}
	
	public SysUser findByCell(String cellPhone) {
		return userDao.findByCellphone(cellPhone);
	}

	public SysUser findByOpenId(String openId) {
		return userDao.findByOpenId(openId);
	}

	//@CachePut(cacheNames = "sysUsers", key = "#entity.cellphone")
	public SysUser save(SysUser entity) {
		return userDao.save(entity);
	}
	
	public List<SysUser> findByInviteCode(String userId) {
		return userDao.findByInviteCode(userId);
	}
	
	
}
