package com.shenghesun.treasure.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.auth.support.RegisterService;
import com.shenghesun.treasure.config.CustomConfig;
import com.shenghesun.treasure.core.constant.BaseConstant;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.RandomUtil;
import com.shenghesun.treasure.utils.RedisUtil;
import com.shenghesun.treasure.utils.SmsCodeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RegisterController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SmsCodeService SmsCodeService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private RegisterService registerService;
	@Value("${sms.template.code}")
	private String templateCode;
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
				return JsonUtil.getFailJSONObject(BaseConstant.ACCOUNT_ERROR);
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
	/**
	 * 发送手机验证码
	 */
	@RequestMapping(value = "/sms", method = RequestMethod.GET)
	public JSONObject sendMessage(String account) {
		String code=null;
		try {
			code = RandomUtil.randomNum();
			redisUtil.set(account, code, CustomConfig.SMSCODE_TIME_SECOND);
			//正式时用
			//String sendSmsCode = SmsCodeService.sendSms(account,"伟林易航",templateCode,"{\"code\":\""+code+"\"}");
			//临时发送短信
			String sendSmsCode = SmsCodeService.sendSmsCode(account, code);
			if(BaseConstant.ACCPUNT_LIMIT_CODE.equals(sendSmsCode)) {
				return JsonUtil.getFailJSONObject(BaseConstant.ACCPUNT_LIMIT_CONTENT);
			}
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
		}
		return JsonUtil.getSuccessJSONObject(code);
	}
}
