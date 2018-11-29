package com.shenghesun.treasure.cpic.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.cpic.webservice.Datas;
import com.shenghesun.treasure.system.cpic.webservice.Freightcpic;
import com.shenghesun.treasure.system.cpic.webservice.Header;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.util.cpic.StringGenerateUtils;
import com.shenghesun.util.cpic.XStreamUtil;

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

	/**
	 * 异步  项目使用投保方法
	 * @param orderMessage
	 * @return
	 */
	@Async("asyncServiceExecutor")
	public Map<String,Object> executeAsync(OrderMessage orderMessage) {
		Map<String, Object> map = execute(orderMessage);
		return map;
	}
	/**
	 * 同步 提供给外部投保方法
	 */
	public Map<String,Object> executeApprovl(OrderMessage orderMessage) {
		Map<String, Object> map = execute(orderMessage);
		return map;
	}
	/**
	 * 投保方法
	 */
	public Map<String,Object> execute(OrderMessage orderMessage){
		Map<String,Object> map = new HashMap<String,Object>();
		log.info("start executeAsync");
		try{
			if(orderMessage != null) {
				String xml = getOrderMessageXml(orderMessage);
				boolean flag = true;
				if(StringUtils.isNotEmpty(xml)) {
					//货运险承保接口
					map = webServiceClient.approvl(xml,orderMessage);
					flag = (boolean) map.get("flag");
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
		return map;
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
		//		if("2".equals(orderMessage.getClassestype())) {
		//			orderMessage.setFlightareacode("12040200");
		//		}
		datas.setOrderMessage(orderMessage);

		freightcpic.setDatas(datas);

		return XStreamUtil.beanToXmlWithTag(freightcpic);
	}



}
