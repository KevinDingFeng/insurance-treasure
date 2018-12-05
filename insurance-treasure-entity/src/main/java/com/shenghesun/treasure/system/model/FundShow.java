package com.shenghesun.treasure.system.model;

import java.io.Serializable;
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
public class FundShow extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -7917411850934668617L;

	private String preminum;
	
	private String plusOrMinus;
	
}
