package com.shenghesun.treasure.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.auth.support.RegisterService;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RegisterController {

	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private RegisterService registerService;
	/**
	 * 用户注册
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public JSONObject register(@Validated SysUser user) {
		try {
			// 获取登录名对应的数据
			SysUser findUser = sysUserService.findByCell(user.getCellphone());
			if (findUser != null) {
				return JsonUtil.getFailJSONObject("该用户已存在");
			}
			user = registerService.regist(user);
			//保存用户
			sysUserService.save(user);
			return JsonUtil.getSuccessJSONObject();
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
}
