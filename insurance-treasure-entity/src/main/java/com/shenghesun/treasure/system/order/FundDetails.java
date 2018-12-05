package com.shenghesun.treasure.system.order;

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
public class FundDetails extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -7917411850934668617L;

	//资金数
	@Column(columnDefinition="DECIMAL(16,2)")
	private Integer preminum;
	
	//资金状态，入账还是出账，0代表出账 1代表入账
	@Column
	private String plusOrMinus;
	
	@Column
	private Long companyId;
	
	@Column
	private Long userId;

}
