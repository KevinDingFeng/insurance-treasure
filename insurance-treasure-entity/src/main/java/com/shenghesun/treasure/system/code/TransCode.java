package com.shenghesun.treasure.system.code;

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
public class TransCode extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = -8540943432301920679L;

	@Column
	private String transCode;
	
	/**
	 * 投保 运输工具
	 */
	@Column
	private String kindName;
	
	/**
	 *投保 运输方式
	 */
	@Column
	private String kind;
	
	/**
	 *投保 险种代码
	 */
	@Column
	private String classType;
	
	/**
	 *投保条款代码
	 */
	@Column
	private String itemCode;
	
	/**
	 *投保 条款内容
	 */
	@Column
	private String itemContent;
	/**
	 * 投保 条款名称
	 */
	@Column
	private String itemName;
	/**
	 * 标识名称
	 */
	@Column
	private String markName;

}
