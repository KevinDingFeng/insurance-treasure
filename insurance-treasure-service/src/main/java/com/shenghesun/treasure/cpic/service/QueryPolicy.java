package com.shenghesun.treasure.cpic.service;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalProduct;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalRequest;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalResponse;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.LoginUser;

@Service
public class QueryPolicy {

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
	 * 查询保单支付状态，返回保单号和电子保单，base64PDF格式
	 * @throws Exception 
	 * 
	 * @throws IOException
	 * @throws ServiceException
	 * @throws DocumentException
	 */
/*	public void queryPolicyStatus() {
        // Test operation
		ApprovalRequest request = new ApprovalRequest();
		//用户信息
		LoginUser userInfo = new LoginUser();
		//测试账号
		userInfo.setUserName(userName);
		//测试密码
		userInfo.setPassword(password);
		//险种信息
		ApprovalProduct productInfo = new ApprovalProduct();	
		//航空险种
		productInfo.setClassesCode(classesCode);		
		ApprovalResponse reposne=null;
		try {
			//设置用户信息、产品信息
			request.setUserInfo(userInfo);
			request.setProductInfo(productInfo);
			
		Document document = DocumentHelper.createDocument();
			Element root = document.addElement("ROOT");
			Element config = root.addElement("CONFIG");
		    config.addElement("TYPE").setText("IN");
		    config.addElement("WORKTYPE").setText("2");
			
			Element data = root.addElement("DATA");
			Element policy = data.addElement("POLICY");
			//分公司代码，测试环境：3110100 正式环境后续提供，可配置参数
			policy.addElement("UNITCODE").setText("3110100");
			//投保单号，投保后返回
			policy.addElement("APPLYNO").setText("AHYXAGD24218D0004250");
			String str = XmlUtils.toPrettyString(document,"GBK");
			request.setPolicyInfo(str);
			//默认
			request.setCheckCode("hyxnew");
	        ((Stub) this.getBinding()).setTimeout(60000);
	        //调用查询接口
	        reposne = this.getBinding().queryPolicyStatus(request);
	        //返回结果,返回保单号和电子保单,PDF格式
	       log.error(reposne.getPolicyInfo());
			SysMessage sysMessage = reposne.getSysMessage();
			if (sysMessage != null) {
				log.info("错误信息:" + sysMessage.getErrorMsg() + "\n");
			}				
			log.info("返回结果: \r" + reposne.getPolicyInfo()+ "\n"); 	        
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
	}*/
}
