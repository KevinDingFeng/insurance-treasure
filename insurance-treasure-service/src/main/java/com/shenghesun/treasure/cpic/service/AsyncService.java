package com.shenghesun.treasure.cpic.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.approvl.service.ApprovlResultService;
import com.shenghesun.treasure.system.cpic.Approvl;
import com.shenghesun.treasure.system.cpic.webservice.Datas;
import com.shenghesun.treasure.system.cpic.webservice.Freightcpic;
import com.shenghesun.treasure.system.cpic.webservice.Header;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.util.cpic.StringGenerateUtils;
import com.shenghesun.util.cpic.XStreamUtil;

import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * 异步方法
 * @ClassName: AsyncService 
 * @Description: TODO
 * @author: yangzp
 * @date: 2018年10月10日 下午7:04:32  
 */
@Service
@Slf4j
public class AsyncService {

	@Autowired
	private WebServiceClient webServiceClient;
	@Autowired
	private ApprovlResultService approvlResultService;
	/**
	 * 异步  项目使用投保方法
	 * @param orderMessage
	 * @return
	 */
	@Async("asyncServiceExecutor")
	public void executeAsync(OrderMessage orderMessage) {
		log.info("start executeAsync");
		try{
			if(orderMessage != null) {
				String xml = getOrderMessageXml(orderMessage);
				boolean flag = true;
				if(StringUtils.isNotEmpty(xml)) {
					//货运险承保接口
					ApprovalResponse response = webServiceClient.approvl(xml);
					Approvl approvl =  approvlResultService.xml2Approvl(response.getPolicyInfo(),orderMessage.getOrderNo());
					//处理投保返回结果
					flag = approvlResultService.manageApprovl(approvl,orderMessage);
					if(!flag) {
						flag = false;
					}
				}
				//记录日志
				setLog(flag,orderMessage);
			}
		}catch(Exception e){
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
		}
		log.info("end executeAsync");
	}

	/**
	 * 将投保对象处理转为xml用于投保
	 */
	public String getOrderMessageXml(OrderMessage orderMessage) {
		OrderMessage pmTemp = new OrderMessage();
		BeanUtils.copyProperties(orderMessage, pmTemp);
		pmTemp.setId(null);
		pmTemp.setCreation(null);
		pmTemp.setLastModified(null);
		pmTemp.setVersion(0l);
		String xml = orderMessage2Xml(pmTemp);
		return xml;
	}
	/**
	 * 记录投保日志
	 */
	public void setLog(Boolean flag,OrderMessage orderMessage) {
		if(flag) {
			log.info("订单号为:"+orderMessage.getOrderNo()+"的订单投保成功");
		}else {
			log.info("订单号为:"+orderMessage.getOrderNo()+"的订单投保失败");
		}
	}
	/**
	 * 货运险承保接口应答报文转xml
	 * @Title: orderMessage2Xml 
	 * @Description: TODO 
	 * @param orderMessage
	 * @return  String 
	 * @author yangzp
	 * @date 2018年10月10日下午5:36:20
	 **/ 
	private String orderMessage2Xml(OrderMessage orderMessage) {
		Freightcpic freightcpic = new Freightcpic();

		Header header = new Header();
		header.setApplyid(StringGenerateUtils.generateId());
		header.setClassestype(orderMessage.getClassesType());
		freightcpic.setHeader(header);

		Datas datas = new Datas();
		datas.setOrderMessage(orderMessage);

		freightcpic.setDatas(datas);

		return XStreamUtil.beanToXmlWithTag(freightcpic);
	}


}
