package com.shenghesun.treasure.system.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.treasure.entity.base.BaseEntity;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.order.FundDetails;
import com.shenghesun.treasure.system.order.OrderMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 系统用户，平台用户
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true, exclude = { "sysPool" })
@EqualsAndHashCode(callSuper = true, exclude = { "sysPool" })
public class SysUser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -1587698475114512822L;

	/**
	 * 登录名 用户名
	 */
	@Column(nullable = false, length = 64)
	private String account;

	/**
	 * 是否激活
	 */
	private boolean active = true;

	/**
	 * 是否审核通过
	 */
	private boolean audited = true;

	/**
	 * 联系方式
	 */
	@Column(nullable = true, length = 32)
	private String cellphone;
	/**
	 * 联系方式是否通过了校验
	 */
	private boolean cellphoneVerified = false;

	/**
	 * 邮箱
	 */
	@Column(nullable = true, length = 255)
	private String email;
	/**
	 * 邮箱是否通过了校验
	 */
	private boolean emailVerified = false;

	/**
	 * 名称
	 */
/*	@Column(length = 64)
	private String name;*/
	/**
	 * 密码，加密加盐后的结果
	 */
	@JsonIgnore
	@Column(nullable = false, length = 200)
	private String password;

	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "sys_pool_id", nullable = false)
	private SysPool sysPool;
	
	/**
	 * 池 id
	 */
	@Column(name = "sys_pool_id", insertable = false, updatable = false, nullable = false)
	private Long sysPoolId;

	/**
	 * 是否已删除
	 */
	private boolean removed = false;
	/**
	 * 盐值
	 */
	@JsonIgnore
	@Column(nullable = false, length = 20)
	private String salt;

	/**
	 * 系统标志
	 */
	@Column(nullable = true, length = 64)
	private String sysId;

	/**
	 * 微信用户唯一的 open id
	 */
	@Column(nullable = true, length = 255)
	private String openId;
	
	/**
	 * 等级，用户可以看到同等级及以下等级的角色
	 * 	一版情况，等级越高，看到的权限范围越大
	 */
	private int level;
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },fetch = FetchType.EAGER)
	@JoinTable(name = "sys_user_role", inverseJoinColumns = { @JoinColumn(name = "role_id") }, joinColumns = {
			@JoinColumn(name = "user_id") })
	private Set<SysRole> roles;
	
	/**
	 * 用户生成的推荐邀请码,注册时每个用户都生成唯一注册码
	 */
	@Column(nullable = true, length = 5)
	private String invitCode;
	
	/**
	 * 用户被谁推荐,注册的时候有邀请码时更新该字段
	 */
	@Column(nullable = true, length = 5)
	private String recommended;
	
	
	
	/**
	 * 用户所属公司
	 */
	@ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.MERGE)
	@JoinColumn(name = "company_id",referencedColumnName="id")
	@NotFound(action=NotFoundAction.IGNORE)
	private CompanyMessage companyMessage;
	
	/**
	 * 保单记录
	 */
	@OneToMany(mappedBy = "sysUser",cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private Set<OrderMessage> orderMessage;
	
}
