package com.shenghesun.treasure.system.model;

import com.shenghesun.treasure.system.order.OrderMessage;

import lombok.Data;

@Data
public class OrderShow extends OrderMessage{
	
	private static final long serialVersionUID = -4291520311208007068L;
	
	/**
	 * 币种代码对应名称
	 */
	private String currencyName;
	/**
	 * 包装代码对应名称
	 */
	private String packageType;
	
}
