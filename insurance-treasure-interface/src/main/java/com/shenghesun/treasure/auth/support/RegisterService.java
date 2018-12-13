package com.shenghesun.treasure.auth.support;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.entity.SysRole;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysRoleService;
import com.shenghesun.treasure.utils.PasswordUtil;
import com.shenghesun.treasure.utils.RandomCodeUtil;
import com.shenghesun.treasure.utils.RandomUtil;

@Service
public class RegisterService {
	
	@Autowired
	SysRoleService sysRoleService;
	/**
	 * 	@Title
	 *  @param user
	 *  @return SysUser
	 *  @author zdd
	 *	@date 2018年12月13日下午2:25:40
	 *  @Description 为注册用户完善基础信息
	 */
	public SysUser regist(SysUser user) {
		//生成用户邀请码
		user.setInvitCode(RandomCodeUtil.randomString(4));
		//生成密码盐
		String salt = RandomUtil.randomString(16);
		user.setSalt(salt);
		//生成加密密码
		String password = PasswordUtil.encrypt(user.getPassword(), user.getSalt());
		user.setPassword(password);
		//设置默认角色
		Set<SysRole> sysRole = sysRoleService.findByLevel(1);
		user.setRoles(sysRole);
		//设置用户名称
		user.setAccount(user.getCellphone());
		return user;
	}
}
