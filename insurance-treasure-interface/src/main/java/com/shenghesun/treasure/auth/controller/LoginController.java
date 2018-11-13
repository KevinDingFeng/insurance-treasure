package com.shenghesun.treasure.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.auth.support.LoginSuccessService;
import com.shenghesun.treasure.config.CustomConfig;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.PasswordUtil;
import com.shenghesun.treasure.utils.RandomUtil;
import com.shenghesun.treasure.utils.RedisUtil;
import com.shenghesun.treasure.utils.SmsCodeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private LoginSuccessService loginSuccessService;
	@Autowired
	private SmsCodeService SmsCodeService;
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 加密算法，接收登录名和明文的密码 如果登录名在数据库存在，则找到对应的盐值，完成对明文密码加密操作，把密文返回
	 * 如果登录名在数据库不存在或明文密码为空，则返回错误信息 不对密码是否正确进行校验，只负责加密
	 * 
	 * @param name
	 * @param password
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/encrypt", method = RequestMethod.POST)
	public JSONObject encrypt(@Validated String account,@Validated String password) {
		// 获取登录名对应的数据
		SysUser user = sysUserService.findByAccount(account);
		if (user == null || user.getSalt() == null) {
			return JsonUtil.getFailJSONObject("用户不存在");
		}
		return JsonUtil.getSuccessJSONObject(PasswordUtil.encrypt(password, user.getSalt()));
	}

	/**
	 * 提交用户信息，进行登录校验 登录成功后跳转到指定的页面 登录失败，返回 传入的密码，目前版本是已经通过 /encrypt
	 * 完成加密的内容，所以后续登录判断中，无需再次加密 9
	 * 
	 * @param user
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public JSONObject login(HttpServletRequest request, @Validated String account,
			@Validated String password,String code) {
		try {
			//判断验证码是否正确
			String smsCode = redisUtil.get(account);
			if(smsCode==null || smsCode!=code) {
				return JsonUtil.getFailJSONObject("验证码错误"); 
			}
			// 获取登录名对应的数据
			SysUser user = sysUserService.findByAccount(account);
			if (user == null || user.getSalt() == null) {
				System.out.println("用户名不存在或用户的盐值不存在");
				return JsonUtil.getFailJSONObject("输入信息有误");
			}
			password = PasswordUtil.encrypt(password, user.getSalt());
//			根据用户名和密码（密文）校验是否登录成功
			if(this.login(user.getPassword(), password)) {
				log.info("login " + account);
				Map<String, Object> returnMap = loginSuccessService.setReturnMessage(user);
				return JsonUtil.getSuccessJSONObject(returnMap);
			}else {
				return JsonUtil.getFailJSONObject("用户名密码错误"); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("特殊错误");
			return JsonUtil.getFailJSONObject("特殊错误");
		}
	}
	
	/**
	 * 判断是否可以登录逻辑，库存密码存在且与输入的现有密码相等，即登录成功
	 * 
	 * @param passwordSource 库存密码
	 * @param password       现有密码
	 * @return
	 */
	private boolean login(String passwordSource, String password) {
		return passwordSource != null && passwordSource.equals(password);
	}
	
	/**
	 * 发送手机验证码
	 */
	@RequestMapping(value = "/sms", method = RequestMethod.GET)
	public JSONObject sendMessage(String account) {
		String code = RandomUtil.randomNum();
		SmsCodeService.sendSmsCode(account, code);
		redisUtil.set(account, code, CustomConfig.SMSCODE_TIME_SECOND);
		return JsonUtil.getSuccessJSONObject(code);
	}
}
