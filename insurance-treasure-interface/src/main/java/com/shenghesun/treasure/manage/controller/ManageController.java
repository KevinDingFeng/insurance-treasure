package com.shenghesun.treasure.manage.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.auth.support.LoginService;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.TokenUtil;

@RestController
@RequestMapping(value = "/api")
public class ManageController {

	@Autowired
	private LoginService loginService;

	/**
	 * 该接口测试使用，尽量不要加入业务逻辑
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public JSONObject index(HttpServletRequest req) {
		String token = HttpHeaderUtil.getToken((HttpServletRequest) req);
		//两种不同的获取当前登录用户信息的方法
		//1
		String loginAccount = TokenUtil.getLoginUserAccount(token);//登录用户的 用户名
		Long loginUserId = TokenUtil.getLoginUserId(token);//登录用户的 id
		System.out.println("user Id = " + loginUserId + " ; user account = " + loginAccount);
		//2
		Map<String, Object> map = TokenUtil.decode(token);
		String account = TokenUtil.getLoginUserAccount(map);//登录用户的 用户名
		Long userId = TokenUtil.getLoginUserId(map);//登录用户的 id
		System.out.println("user Id = " + userId + " ; user account = " + account);
		//验证用户是否拥有角色和权限的方法
		//判断角色
		String roleName = "二级角色";
		String[] roleNames = {"二级角色", "六级角色", "十级角色"};
		//1 是否拥有某个角色
		System.out.println(loginService.hasRole(token, roleName));
		roleName = "十级角色";
		System.out.println(loginService.hasRole(token, roleName));
		//2 是否拥有多个角色中的某一个
		System.out.println(loginService.hasOneRoles(token, roleNames));
		//3 是否拥有多个角色中的全部角色
		System.out.println(loginService.hasAllRoles(token, roleNames));
		//判断权限
		String perm = "role:view";
		String[] perms = {"role:view", "role:create", "role:update", "role:removed"};
		//4 是否拥有某个权限
		System.out.println(loginService.hasPerms(token, perm));
		perm = "role:removed";
		System.out.println(loginService.hasPerms(token, perm));
		//5 是否拥有多个权限中的某一个
		System.out.println(loginService.hasOnePerms(token, perms));
		//6 是否拥有多个权限中的全部权限
		System.out.println(loginService.hasAllPerms(token, perms));
		
		return JsonUtil.getSuccessJSONObject(account);
	}
	
	/**
	 * 该接口测试使用，尽量不要加入业务逻辑
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public JSONObject updateBalance(HttpServletRequest req) {
		return JsonUtil.getSuccessJSONObject();
	}
}
