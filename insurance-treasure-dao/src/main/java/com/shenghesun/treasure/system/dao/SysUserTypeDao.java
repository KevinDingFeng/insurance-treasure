package com.shenghesun.treasure.system.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.entity.SysUserType;

@Repository
public interface SysUserTypeDao extends JpaRepository<SysUserType, Long>, JpaSpecificationExecutor<SysUserType>{
	SysUserType findByAccount(String account);
}
