package com.shenghesun.treasure.system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.entity.SysUser;
@Repository
public interface SysUserDao extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

	SysUser findByAccount(String account);

	SysUser findByOpenId(String openId);
	
	@Query(value="select b.* from sys_user a,sys_user b where a.invit_code = b.recommended and a.id=?",nativeQuery=true)
	List<SysUser> findByInviteCode(String userId);
	
	
}
