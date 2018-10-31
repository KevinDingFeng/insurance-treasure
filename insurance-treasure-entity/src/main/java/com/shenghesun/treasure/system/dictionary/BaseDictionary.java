package com.shenghesun.treasure.system.dictionary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.treasure.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//数据字典
@Entity
@Table
@Data  
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseDictionary extends BaseEntity{
	
	//字典类型，根据前台输入该字段的不同，查询不同内容的数据字典
	@Column(length = 10)
	private String type;
	
	@Column(length = 10)
	private String code;
	
	@Column(length = 10)
	private String name;
	
	@Column(length = 10)
	private String parentCode;
	
	
}
