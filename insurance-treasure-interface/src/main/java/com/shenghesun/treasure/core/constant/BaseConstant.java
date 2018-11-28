package com.shenghesun.treasure.core.constant;

public class BaseConstant {

	/**
	 * 接口调用来源，系统内部调用
	 */
	public static final String SYS_LOCAL = "local";
	
	/**
	 * 接口调用来源，外部接口调用
	 */
	public static final String SYS_OUT = "out";
	
	/**
	 * 验证码错误
	 */
	public static final String CODE_ERROR = "验证码错误";
	
	/**
	 * 原始密码错误
	 */
	public static final String OLD_PASSWORD_ERROR = "原密码错误";
	
	/**
	 * 手机号已经被注册
	 */
	public static final String ACCOUNT_ERROR = "手机号已被注册，请更换手机号";
	
	/**
	 * 国内
	 */
	public static final String INLAND = "0";
	
	/**
	 * 阿里返回手机号发送信息超出数量限制标识
	 */
	public static final String ACCPUNT_LIMIT_CODE = "isv.BUSINESS_LIMIT_CONTROL";
	
	/**
	 * 短信数量超限制错误
	 */
	public static final String ACCPUNT_LIMIT_CONTENT = "发送短信数量超出限制！";
	
}
