package com.shenghesun.treasure.external.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
	 * 	@Title
	 *  @param orderMessage
	 *  @return ReturnApprovl
	 *  @author zdd
	 *	@date 2018年12月13日上午11:37:53
	 *  @Description 对外接口使用，同步进行投保返回数据
	 *  			1.将投保对象转成xml
	 *  			2.进行投保，获取响应xml
	 *  			3.将响应xml转对象
	 *  			4.处理响应对象
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
	 * 	@Title
	 *  @param approvl
	 *  @param orderMessage
	 *  @return ReturnApprovl
	 *  @author zdd
	 *	@date 2018年12月13日上午11:40:21
	 *  @Description 对外接口使用
	 *  			1.处理响应对象，进行保存更新订单信息等操作
	 *  			2.创建接口返回对象，设置需要返回的信息
	 */
	public ReturnApprovl manageOutApprovl(Approvl approvl,OrderMessage orderMessage) {
		approvlResultService.manageApprovl(approvl, orderMessage);
		ReturnApprovl returnApprovl = new ReturnApprovl();
		if(approvl != null) {
			//设置外部接口调用返回数据
			returnApprovl.setPreminum(orderMessage.getPreminum());
			BeanUtils.copyProperties(approvl, returnApprovl);
		}
		return returnApprovl;
	}
}
