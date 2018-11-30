package com.shenghesun.treasure.system.union.code;

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
public class UnionGoodsCode extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -7688627497273461894L;

	/**
	 * 联盟速运接口货物名称
	 */
	@Column
	private String unionGoodsName;
	
	/**
	 * 物流宝宝货物名称代码
	 */
	@Column
	private String wBGoodsCode;
	
}
