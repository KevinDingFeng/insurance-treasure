package com.shenghesun.treasure.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.auth.support.UserService;
import com.shenghesun.treasure.core.constant.BaseConstant;
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
	private UserService userService;
	
	@Value("${sms.template.register.code}")
	private String registertemplateCode;
	@Value("${sms.template.update.code}")
	private String modifytemplateCode;
	
	/**
	 * 	@Title
	 *  @param user
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午2:24:18
	 *  @Description 用户注册
	 *  			1.校验验证码是否正确
	 *  			2.根据手机号查找用户，用户存在返回用户存在信息，不存在完善用户信息后进行保存
	 */
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public JSONObject register(@Validated SysUser user,String code) {
		try {
			//验证码是否正确
			JSONObject checkSmsCode = userService.checkSmsCode(user.getCellphone(), code);
			if(checkSmsCode!=null) {
				return checkSmsCode;
			}
			// 获取登录名对应的数据
			SysUser findUser = sysUserService.findByCell(user.getCellphone());
			if (findUser != null) {
				return JsonUtil.getFailJSONObject(BaseConstant.ACCOUNT_ERROR);
			}
			//完善用户信息
			user = userService.regist(user);
			//保存用户
			sysUserService.save(user);
			return JsonUtil.getSuccessJSONObject();
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
	/**
	 * 	@Title
	 *  @param account
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午2:26:03
	 *  @Description 发送注册手机验证码
	 */
	@RequestMapping(value = "/sms", method = RequestMethod.GET)
	public JSONObject sendRegisterCode(String account) {
		return userService.sendCode(account,registertemplateCode);
	}
	
	/**
	 * 	@Title
	 *  @param account
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月12日下午1:57:06
	 *  @Description 发送修改手机号验证码
	 */
	@RequestMapping(value = "/smsModify", method = RequestMethod.GET)
	public JSONObject sendModifyCode(String account) {
		return userService.sendCode(account,modifytemplateCode);
	}

}
