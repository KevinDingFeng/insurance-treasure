package com.shenghesun.treasure.system.dictionary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.treasure.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//运输方式字典
@Entity
@Table
@Data  
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseTransport extends BaseEntity{


	//运输方式名称
	@Column(length = 10)
	private String tname;
	
	//运输方式代码
	@Column(length = 10)
	private String tcode;
	
	//上级运输代码
	@Column(length = 10)
	private String tparentCode;
	
	//业务类型代码
	@Column(length = 10)
	private String businessCode;
	
	//国内国际
	@Column(length = 10)
	private String country;
}
