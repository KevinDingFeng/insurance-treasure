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
public class CodeList extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -3163096919831705249L;
	
	@Column
	private String codeKey;
	
	@Column
	private String codeValue;
}
