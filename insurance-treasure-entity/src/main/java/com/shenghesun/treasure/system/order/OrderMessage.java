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
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//支付相关信息
@Entity
@Table
@Data  
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@XStreamAlias("DATA")
public class OrderMessage extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -4353307437903537087L;

	//投保用户id
	@XStreamOmitField
	@Column(length=50)
	private Long userid;
	//保单订单号
	@XStreamOmitField
	@Column(length=100)
	private String orderNo;
	
	//资金状态，入账还是出账，0代表出账 1代表入账
	@XStreamOmitField
	@Column
	private String plusOrMinus;
	
	/**
	 * 页面收集字段
	 */
	//业务类型
	@Column(length=100)
	@XStreamOmitField
	private String BusinessType;
	//货物价值
	@Column(length=100)
	@XStreamOmitField
	private String goodsValue;
	//包装类型
	@Column(length=100)
	@XStreamOmitField
	private String packageType;
	//车牌号或运单号
	@Column(length=100)
	@XStreamOmitField
	private String licenseNumber;
	//开票抬头
	@Column(length=100)
	@XStreamOmitField
	private String invoiceTitle;
	//保险公司
	@Column(length=100)
	@XStreamOmitField
	private String insuranceCompany;
	//保险条款
	@Column(length=100)
	@XStreamOmitField
	private String insuranceClause;
	
	
	/**
	 * 太平洋保险接口字段
	 */
	//投保人名称
	@Column(length=200)
	@XStreamAlias("APPLYNAME")
	private String applyname="Alice";
	//被保人名称
	@Column(length=200)
	@XStreamAlias("INSURANTNAME")
	private String insurantname="Alice";
	//费率
	@Column(columnDefinition="DECIMAL(16,2)")
	@XStreamAlias("RATE")
	private Integer rate;
	//始发地
	@Column(length=60)
	@XStreamAlias("STARTPORT")
	private String startport="北京";
	//目的地
	@Column(length=60)
	@XStreamAlias("ENDPORT")
	private String endport="上海";
	//运输方式
	@Column(length=2)
	@XStreamAlias("KIND")
	private String kind="4";
	//起运日期
	@Column(length=10)
	@XStreamAlias("SAILDATE")
	private String saildate="2018-11-08";
	//货物名称
	@Column(length=500)
	@XStreamAlias("ITEM")
	private String item="大豆";
	
	//险类
	@Column(length=1)
	@XStreamAlias("CLASSESTYPE")
	private String classestype="1";

	//险种代码
	@Column(length=8)
	@XStreamAlias("CLASSTYPE")
	private String classtype="11040200";
	//运单号、发票号码
	@Column(length=500)
	@XStreamAlias("MARK")
	private String mark="9630151214002";
	//包装及数量
	@Column(length=500)
	@XStreamAlias("QUANTITY")
	private String quantity="1";

	//包装代码
	@Column(length=2)
	@XStreamAlias("PACKCODE")
	private String packcode="01";
	//货物类型
	@Column(length=4)
	@XStreamAlias("ITEMCODE")
	private String itemcode="1106";

	//运输工具名称
	@Column(length=30)
	@XStreamAlias("KINDNAME")
	private String kindname="卡车";

	//主险条款代码
	@Column(length=12)
	@XStreamAlias("MAINITEMCODE")
	private String mainitemcode="ZH";
	//主险条款内容
	@Column
	@XStreamAlias("ITEMCONTENT")
	private String itemcontent="国内水路、陆路货物运输保险条款综合险";
	//币种代码
	@Column(length=2)
	@XStreamAlias("CURRENCYCODE")
	private String currencycode="01";
	//发票金额
	@Column(columnDefinition="DECIMAL(16,2)")
	@XStreamAlias("INVAMOUNT")
	private Integer invamount=100;
	//保费
	@Column(columnDefinition="DECIMAL(16,2)")
	@XStreamAlias("PREMIUM")
	private Integer preminum=1000;
	//保费币种
	@Column(length=50)
	@XStreamAlias("FCURRENCYCODE")
	private String fcurrencycode="01";
	//起保日期
	@Column(length=10)
	@XStreamAlias("EFFECTDATE")
	private String effectdate="2018-11-08";

	//免赔条件
	@Column(length=500)
	@XStreamAlias("FRANCHISE")
	private String franchise="other terms  conditions are equalent to the updated Open Policy.";
	//自定义查询码
	@Column(length=50)
	@XStreamAlias("USERNO")
	private String userno="58965412563";

	/**
	 * 保单总金额
	 */
	@Column(columnDefinition="DECIMAL(16,2)")
	@XStreamOmitField
	private Integer orderAmount;

	/**
	 * 保单支付状态 0：下单成功；1：支付成功
	 * 默认为 0
	 */
	@Column
	@XStreamOmitField
	private Integer payStatus = 0; 
	
	/**
	 * 保单保险状态 0：下保成功；1：保单生效成功
	 * 默认为 0
	 */
	@Column
	@XStreamOmitField
	private Integer insuranceStatus = 0; 

}
