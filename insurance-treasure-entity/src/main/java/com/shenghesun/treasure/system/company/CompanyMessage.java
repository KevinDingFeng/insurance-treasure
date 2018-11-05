package com.shenghesun.treasure.system.company;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.shenghesun.treasure.entity.base.BaseEntity;
import com.shenghesun.treasure.system.entity.SysUser;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table
@Data
@ToString(callSuper = true, exclude = { "sysUser"})
@EqualsAndHashCode(callSuper = true, exclude = { "sysUser"})
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
	private Integer balance;
	
	/**
	 * 公司账户下子用户
	 */
/*	@OneToMany(mappedBy = "companyMessage",cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	@JSONField(serialize = false)
	private Set<SysUser> sysUser;*/

	/**
	 * 用户余额明细
	 */
	/*@OneToMany(mappedBy = "companyMessage",cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private List<FundDetails> fundDetails;
	*/
}
