package com.shenghesun.treasure.system.dictionary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.treasure.entity.base.BaseCode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//运输方式字典
@Entity
@Table
@Data  
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseTransport extends BaseCode{
	@Column(length = 10)
	private String businessCode;
	@Column(length = 1)
	private String city;
}
