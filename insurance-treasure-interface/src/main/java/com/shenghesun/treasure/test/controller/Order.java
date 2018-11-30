package com.shenghesun.treasure.test.controller;

import javax.persistence.Column;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class Order {

	private String businessType;
	//一级货物名称
	private String firstGoodsName;
	//二级货物名称
	private String secondGoodsName;
	//货物价值
	private String goodsValue;
	//一级运输方式
	private String firstTransName;
	//二级运输方式
	private String secondTransName;
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getFirstGoodsName() {
		return firstGoodsName;
	}
	public void setFirstGoodsName(String firstGoodsName) {
		this.firstGoodsName = firstGoodsName;
	}
	public String getSecondGoodsName() {
		return secondGoodsName;
	}
	public void setSecondGoodsName(String secondGoodsName) {
		this.secondGoodsName = secondGoodsName;
	}
	public String getGoodsValue() {
		return goodsValue;
	}
	public void setGoodsValue(String goodsValue) {
		this.goodsValue = goodsValue;
	}
	public String getFirstTransName() {
		return firstTransName;
	}
	public void setFirstTransName(String firstTransName) {
		this.firstTransName = firstTransName;
	}
	public String getSecondTransName() {
		return secondTransName;
	}
	public void setSecondTransName(String secondTransName) {
		this.secondTransName = secondTransName;
	}
	
	
}
