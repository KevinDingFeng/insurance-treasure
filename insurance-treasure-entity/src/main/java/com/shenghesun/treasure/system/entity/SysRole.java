package com.shenghesun.treasure.system.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.treasure.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 角色
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true, exclude = { "users" })
@EqualsAndHashCode(callSuper = true, exclude = { "users" })
public class SysRole extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -350044008120204151L;

	/**
	 * 等级，和用户中的等级相关，用户可以看到同等级及以下等级的角色
	 * 	一版情况，等级越高，看到的权限范围越大
	 */
	private int level;
	
	/**
	 * 角色名称
	 */
	@Column(length = 64, nullable = false)
	private String name;

	@Column(length = 255, nullable = true)
	private String remark;
	
	private boolean removed = false;
	/**
	 * 系统id，暂时没使用
	 */
	@Column(length = 20, nullable = true)
	private String sysId;

	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "roles")
	private Set<SysUser> users;
	
	@ManyToMany(fetch = FetchType.EAGER,cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "sys_role_permission", inverseJoinColumns = { @JoinColumn(name = "perm_id") }, joinColumns = {
			@JoinColumn(name = "role_id") })
	@JSONField(serialize = false)
	private Set<SysPermission> permissions;
	
}
