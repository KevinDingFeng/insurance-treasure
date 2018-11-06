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
	private Integer orderAmount;
	
	//资金状态，入账还是出账，0代表出账 1代表入账
	@Column
	private String plusOrMinus;
	
	@Column
	private String companyId;
	
	@Column
	private String orderId;
	//资金明细与公司关系
/*	@ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.MERGE)
	@JoinColumn(name = "company_id",referencedColumnName="id")
	@NotFound(action=NotFoundAction.IGNORE)
	private CompanyMessage companyMessage;*/
	
	//资金明细与订单关系
/*	@ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.MERGE)
	@JoinColumn(name = "order_id",referencedColumnName="id")
	@NotFound(action=NotFoundAction.IGNORE)
	private OrderMessage orderMessage;*/
}
