package com.shenghesun.treasure.auth.support;

import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.utils.PasswordUtil;
import com.shenghesun.treasure.utils.RandomCodeUtil;
import com.shenghesun.treasure.utils.RandomUtil;

@Service
public class RegisterService {

	/**
	 * 为注册用户完善基础信息
	 * @param user
	 * @return
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
		return user;
	}
}
