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
	 *  			  根据手机号查找用户，用户存在返回用户存在信息，不存在完善用户信息后进行保存
	 */
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public JSONObject register(@Validated SysUser user) {
		try {
			// 获取登录名对应的数据
			SysUser findUser = sysUserService.findByCell(user.getCellphone());
			if (findUser != null) {
				return JsonUtil.getFailJSONObject(BaseConstant.ACCOUNT_ERROR);
			}
			//完善用户信息
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
	 * 	@Title
	 *  @param account
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午2:26:03
	 *  @Description 发送注册手机验证码
	 */
	@RequestMapping(value = "/sms", method = RequestMethod.GET)
	public JSONObject sendCode(String account) {
		String code=null;
		try {
			//生成随机验证码，并存储至redis中
			code = RandomUtil.randomNum();
			redisUtil.setString(account, code, CustomConfig.SMSCODE_TIME_SECOND);
			//发送短信
			String sendSmsCode = SmsCodeService.sendSms(account,"物流保宝",registertemplateCode,"{\"code\":\""+code+"\"}");
			//判断是否有超出手机短信发送条数限制
			if(BaseConstant.ACCPUNT_LIMIT_CODE.equals(sendSmsCode)) {
				return JsonUtil.getFailJSONObject(BaseConstant.ACCPUNT_LIMIT_CONTENT);
			}
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
		}
		return JsonUtil.getSuccessJSONObject(code);
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
		String code=null;
		try {
			code = RandomUtil.randomNum();
			redisUtil.set(account, code, CustomConfig.SMSCODE_TIME_SECOND);
			String sendSmsCode = SmsCodeService.sendSms(account,"物流保宝",modifytemplateCode,"{\"code\":\""+code+"\"}");
			if(BaseConstant.ACCPUNT_LIMIT_CODE.equals(sendSmsCode)) {
				return JsonUtil.getFailJSONObject(BaseConstant.ACCPUNT_LIMIT_CONTENT);
			}
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
		}
		return JsonUtil.getSuccessJSONObject(code);
	}
}
