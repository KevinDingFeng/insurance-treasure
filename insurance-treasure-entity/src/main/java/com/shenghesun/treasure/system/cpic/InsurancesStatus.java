package com.shenghesun.treasure.system.cpic;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.treasure.entity.base.BaseCode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
/**
 * 
 * @author zdd
 * 保险状态表
 */
@Entity
@Table
@Data  
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InsurancesStatus extends BaseCode{

}
