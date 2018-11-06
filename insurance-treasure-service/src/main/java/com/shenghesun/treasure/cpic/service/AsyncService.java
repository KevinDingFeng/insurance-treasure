package com.shenghesun.treasure.cpic.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.system.cpic.webservice.Datas;
import com.shenghesun.treasure.system.cpic.webservice.Freightcpic;
import com.shenghesun.treasure.system.cpic.webservice.Header;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.util.cpic.StringGenerateUtils;
import com.shenghesun.util.cpic.XStreamUtil;


/**
  * 异步方法
  * @ClassName: AsyncService 
  * @Description: TODO
  * @author: yangzp
  * @date: 2018年10月10日 下午7:04:32  
  */
@Service
public class AsyncService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/*@Autowired
	private PayService payService;*/
	@Autowired
	private OrderMessageService orderMessageService;
	@Autowired
	private WebServiceClient webServiceClient;
	
//	@Autowired
//	private SmsCodeService smsCodeService;
	
	@Async("asyncServiceExecutor")
    public void executeAsync(OrderMessage orderMessage) {
        logger.info("start executeAsync");
        try{
        	//OrderMessage orderMessage = orderMessageService.findById(1L);
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
        				flag = webServiceClient.approvl(xml,orderMessage);
        				System.out.println(flag);
        				if(!flag) {
        					flag = false;
        				}
        			}
    				if(flag) {
    					
    				}else {
    					
    				}
    			
    		}
//        	orderMessage orderMessage = payService.findByOrderNo(orderNo);
    		
    		//修改保单状态
    		//测试用，正式删除
//    		orderMessage.setOrderNo(StringGenerateUtils.generateId());
    		
    	/*	if(orderMessage != null) {
    			orderMessage pmTemp = new orderMessage();
    			BeanUtils.copyProperties(orderMessage, pmTemp);
    			pmTemp.setId(null);
    			pmTemp.setCreation(null);
    			pmTemp.setLastModified(null);
    			pmTemp.setVersion(null);
    			List<Mark> markList = pmTemp.getMark();
    			if(!CollectionUtils.isEmpty(markList)) {
    				boolean flag = true;
    				for(Mark mark : markList) {
    					String xml = orderMessage2Xml(pmTemp,mark.getMark());
            			if(StringUtils.isNotEmpty(xml)) {
            				//货运险承保接口
            				flag = webServiceClient.approvl(xml,orderMessage);
            				if(!flag) {
            					flag = false;
            				}
            			}
    				}
    				if(flag) {
    					
    				}else {
    					
    				}
    			}
    			
    		}*/
        }catch(Exception e){
            e.printStackTrace();
            logger.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
        }
        //logger.info("end executeAsync");
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
		header.setClassestype(orderMessage.getClassestype());
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
