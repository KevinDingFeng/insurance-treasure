package com.shenghesun.treasure.system.cpic;

import lombok.Data;

/**
 * 提供的外部接口，货运承保结果返回
 */
@Data
public class ReturnApprovl{

	private String orderNo;
	
	private String applyId;
	
	private String type;
	
	private String workType;
	
	private String applyNo;
	
	private String policyNo;
	
	private String status;
	
	private String comments;
	
	private String statusEpolicy;
}
