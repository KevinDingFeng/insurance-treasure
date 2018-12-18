package com.shenghesun.treasure.auth.support;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.config.CustomConfig;
import com.shenghesun.treasure.core.constant.BaseConstant;
import com.shenghesun.treasure.system.entity.SysRole;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysRoleService;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.PasswordUtil;
import com.shenghesun.treasure.utils.RandomCodeUtil;
import com.shenghesun.treasure.utils.RandomUtil;
import com.shenghesun.treasure.utils.RedisUtil;
import com.shenghesun.treasure.utils.SmsCodeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	SysRoleService sysRoleService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private SmsCodeService smsCodeService;
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
	
	/**
	 * 	@Title
	 *  @param account
	 *  @param template
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月17日下午3:40:07
	 *  @Description 发送验证码方法
	 */
	public JSONObject sendCode(String account,String template) {
		try {
			//生成随机验证码，并存储至redis中
			String code = RandomUtil.randomNum();
			redisUtil.set(BaseConstant.CODE+account, code, CustomConfig.SMSCODE_TIME_SECOND);
			//发送短信
			String sendSmsCode = smsCodeService.sendSms(account,"物流保宝",template,"{\"code\":\""+code+"\"}");
			//判断是否有超出手机短信发送条数限制
			if(BaseConstant.ACCPUNT_LIMIT_CODE.equals(sendSmsCode)) {
				return JsonUtil.getFailJSONObject(BaseConstant.ACCPUNT_LIMIT_CONTENT);
			}
			return JsonUtil.getSuccessJSONObject(code);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
	/**
	 * 	@Title
	 *  @param account
	 *  @param code
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月18日上午10:42:57
	 *  @Description 校验验证码与redis中存储验证码是否一致
	 *  			1.根据手机号获取存储至redis中的验证码，如果查找结果为空，则输入手机号与发送短信手机号不一致，进行返回
	 *  			2.查找到
	 */
	public JSONObject checkSmsCode(String account,String code) {
		String smscode = redisUtil.getString(BaseConstant.CODE+account);
		if(smscode==null ||!code.equals(smscode)) {
			return JsonUtil.getFailJSONObject(BaseConstant.CODE_ERROR); 
		}
		return null;
	}
}
