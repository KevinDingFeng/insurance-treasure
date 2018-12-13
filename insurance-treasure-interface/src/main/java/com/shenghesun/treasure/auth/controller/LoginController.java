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
import com.shenghesun.treasure.core.constant.BaseConstant;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.PasswordUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private LoginSuccessService loginSuccessService;

	/**
	 * 	@Title
	 *  @param request
	 *  @param account
	 *  @param password
	 *  @param code
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午2:33:46
	 *  @Description 系统使用接口进行用户登陆
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public JSONObject login(HttpServletRequest request, @Validated String account,
			@Validated String password,String code) {
		try {
			return loginToken(account,password);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
	/**
	 * 	@Title
	 *  @param request
	 *  @param account
	 *  @param password
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午2:34:43
	 *  @Description 提供对外接口进行用户登陆，获取token
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
	 * 	@Title
	 *  @param account
	 *  @param password
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午2:35:20
	 *  @Description 用户登陆
	 *  			1.根据用户名获取用户
	 *  			2.加密登陆密码与数据库中密码比对
	 *  			3.设置登陆成功返回token等信息
	 */
	private JSONObject loginToken(String account,String password) {
		// 获取登录名对应的数据
		SysUser user = sysUserService.findByAccount(account);
		if (user == null || user.getSalt() == null) {
			return JsonUtil.getFailJSONObject(BaseConstant.INPUT_ERROR);
		}
		password = PasswordUtil.encrypt(password, user.getSalt());
		//根据用户名和密码（密文）校验是否登录成功
		if(this.login(user.getPassword(), password)) {
			log.info("login " + account);
			Map<String, Object> returnMap = loginSuccessService.setReturnMessage(user);
			return JsonUtil.getSuccessJSONObject(returnMap);
		}else {
			return JsonUtil.getFailJSONObject(BaseConstant.PASSWORD_ERROR); 
		}
	}
	/**
	 * 判断是否可以登录逻辑，库存密码存在且与输入的现有密码相等，即登录成功
	 * @param passwordSource 库存密码
	 * @param password       现有密码
	 * @return
	 */
	private boolean login(String passwordSource, String password) {
		return passwordSource != null && passwordSource.equals(password);
	}

}
