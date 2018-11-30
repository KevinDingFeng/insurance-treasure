package com.shenghesun.treasure.system.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.treasure.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 对外接口,不同用户走不同的接口流程
 * @author EDZ
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysUserType extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 7428348652885254275L;

	/**
	 * 用户账号
	 */
	@Column(length = 50)
	private String account;
	
	/**
	 * 类型
	 */
	@Column(length = 50)
	private String type;
	
	/**
	 * 接口说明
	 */
	@Column(length = 50)
	private String mark;
}
