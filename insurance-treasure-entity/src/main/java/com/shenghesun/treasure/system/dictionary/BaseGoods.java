package com.shenghesun.treasure.system.dictionary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.treasure.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//商品数据字典
@Entity
@Table
@Data  
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseGoods extends BaseEntity{
	
	//字典类型，根据前台输入该字段的不同，查询不同内容的数据字典
	@Column(length = 10)
	private String type;
	
	@Column(length = 10)
	private String code;
	
	@Column(length = 50)
	private String name;
	
	//该字段填写父类型,归属的业务类型
	@Column(length = 10)
	private String parentCode;
	
	//该字段用于一级和二级货物名称，也是参考code字段，一级该段为空，二级参照一级的code
	@Column(length = 10)
	private String parentId;
	
}
