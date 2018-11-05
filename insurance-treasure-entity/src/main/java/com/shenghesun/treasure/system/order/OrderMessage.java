package com.shenghesun.treasure.system.order;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.treasure.entity.base.BaseEntity;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//支付相关信息
@Entity
@Table
@Data  
@ToString(exclude = {"sysUser"},callSuper = true)
@EqualsAndHashCode(exclude = {"sysUser"},callSuper = true)
public class OrderMessage extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -4353307437903537087L;

	//投保用户id
	@Column(length=50)
	private Long userid;
	//保单订单号
	@Column(length=100)
	private String orderNo;
	
	//以下信息来源页面
	//业务类型
	@Column(length=100)
	private String BusinessType;
	//货物名称
	@Column(length=100)
	private String goodsName;
	//货物价值
	@Column(length=100)
	private String goodsValue;
	//币种
	@Column(length=100)
	private String currency;
	//包装类型
	@Column(length=100)
	private String packageType;
	//体积重量
	@Column(length=100)
	private String weight;
	//运输方式
	@Column(length=100)
	private String transportMethod;
	//起运地
	@Column(length=100)
	private String firstPlace;
	//目的地
	@Column(length=100)
	private String destination;
	//起运日期
	@Column(length=100)
	private String transportDate;
	//车牌号或运单号
	@Column(length=100)
	private String licenseNumber;
	//投保人
	@Column(length=100)
	private String applicator;
	//被保险人
	@XStreamAlias("APPLYCARTTYPE")
	@Column(length=100)
	private String insured;
	//开票抬头
	@Column(length=100)
	private String invoiceTitle;
	//保险公司
	@Column(length=100)
	private String insuranceCompany;
	//保险条款
	@Column(length=100)
	private String insuranceClause;
	//费率
	@Column(length=100)
	private String rate;
	
	//险类
	private String classestype;
	//投保人名称
	private String applyname;
	//被保人名称
	private String insurantname;
	//险种代码
	private String classtype;
	//运单号、发票号码
	private String mark;
	//包装及数量
	private String quantity;
	//货物名称
	private String item;
	//包装代码
	private String packcode;
	//货物类型
	private String itemcode;
	//运输方式
	private String kind;
	//运输工具名称
	private String kindname;
	//始发地
	private String startport;
	//目的地
	private String endport;
	//主险条款代码
	private String mainitemcode;
	//主险条款内容
	private String itemcontent;
	//币种代码
	private String currencycode;
	//发票金额
	private String invamount;
	//private String rate;
	//保费
	private String preminum;
	//保费币种
	private String fcurrencycode;
	//起保日期
	private String effectdate;
	//航班日期
	private String saildate;
	//免赔条件
	private String franchise;
	//自定义查询码
	private String userno;

	/**
	 * 保单总金额
	 */
	@Column(columnDefinition="DECIMAL(16,2)")
	private Integer orderAmount;

	/**
	 * 保单支付状态 0：下单成功；1：支付成功
	 * 默认为 0
	 */
	@Column
	private Integer payStatus = 0; 
	
	/**
	 * 保单保险状态 0：下保成功；1：保单生效成功
	 * 默认为 0
	 */
	@Column
	private Integer insuranceStatus = 0; 

}
