package com.shenghesun.treasure.system.dto;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;
@Data
public class OrderDto{

	//城市类型，国际还是国内
	@NotBlank(message = "{order.city.notBlank}")
	private String city;
	//货物代码
	private String goodsCode;
	//货物名称
	private String firstGoodsName;
	//货物价值
	private String goodsValue;
	//币种代码
	@NotBlank(message = "{order.currencyCode.notBlank}")
	private String currencyCode;
	//加成比例
	private String incrate;
	//包装代码
	@NotBlank(message = "{order.packCode.notBlank}")
	private String packCode;
	//运输方式代码
	@NotBlank(message = "{order.transCode.notBlank}")
	private String transCode;
	//始发地
	@NotBlank(message = "{order.startPort.notBlank}")
	private String startPort;
	//中转地
	private String transPort;
	//目的地
	@NotBlank(message = "{order.endPort.notBlank}")
	private String endPort;
	//起运日期
	@NotBlank(message = "{order.saildate.notBlank}")
	private String saildate;
	//运单号、发票号码
	@NotBlank(message = "{order.mark.notBlank}")
	private String mark;
	//投保人名称
	@NotBlank(message = "{order.applyName.notBlank}")
	private String applyName;
	//被保人名称
	@NotBlank(message = "{order.insurantName.notBlank}")
	private String insurantName;
	//费率
	@NotBlank(message = "{order.rate.notBlank}")
	private String rate;
}
