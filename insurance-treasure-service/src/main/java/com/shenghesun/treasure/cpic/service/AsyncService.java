package com.shenghesun.treasure.cpic.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.order.service.OrderMessageService;
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
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private WebServiceClient webServiceClient;
	
	@Async("asyncServiceExecutor")
    public Map<String,Object> executeAsync(OrderMessage orderMessage) {
		Map<String,Object> map = new HashMap<String,Object>();
        logger.info("start executeAsync");
        try{
        	if(orderMessage != null) {
    			OrderMessage pmTemp = new OrderMessage();
    			BeanUtils.copyProperties(orderMessage, pmTemp);
    			pmTemp.setId(null);
    			pmTemp.setCreation(null);
    			pmTemp.setLastModified(null);
    			pmTemp.setVersion(0l);
    				boolean flag = true;
    				String xml = orderMessage2Xml(pmTemp);
    				System.out.println(xml);
        			if(StringUtils.isNotEmpty(xml)) {
        				//货运险承保接口
        				map = webServiceClient.approvl(xml,orderMessage);
        				flag = (boolean) map.get("flag");
        				System.out.println(flag);
        				if(!flag) {
        					flag = false;
        				}
        			}
    				if(flag) {
    					log.info("订单号为:"+orderMessage.getOrderNo()+"的订单投保成功");
    				}else {
    					log.info("订单号为:"+orderMessage.getOrderNo()+"的订单投保失败");
    				}
    		}
        }catch(Exception e){
            e.printStackTrace();
            logger.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
        }
        //logger.info("end executeAsync");
		return map;
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
