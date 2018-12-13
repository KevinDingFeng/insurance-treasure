package com.shenghesun.treasure.cpic.service;

import java.util.Iterator;

import javax.xml.rpc.ServiceException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.cpic.Approvl;
import com.shenghesun.treasure.system.cpic.Policy;
import com.shenghesun.util.cpic.XmlUtils;
import org.apache.axis.client.Stub;
import org.apache.commons.lang3.StringUtils;

import cn.com.cpic.wss.propertyinsurance.commonservice.freight.FreightCommonServiceLocator;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.IZrxCommonService;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalProduct;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalRequest;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalResponse;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.LoginUser;
import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.SysMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QueryPolicyService {

	@Value("${cpic.userName}")
	private String userName;
	@Value("${cpic.password}")
	private String password;
	/**
	 * 险种代码
	 */
	@Value("${cpic.classescode}")
	private String classesCode;
	@Value("${cpic.web.wsdl.url}")
	private String _WsUrl;
	@Autowired
	private PolicyService policyService;
	/**
	 * 查询保单支付状态，返回保单号和电子保单，base64PDF格式
	 * @throws Exception 
	 * 
	 * @throws IOException
	 * @throws ServiceException
	 * @throws DocumentException
	 */
	public Policy queryPolicyStatus(Approvl approvl) {
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
		Policy resultPolicy = null;
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
			policy.addElement("UNITCODE").setText(approvl.getUnitCode());
			//投保单号，投保后返回
			policy.addElement("APPLYNO").setText(approvl.getApplyNo());
			String str = XmlUtils.toPrettyString(document,"GBK");
			request.setPolicyInfo(str);
			//默认
			request.setCheckCode("hyxnew");
			((Stub) this.getBinding()).setTimeout(60000);
			//调用查询接口
			reposne = this.getBinding().queryPolicyStatus(request);
			//返回结果,返回保单号和电子保单,PDF格式
			String policyInfo = reposne.getPolicyInfo();
			resultPolicy = xml2QueryPolicy(policyInfo);
			policyService.save(resultPolicy);
			log.error(reposne.getPolicyInfo());
			SysMessage sysMessage = reposne.getSysMessage();
			if (sysMessage != null) {
				log.info("错误信息:" + sysMessage.getErrorMsg() + "\n");
			}				
			log.info("返回结果: \r" + reposne.getPolicyInfo()+ "\n"); 	        
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
		return resultPolicy;
	}

	public IZrxCommonService getBinding() throws ServiceException {
		FreightCommonServiceLocator locator = new FreightCommonServiceLocator();
		String url = _WsUrl+"FreightCommonService?wsdl";
		locator.setFreightCommonServicePortEndpointAddress(url);
		return locator.getFreightCommonServicePort();
	}

	private Policy xml2QueryPolicy(String xml) {
		if(StringUtils.isNotEmpty(xml)) {
			Policy policy = new Policy();
			Document resultDo;
			try {
				resultDo = XmlUtils.parseWithSAX(xml,"GBK");
				Element rootElt = resultDo.getRootElement(); // 获取根节点

				Iterator<?> iter = rootElt.elementIterator("DATE"); // 获取根节点下的子节点CONFIG
				while (iter.hasNext()) {
					Element recordEle = (Element) iter.next();
					Iterator<?> iterData = recordEle.elementIterator("P_AUDIT_WORK");
					while(iterData.hasNext()) {
						Element data = (Element)iterData.next();
						String unitcode = data.elementTextTrim("P_AUDIT_WORK__UNITCODE");
						policy.setUnitcode(unitcode);

						String applyno = data.elementTextTrim("P_AUDIT_WORK__APPLYNO");
						policy.setApplyno(applyno);

						String policyNo= data.elementTextTrim("P_AUDIT_WORK__POLICYNO");
						policy.setPolicyno(policyNo);

						String resultCode = data.elementTextTrim("P_AUDIT_WORK__RESULTCODE");
						policy.setResultCode(resultCode);

						String epolicyStatus = data.elementTextTrim("STATUS_EPOLICY");
						policy.setEpolicyStatus(epolicyStatus);

						String epolicyFile = data.elementTextTrim("FILE_EPOLICY");
						policy.setEpolicyFile(epolicyFile);

						String status = data.elementTextTrim("STATUS");
						policy.setStatus(status);
					}
				}
				return policy;
			} catch (DocumentException e) {
				log.error("错误信息:" + e.getStackTrace());
				return null;
			}

		}
		return null;
	}
}
