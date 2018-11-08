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
public class GoodsCode extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = -8540943432301920679L;

	@Column
	private String goodsCode;
	
	/**
	 * 投保货物名称代码
	 */
	@Column
	private String itemCode;
	
	/**
	 * 投保 货物名称
	 */
	@Column
	private String itemName;
}
