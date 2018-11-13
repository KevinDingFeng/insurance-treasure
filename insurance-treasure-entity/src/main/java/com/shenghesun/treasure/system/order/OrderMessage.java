package com.shenghesun.treasure.system.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.treasure.entity.base.BaseEntity;
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
	private Long userId;
	//投保用户公司id
	@XStreamOmitField
	@Column(length=50)
	private Long companyId;
	//保单订单号
	//自定义查询码
	@Column(length=50)
	@XStreamAlias("USERNO")
	private String orderNo;

	//资金状态，入账还是出账，0代表出账 1代表入账
	@XStreamOmitField
	@Column
	private String plusOrMinus;

	//城市类型，国际还是国内
	@XStreamOmitField
	@Column
	private String city;
	/**
	 * 页面收集字段
	 */
	//业务类型
	@Column(length=100)
	@XStreamOmitField
	private String businessType;
	//一级货物名称
	@Column(length=100)
	@XStreamOmitField
	private String firstGoodsName;
	//二级货物名称
	@Column(length=100)
	@XStreamOmitField
	private String secondGoodsName;
	//货物价值
	@Column(columnDefinition="DECIMAL(16,2)")
	@XStreamOmitField
	private Integer goodsValue;
	//币种名称
	@Column(length=10)
	@XStreamOmitField
	private String currencyName;
	//包装类型
	@Column(length=100)
	@XStreamOmitField
	private String packageType;
	//一级运输方式
	@Column(length=100)
	@XStreamOmitField
	private String firstTransName;
	//二级运输方式
	@Column(length=100)
	@XStreamOmitField
	private String secondTransName;
	//开票抬头
	@Column(length=100)
	@XStreamOmitField
	private String invoiceTitle;
	//保险公司
	@Column(length=100)
	@XStreamOmitField
	private String insuranceCompany;
	//运输方式代码
	@Column(length=10)
	@XStreamOmitField
	private String transCode;
	//货物代码
	@Column(length=10)
	@XStreamOmitField
	private String goodsCode;
	/**
	 * 太平洋保险接口字段
	 */
	//投保人名称
	@Column(length=200)
	@XStreamAlias("APPLYNAME")
	private String applyName;
	//被保人名称
	@Column(length=200)
	@XStreamAlias("INSURANTNAME")
	private String insurantName;
	//费率
	@Column(columnDefinition="DECIMAL(16,2)")
	@XStreamAlias("RATE")
	private String rate;
	//始发地
	@Column(length=60)
	@XStreamAlias("STARTPORT")
	private String startPort;
	//中转地
	@Column(length=60)
	@XStreamAlias("TRANSPORT1")
	private String transPort;
	
	//目的地
	@Column(length=60)
	@XStreamAlias("ENDPORT")
	private String endPort;
	//运输方式
	@Column(length=2)
	@XStreamAlias("KIND")
	private String kind;
	//起运日期
	@Column(length=10)
	@XStreamAlias("SAILDATE")
	private String saildate;
	//货物名称
	@Column(length=500)
	@XStreamAlias("ITEM")
	private String item;


	//运单号、发票号码
	@Column(length=500)
	@XStreamAlias("MARK")
	private String mark;
	//包装及数量
	@Column(length=500)
	@XStreamAlias("QUANTITY")
	private String quantity="1";

	//包装代码
	@Column(length=2)
	@XStreamAlias("PACKCODE")
	private String packCode;
	//货物类型
	@Column(length=4)
	@XStreamAlias("ITEMCODE")
	private String itemCode;

	//运输工具名称
	@Column(length=30)
	@XStreamAlias("KINDNAME")
	private String kindName;

	//主险条款代码
	@Column(length=12)
	@XStreamAlias("MAINITEMCODE")
	private String mainItemCode;
	//主险条款内容
	@Column
	@XStreamAlias("ITEMCONTENT")
	private String itemContent;
	//币种代码
	@Column(length=2)
	@XStreamAlias("CURRENCYCODE")
	private String currencyCode;

	//保费
	@Column(columnDefinition="DECIMAL(16,2)")
	@XStreamAlias("PREMIUM")
	private Integer preminum=10;
	//加成比例
	@Column(columnDefinition="DECIMAL(16,2)")
	@XStreamAlias("INCRATE")
	private Integer incrate;
	//保费币种
	@Column(length=50)
	@XStreamAlias("FCURRENCYCODE")
	private String fcurrencyCode="01";
	//起保日期
	@Column(length=10)
	@XStreamAlias("EFFECTDATE")
	private String effectDate;

	//免赔条件
	@Column(length=500)
	@XStreamAlias("FRANCHISE")
	private String franchise="other terms  conditions are equalent to the updated Open Policy.";
	/**
	 * 保单总金额
	 */
	@Column(columnDefinition="DECIMAL(16,2)")
	@XStreamAlias("AMOUNT")
	private Integer orderAmount;
	//发票金额
	@Column(columnDefinition="DECIMAL(16,2)")
	@XStreamAlias("INVAMOUNT")
	private Integer invamount;
	//险类
	@Column(length=1)
	@XStreamAlias("CLASSESTYPE")
	private String classesType;

	//险种代码
	@Column(length=8)
	@XStreamAlias("CLASSTYPE")
	private String classType;

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
