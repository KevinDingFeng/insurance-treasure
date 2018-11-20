package com.shenghesun.treasure.order.model;

import java.sql.Date;

import lombok.Data;

/**
  * @ClassName: OrderCondition 
  * @Description: 查询条件
  * @author: yangzp
  * @date: 2018年11月20日 下午4:02:21  
  */
@Data
public class OrderCondition {
	
	//投保用户id
	private Long userId;
	/**
	 * 保单支付状态 0：下单成功；1：支付成功
	 */
	private Integer payStatus;
	/**
	 * 开始时间
	 */
	private Date beginDate;
	/**
	 * 结束时间
	 */
	private Date endDate;
	
	/**
	 * 关键字查询
	 */
	private String keyword;
}
