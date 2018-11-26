package com.shenghesun.treasure.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.core.constant.BaseConstant;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.PasswordUtil;
import com.shenghesun.treasure.utils.RedisUtil;
import com.shenghesun.treasure.utils.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private SysUserService sysUserService;

	/**
	 * 修改手机号
	 */
	@RequestMapping(value = "/cell", method = RequestMethod.GET)
	public JSONObject changeCell(HttpServletRequest request,String account,String code) {
		try {
			//验证码是否正确
			String smsCode = redisUtil.get(account);
			if(smsCode==null || smsCode!=code) {
				return JsonUtil.getFailJSONObject(BaseConstant.CODE_ERROR); 
			}
			//获取登陆用户信息
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long userId = TokenUtil.getLoginUserId(token);
			SysUser user = sysUserService.findById(userId);
			user.setAccount(account);
			user.setCellphone(account);
			sysUserService.save(user);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
		return JsonUtil.getSuccessJSONObject();
	}
	
	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public JSONObject changePassword(HttpServletRequest request,String old,String current,String code) {
		try {
			//获取登陆用户信息
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long userId = TokenUtil.getLoginUserId(token);
			SysUser user = sysUserService.findById(userId);
			//获取用户原始密码
			if(!PasswordUtil.encrypt(old, user.getSalt()).equals(user.getPassword())) {
				return JsonUtil.getFailJSONObject(BaseConstant.OLD_PASSWORD_ERROR);
			}
			//验证码判断
			//验证码是否正确
			String smsCode = (String) redisUtil.getObj(user.getCellphone());
			String sms = smsCode.replaceAll("\"", "");
			if(sms==null || !sms.equals(code)) {
				return JsonUtil.getFailJSONObject(BaseConstant.CODE_ERROR); 
			}
			user.setPassword(PasswordUtil.encrypt(current, user.getSalt()));
			sysUserService.save(user);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
		}
		return JsonUtil.getSuccessJSONObject();
	}
}
