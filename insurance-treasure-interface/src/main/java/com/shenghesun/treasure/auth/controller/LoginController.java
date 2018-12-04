package com.shenghesun.treasure.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.auth.support.LoginSuccessService;
import com.shenghesun.treasure.config.CustomConfig;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.PasswordUtil;
import com.shenghesun.treasure.utils.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private LoginSuccessService loginSuccessService;
	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 系统用户登陆，先判断验证码是否正确，正确进行登陆
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
			/*String smsCode = redisUtil.get(account);
			if(smsCode==null || smsCode!=code) {
				return JsonUtil.getFailJSONObject("验证码错误"); 
			}*/
			return loginToken(account,password);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
	/**
	 * 获取对外接口token
	 * @return
	 */
	@RequestMapping(value = "/getToken")
	public JSONObject getToken(HttpServletRequest request, @Validated String account,
			@Validated String password) {
		try {
			return loginToken(account,password);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}

	/**
	 * 检测token是否失效
	 */
	@RequestMapping(value = "/checkToken", method = RequestMethod.GET)
	public JSONObject checkToken(HttpServletRequest request, @Validated String token) {
		try {
			if(redisUtil.exists(CustomConfig.REDIS_TOKEN_PREFIX+token)) {
				return JsonUtil.getSuccessJSONObject(true);
			}else {
				return JsonUtil.getSuccessJSONObject(false);
			}
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject();
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
	 * 登陆
	 */
	private JSONObject loginToken(String account,String password) {
		// 获取登录名对应的数据
		SysUser user = sysUserService.findByAccount(account);
		if (user == null || user.getSalt() == null) {
			System.out.println("用户名不存在或用户的盐值不存在");
			return JsonUtil.getFailJSONObject("输入信息有误");
		}
		password = PasswordUtil.encrypt(password, user.getSalt());
		//根据用户名和密码（密文）校验是否登录成功
		if(this.login(user.getPassword(), password)) {
			log.info("login " + account);
			Map<String, Object> returnMap = loginSuccessService.setReturnMessage(user);
			return JsonUtil.getSuccessJSONObject(returnMap);
		}else {
			return JsonUtil.getFailJSONObject("用户名密码错误"); 
		}
	}
}
