package com.shenghesun.treasure.system.company;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

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
	@NotBlank(message = "{company.companyName}")
	@Column(nullable = false, length = 64)
	private String companyName;
	
	/**
	 * 常用联系人姓名
	 */
	@NotBlank(message = "{company.contactName}")
	@Column(nullable = false, length = 64)
	private String contactName;
	
	/**
	 * 常用联系人电话
	 */
	@NotBlank(message = "{company.contactCell}")
	@Column(nullable = false, length = 64)
	private String contactCell;
	
	/**
	 * 邮箱
	 */
	@NotBlank(message = "{company.email}")
	@Column(nullable = false, length = 64)
	private String email;
	
	/**
	 * 社会统一信用代码
	 */
	@NotBlank(message = "{company.creditCode}")
	@Column(nullable = false, length = 64)
	private String creditCode;
	
	/**
	 * 统一信用凭证，图片上传
	 */
	@Column(nullable = false, length = 500)
	private String creditCard;
	
	/**
	 * 公司账户余额
	 */
	@Column(columnDefinition="DECIMAL(16,2)")
	private Double balance=0d;
	
}
