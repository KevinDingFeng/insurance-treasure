package com.shenghesun.treasure.system.dictionary;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.treasure.entity.base.BaseCode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//商品数据字典
@Entity
@Table
@Data  
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseGoods extends BaseCode{
	
}
