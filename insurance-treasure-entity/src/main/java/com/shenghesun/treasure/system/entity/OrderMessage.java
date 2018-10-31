package com.shenghesun.treasure.system.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shenghesun.treasure.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//支付相关信息
@Entity
@Table
@Data  
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrderMessage extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -4353307437903537087L;
	
	@Column(length=50)
	private String openid;
	
	@Column(length=100)
	private String orderNo;
	
	/**
	 * 保单总金额
	 */
	@Column(columnDefinition="DECIMAL(16,2)")
	private Integer orderAmount;

	/**
	 * 保单状态 0：下单成功；1：支付成功
	 * 默认为 0
	 */
	@Column
	private Integer payStatus = 0; 

}
