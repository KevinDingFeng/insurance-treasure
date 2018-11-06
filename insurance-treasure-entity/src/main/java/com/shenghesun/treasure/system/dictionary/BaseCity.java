package com.shenghesun.treasure.system.dictionary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.treasure.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//城市数据字典
@Entity
@Table
@Data  
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseCity extends BaseEntity{
	
	@Column(length = 10)
	private String code;
	
	@Column(length = 50)
	private String name;
}
