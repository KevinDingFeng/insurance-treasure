package com.shenghesun.treasure.system.union;

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
public class UnionPackageCode extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 6625853882803552837L;
	
	/**
	 * 联盟速运接口包装代码
	 */
	@Column
	private String UnionPackagecode;
	
	/**
	 * 物流宝宝包装代码
	 */
	@Column
	private String WBPackagecode;
}
