package com.shenghesun.treasure.system.cpic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.treasure.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
  * 货运险承保接口应答报文
  * @ClassName: Approvl 
  * @Description: TODO
  * @author: yangzp
  * @date: 2018年10月10日 下午5:02:04  
  */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class Approvl extends BaseEntity{
	
	/**
	 * 订单号
	 */
	@Column(length=100)
	private String orderNo;
	
	@Column(length=45)
	private String applyId;
	
	@Column(length=10)
	private String type;
	
	@Column(length=5)
	private String workType;
	
	@Column(length=10)
	@JsonIgnore
	private String unitCode;
	
	@Column(length=25)
	private String applyNo;
	
	@Column(length=25)
	private String policyNo;
	
	/**
	 * 保单状态
	 * 7  待审核-----投保单录入成功，提交人工审核
		36 待签发-----投保单录入成功，自动核保通过 （非在线支付情况，状态 10 --保单生效）
		19 提交失败----投保单录入成功，系统提交核保失败，需联系技术人员处理
	 * 
	 */
	@Column(length=5)
	private Integer status;
	
	@Column
	private String comments;

	/**
	 * 电子保单状态
	 * 0 创建失败 1 创建成功 2 未创建 
	 */
	@Column(length=2)
	private String statusEpolicy;
	
	/**
	 * 电子保单内容
	 */
	@Column(columnDefinition="MEDIUMTEXT")
	private String fileEpolicy;
}
