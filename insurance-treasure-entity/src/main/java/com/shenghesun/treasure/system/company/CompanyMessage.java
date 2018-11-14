package com.shenghesun.treasure.system.company;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.treasure.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CompanyMessage extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 368306344206713573L;

	/**
	 * 公司名称
	 */
	@Column(nullable = false, length = 64)
	private String companyName;
	
	/**
	 * 常用联系人姓名
	 */
	@Column(nullable = false, length = 64)
	private String contactName;
	
	/**
	 * 常用联系人电话
	 */
	@Column(nullable = false, length = 64)
	private String contactCell;
	
	/**
	 * 邮箱
	 */
	@Column(nullable = false, length = 64)
	private String email;
	
	/**
	 * 社会统一信用代码
	 */
	@Column(nullable = false, length = 64)
	private String creditCode;
	
	/**
	 * 统一信用凭证，图片上传
	 */
	@Column(nullable = false, length = 64)
	private String creditCard;
	
	/**
	 * 公司账户余额
	 */
	@Column(columnDefinition="DECIMAL(16,2)")
	private Double balance=0d;
	
}
