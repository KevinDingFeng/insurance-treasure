package com.shenghesun.treasure.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.auth.support.UserService;
import com.shenghesun.treasure.core.constant.BaseConstant;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.PasswordUtil;
import com.shenghesun.treasure.utils.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private UserService userService;
	/**
	 * 	@Title
	 *  @param request
	 *  @param account
	 *  @param code
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午2:32:02
	 *  @Description 修改手机号
	 *  			1.校验验证码
	 *  			2.判断修改的手机号是否已经被注册
	 *  			3.获取登陆用户，更改信息并保存
	 */
	@RequestMapping(value = "/cell", method = RequestMethod.GET)
	public JSONObject changeCell(HttpServletRequest request,String account,String code) {
		try {
			//验证码是否正确
			JSONObject checkSmsCode = userService.checkSmsCode(account, code);
			if(checkSmsCode!=null) {
				return checkSmsCode;
			}
			//验证新修改的手机号是否已经被注册
			if(sysUserService.findByAccount(account)!=null) {
				return JsonUtil.getFailJSONObject(BaseConstant.ACCOUNT_ERROR); 
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
	 * 	@Title
	 *  @param request
	 *  @param old
	 *  @param current
	 *  @param code
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午2:29:39
	 *  @Description 修改密码
	 *  			1.获取用户信息，查找出当前对象
	 *  			2.用户输入原始密码加密后与数据库中密码比对，比对未通过则返回信息
	 *  			3.校验验证码是否正确
	 *  			4.保存新密码
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
			JSONObject checkSmsCode = userService.checkSmsCode(user.getAccount(), code);
			if(checkSmsCode!=null) {
				return checkSmsCode;
			}
			//修改密码，进行保存
			user.setPassword(PasswordUtil.encrypt(current, user.getSalt()));
			sysUserService.save(user);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
		return JsonUtil.getSuccessJSONObject();
	}
	/**
	 * 	@Title
	 *  @param request
	 *  @param old
	 *  @param current
	 *  @param code
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午2:29:18
	 *  @Description 发送邀请码
	 */
	@RequestMapping(value = "/invite", method = RequestMethod.GET)
	public JSONObject invite(HttpServletRequest request,String old,String current,String code) {
		try {
			//获取登陆用户信息
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long userId = TokenUtil.getLoginUserId(token);
			SysUser user = sysUserService.findById(userId);
			return JsonUtil.getSuccessJSONObject(user.getInvitCode());
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
}
