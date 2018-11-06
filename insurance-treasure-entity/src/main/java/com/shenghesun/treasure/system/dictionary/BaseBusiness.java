package com.shenghesun.treasure.system.dictionary;

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
public class BaseBusiness extends BaseEntity{

	/**
	 * 业务类型名称
	 */
	@Column(length = 10)
	private String name;
	/**
	 * 业务类型代码
	 */
	@Column(length = 10)
	private String code;
	/**
	 * 费率
	 */
	@Column(columnDefinition="DECIMAL(16,3)")
	private Integer rate;
	
}
