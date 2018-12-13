package com.shenghesun.treasure.cpic.service;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Stub;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.com.cpic.wss.propertyinsurance.commonservice.freight.FreightCommonServiceLocator;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.IZrxCommonService;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalProduct;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalRequest;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalResponse;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.LoginUser;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.SysMessage;


/**
 * WebService客户端通用类
 * @ClassName: WebServiceClient 
 * @Description: TODO
 * @author: yangzp
 * @date: 2018年9月30日 下午1:30:19  
 */
@Service
public class WebServiceClient {

	private static final Logger log = Logger.getLogger(WebServiceClient.class);

	/**
	 * wsdl url
	 */
	@Value("${cpic.web.wsdl.url}")
	private String _WsUrl;

	@Value("${cpic.userName}")
	private String userName;
	@Value("${cpic.password}")
	private String password;
	/**
	 * 险种代码
	 */
	@Value("${cpic.classescode}")
	private String classesCode;

	/**
	 * 分公司代码
	 */
	@Value("${cpic.unitcode}")
	private String unitCode;

	public IZrxCommonService getBinding() throws ServiceException {
		FreightCommonServiceLocator locator = new FreightCommonServiceLocator();
		String url = _WsUrl+"FreightCommonService?wsdl";
		locator.setFreightCommonServicePortEndpointAddress(url);
		return locator.getFreightCommonServicePort();
	}
	/**
	 * 	@Title
	 *  @param xml
	 *  @return ApprovalRequest
	 *  @author zdd
	 *	@date 2018年12月13日上午11:35:50
	 *  @Description  货运承保前完善用户信息以及产品信息等
	 */
	public ApprovalRequest preApprovl(String xml) {
		ApprovalRequest request = null;
		try {
			request = new ApprovalRequest();
			//用户信息
			LoginUser userInfo = new LoginUser();
			userInfo.setUserName(userName);
			userInfo.setPassword(password);
			//产品信息
			ApprovalProduct productInfo = new ApprovalProduct();	
			//航空险种
			productInfo.setClassesCode(classesCode);
			//用户信息
			request.setUserInfo(userInfo);
			request.setProductInfo(productInfo);
			request.setPolicyInfo(xml);
			//默认
			request.setCheckCode("hyxnew");
			request.setFormCommit(true);
			((Stub) this.getBinding()).setTimeout(60000);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return request;
	}
	/**
	 * 货运险承保接口
	 * @Title: approvl 
	 * @Description: TODO 
	 * @param xml 请求报文xml
	 * @return  String 
	 * @author yangzp
	 * @date 2018年9月30日下午1:48:27
	 * 注解@Async(“asyncServiceExecutor”)，
	 * asyncServiceExecutor是前面ExecutorConfig.java中的方法名，
	 * 表明executeAsync方法进入的线程池是asyncServiceExecutor方法创建的
	 **/ 
	public ApprovalResponse approvl(String xml) {
		ApprovalResponse response = null;
		try {
			//完善投保用户相关信息
			ApprovalRequest request = preApprovl(xml);
			//投保
			response = this.getBinding().approval(request);
			//日志记录
			SysMessage sysMessage = response.getSysMessage();
			if (sysMessage != null) {
				log.error("错误类型:"+sysMessage.getErrorType() +"错误代码:"+sysMessage.getErrorCode()+"错误信息:"+sysMessage.getErrorMsg());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
		return response;
	}
}
