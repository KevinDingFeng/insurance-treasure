package com.shenghesun.treasure.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.auth.support.RegisterService;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.PasswordUtil;
import com.shenghesun.treasure.utils.RandomCodeUtil;
import com.shenghesun.treasure.utils.RandomUtil;

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
	@RequestMapping(value = "/register")
	public JSONObject register(HttpServletRequest request, SysUser user) {
		try {
			// 获取登录名对应的数据
			SysUser findUser = sysUserService.findByAccount(user.getAccount());
			if (findUser != null) {
				System.out.println("注册用户名已经存在");
				return JsonUtil.getFailJSONObject("用户名存在，请更换用户名");
			}
			user = registerService.regist(user);
			//保存用户
			sysUserService.save(user);
			return JsonUtil.getSuccessJSONObject();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("特殊错误");
			return JsonUtil.getFailJSONObject("特殊错误");
		}
	}
}
