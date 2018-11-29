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
public class UnionTransCode extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 6625853882803552837L;
	
	/**
	 * 联盟速运接口运输代码
	 */
	@Column
	private String UnionTranscode;
	
	/**
	 * 物流宝宝运输代码
	 */
	@Column
	private String WBTranscode;
}
