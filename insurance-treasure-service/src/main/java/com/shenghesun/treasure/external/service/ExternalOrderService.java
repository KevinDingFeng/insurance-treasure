package com.shenghesun.treasure.external.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.approvl.service.ApprovlResultService;
import com.shenghesun.treasure.cpic.service.AsyncService;
import com.shenghesun.treasure.cpic.service.WebServiceClient;
import com.shenghesun.treasure.system.cpic.Approvl;
import com.shenghesun.treasure.system.cpic.ReturnApprovl;
import com.shenghesun.treasure.system.order.OrderMessage;

import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExternalOrderService {

	@Autowired
	private WebServiceClient webServiceClient;
	@Autowired
	private ApprovlResultService approvlResultService;
	@Autowired
	private AsyncService asyncService;
	/**
	 * 同步 提供给外部投保方法
	 */
	public ReturnApprovl executeApprovl(OrderMessage orderMessage) {
		log.info("start executeAsync");
		ReturnApprovl returnApprovl = null;
		try{
			if(orderMessage != null) {
				String xml = asyncService.getOrderMessageXml(orderMessage);
				if(StringUtils.isNotEmpty(xml)) {
					//货运险承保接口
					ApprovalResponse response = webServiceClient.approvl(xml);
					Approvl approvl =  approvlResultService.xml2Approvl(response.getPolicyInfo(),orderMessage.getOrderNo());
					returnApprovl = manageOutApprovl(approvl,orderMessage);
				}
			}
		}catch(Exception e){
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
		}
		log.info("end executeAsync");
		return returnApprovl;
	}
	
	
	/**
	 * 提供接口对结果处理投保结果
	 */
	public ReturnApprovl manageOutApprovl(Approvl approvl,OrderMessage orderMessage) {
		approvlResultService.manageApprovl(approvl, orderMessage);
		ReturnApprovl returnApprovl = new ReturnApprovl();
		if(approvl != null) {
			//设置外部接口调用返回数据
			returnApprovl.setOrderNo(approvl.getOrderNo());
			returnApprovl.setApplyId(approvl.getApplyId());
			returnApprovl.setType(approvl.getType());
			returnApprovl.setWorkType(approvl.getWorkType());
			returnApprovl.setApplyNo(approvl.getApplyNo());
			returnApprovl.setPolicyNo(approvl.getPolicyNo());
			returnApprovl.setStatus(approvl.getStatus());
			returnApprovl.setComments(approvl.getComments());
			returnApprovl.setStatusEpolicy(approvl.getStatusEpolicy());
			returnApprovl.setFileEpolicy(approvl.getFileEpolicy());
		}
		return returnApprovl;
	}
}
