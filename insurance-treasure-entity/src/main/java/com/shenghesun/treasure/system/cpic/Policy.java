package com.shenghesun.treasure.system.cpic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.treasure.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 核保结果查询
 * @author EDZ
 */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class Policy extends BaseEntity{
	
	/**
	 * 分公司代码
	 */
	@Column(length=50)
	private String unitcode;
	/**
	 * 投保单号
	 */
	@Column(length=50)
	private String applyno;

	/**
	 * 保单号
	 */
	@Column(length=50)
	private String policyno;
	/**
	 * 处理结果代码
	 */
	@Column(length=10)
	private String resultCode;
	/**
	 * 电子保单状态
	 */
	@Column(length=25)
	private String epolicyStatus;
	/**
	 * 电子保单内容
	 */

	@Column(columnDefinition="MEDIUMTEXT")
	private String epolicyFile;
	
	/**
	 * 保单状态
	 * 7  待审核-----投保单录入成功，提交人工审核
		36 待签发-----投保单录入成功，自动核保通过 （非在线支付情况，状态 10 --保单生效）
		19 提交失败----投保单录入成功，系统提交核保失败，需联系技术人员处理
	 * 
	 */
	@Column(length=5)
	private String status;
}
