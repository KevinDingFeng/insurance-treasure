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

import com.shenghesun.treasure.system.cpic.webservice.Datas;
import com.shenghesun.treasure.system.cpic.webservice.Freightcpic;
import com.shenghesun.treasure.system.cpic.webservice.Header;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.utils.StringGenerateUtils;
import com.shenghesun.treasure.utils.XStreamUtil;


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
	private WebServiceClient webServiceClient;
	
//	@Autowired
//	private SmsCodeService smsCodeService;
	
	@Async("asyncServiceExecutor")
    public void executeAsync() {
        //logger.info("start executeAsync");
        try{
//        	PayMessage payMessage = payService.findByOrderNo(orderNo);
    		
    		//修改保单状态
    		//测试用，正式删除
//    		payMessage.setOrderNo(StringGenerateUtils.generateId());
    		
    	/*	if(payMessage != null) {
    			PayMessage pmTemp = new PayMessage();
    			BeanUtils.copyProperties(payMessage, pmTemp);
    			pmTemp.setId(null);
    			pmTemp.setCreation(null);
    			pmTemp.setLastModified(null);
    			pmTemp.setVersion(null);
    			List<Mark> markList = pmTemp.getMark();
    			if(!CollectionUtils.isEmpty(markList)) {
    				boolean flag = true;
    				for(Mark mark : markList) {
    					String xml = payMessage2Xml(pmTemp,mark.getMark());
            			if(StringUtils.isNotEmpty(xml)) {
            				//货运险承保接口
            				flag = webServiceClient.approvl(xml,payMessage);
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
	 * @Title: payMessage2Xml 
	 * @Description: TODO 
	 * @param payMessage
	 * @return  String 
	 * @author yangzp
	 * @date 2018年10月10日下午5:36:20
	 **/ 
	private String payMessage2Xml(OrderMessage payMessage, String markNo) {
		Freightcpic freightcpic = new Freightcpic();
		
		Header header = new Header();
		header.setApplyid(StringGenerateUtils.generateId());
		//header.setClassestype(payMessage.getClassestype());
		freightcpic.setHeader(header);
		
		Datas datas = new Datas();
//		if("2".equals(payMessage.getClassestype())) {
//			payMessage.setFlightareacode("12040200");
//		}
		datas.setPayMessage(payMessage);
		
		freightcpic.setDatas(datas);
		
		return XStreamUtil.beanToXmlWithTag(freightcpic);
	}
}
