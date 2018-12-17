package com.shenghesun.treasure.core.constant;

public class OrderConstant {

	/**
	 * 接口调用来源，系统内部调用
	 */
	public static final String SYS_LOCAL = "local";
	
	/**
	 * 接口调用来源，外部接口调用
	 */
	public static final String SYS_OUT = "out";
	
	/**
	 * 国内
	 */
	public static final String INLAND = "0";
	
	
	/**
	 * 投保返回状态10，成功
	 */
	public static final String APPROVL_SUCCESS = "10";
	
	/**
	 * 投保返回状态7，人工核保
	 */
	public static final String APPROVL_WAIT = "7";
	
	/**
	 * 投保返回状态19,提交失败
	 */
	public static final String APPROVL_FAIL = "19";
	
	/**
	 * 资金消耗
	 */
	public static final String FUND_OUT = "0";
	
	/**
	 * 资金充值
	 */
	public static final String FUND_IN = "1";
	/**
	 * 国内投保险类ClassesType
	 */
	public static final String CLASSESTYPE_IN = "1";
	/**
	 * 国际保险类ClassesType
	 */
	public static final String CLASSESTYPE_OUT = "2";
	/**
	 * 运输代码错误key
	 */
	public static final String TRANS_ERROR = "trans_error";
	/**
	 * 运输代码错误value 
	 */
	public static final String TRANS_MESSAGE = ": 运输代码不存在";
	/**
	 * 货物代码错误key
	 */
	public static final String GOODS_ERROR = "goods_error";
	
	/**
	 * 货物代码错误value
	 */
	public static final String GOODS_MESSAGE = ": 货物代码不存在";
	/**
	 * 包装代码错误key
	 */
	public static final String PACK_ERROR = "pack_error";
	/**
	 * 包装代码错误value
	 */
	public static final String PACK_MESSAGE = ": 包装代码不存在";
	/**
	 * 币种代码错误key
	 */
	public static final String CURRENCY_ERROR = "currency_error";
	/**
	 * 币种代码错误value
	 */
	public static final String CURRENCY_MESSAGE = ": 币种代码不存在";
	
	/**
	 * 费率错误key  RATE_ERROR
	 */
	public static final String RATE_ERROR = "rate_error";

	/**
	 * 费率错误key  RATE_ERROR
	 */
	public static final String RATE_MESSAGE = ": 费率低于最低费率";
	
	/**
	 * 货物价值低于0key
	 */
	public static final String GOODSVALUE_ERROR = "goodsValue_error";
	
	/**
	 * 货物价值低于0value
	 */
	public static final String GOODSVALUE_MESSAGE = ": 货物价值有误";

	/**
	 * 货物价值超出边界key
	 */
	public static final String GOODSVALUE_OUTOFRANGE = "goodsValue_outOfRange";
	
	/**
	 *  货物价值超出边界value
	 */
	public static final String GOODSVALUE_OUTOFRANGE_MESSAGE = ": 货物价值过大，请输入合理范围";
	/**
	 * order
	 */
	public static final String Order = "order";
	
	/**
	 * 币种代码后缀
	 */
	public static final String CURRENCY_SUFFIX = "curr";
	/**
	 * 包装代码后缀
	 */
	public static final String PACKAGE_SUFFIX = "pack";
	/**
	 * 费率最小值
	 */
	public static final float MIN_RATE = 0.02f;
}
