package com.shenghesun.treasure.system.order;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.shenghesun.treasure.entity.base.BaseEntity;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table
@Data  
@ToString(exclude = {"companyMessage"},callSuper = true)
@EqualsAndHashCode(exclude = {"companyMessage"},callSuper = true)
public class FundDetails extends BaseEntity implements Serializable{

	private static final long serialVersionUID = -7917411850934668617L;

	//资金数
	@Column(columnDefinition="DECIMAL(16,2)")
	private Integer price;
	
	//资金状态，入账还是出账，这里用正负号表示
	@Column
	private String plusOrMinus;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.MERGE)
	@JoinColumn(name = "company_id",referencedColumnName="id")
	@NotFound(action=NotFoundAction.IGNORE)
	private CompanyMessage companyMessage;
}
