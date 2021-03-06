package com.shenghesun.treasure.approvl.service;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.core.constant.OrderConstant;
import com.shenghesun.treasure.cpic.service.ApprovlService;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.system.cpic.Approvl;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.util.cpic.XmlUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApprovlResultService {

	@Autowired
	private ApprovlService approvlService;
	@Autowired
	private OrderMessageService orderMessageService;

	/**
	 * 	@Title
	 *  @param approvl
	 *  @param orderMessage
	 *  @return void
	 *  @author zdd
	 *	@date 2018年12月13日上午11:29:53
	 *  @Description 处理投保结果
	 * 				1.保存响应对象
	 * 				2.更新订单信息的保险状态和保单号，然后进行保存
	 * 				3.获取响应的保单状态，如果状态为10，则表示投保成功,状态为7代表太保人工审核，其他表示投保失败，进行日志记录
	 */
	public void manageApprovl(Approvl approvl,OrderMessage orderMessage) {
		if(approvl != null) {
			//保存投保结果
			approvlService.save(approvl);
			//修改订单
			String status = approvl.getStatus().toString();
			if(StringUtils.isNotEmpty(status.toString())) {
				orderMessage.setInsuranceStatus(status);
				orderMessage.setApplyNo(approvl.getApplyNo());
				orderMessageService.save(orderMessage);
				if(OrderConstant.APPROVL_SUCCESS.equals(status)||OrderConstant.APPROVL_WAIT.equals(status)) {
					log.info("订单号为:"+orderMessage.getOrderNo()+"的订单投保成功");
				}else {
					log.info("订单号为:"+orderMessage.getOrderNo()+"的订单投保失败");
				}
			}
		}
	}
	/**
	 * 货运险承保接口应答报文转对象
	 * @Title: xml2Approvl 
	 * @Description: TODO 
	 * @param xml  void 
	 * @author yangzp
	 * @date 2018年10月10日下午5:18:00
	 **/ 
	public Approvl xml2Approvl(String xml, String orderNo) {
		if(StringUtils.isNotEmpty(xml)) {
			Approvl approvl = new Approvl();
			approvl.setOrderNo(orderNo);
			Document resultDo;
			try {
				resultDo = XmlUtils.parseWithSAX(xml,"GBK");
				Element rootElt = resultDo.getRootElement(); // 获取根节点

				Iterator<?> iter = rootElt.elementIterator("CONFIG"); // 获取根节点下的子节点CONFIG
				while (iter.hasNext()) {
					Element recordEle = (Element) iter.next();
					String ApplyId = recordEle.elementTextTrim("ApplyId"); // 拿到CONFIG节点下的子节点ApplyId值
					approvl.setApplyId(ApplyId);
					String type = recordEle.elementTextTrim("TYPE"); // 拿到CONFIG节点下的子节点TYPE值
					approvl.setType(type);

					String worktype = recordEle.elementTextTrim("WORKTYPE"); // 拿到CONFIG节点下的子节点WORKTYPE值
					approvl.setWorkType(worktype);
				}

				Iterator<?> iterResult = rootElt.elementIterator("RESULT"); // 获取根节点下的子节点RESULT
				while (iterResult.hasNext()) {
					Element recordEle = (Element) iterResult.next();
					String unitcode = recordEle.elementTextTrim("UNITCODE"); // 拿到RESULT节点下的子节点UNITCODE值
					approvl.setUnitCode(unitcode);

					String applyno = recordEle.elementTextTrim("APPLYNO"); // 拿到RESULT节点下的子节点APPLYNO值
					approvl.setApplyNo(applyno);

					String policyno = recordEle.elementTextTrim("POLICYNO"); // 拿到RESULT节点下的子节点POLICYNO值
					approvl.setPolicyNo(policyno);

					String status = recordEle.elementTextTrim("STATUS"); // 拿到RESULT节点下的子节点STATUS值
					approvl.setStatus(status);

					String comments = recordEle.elementTextTrim("COMMENTS"); // 拿到RESULT节点下的子节点COMMENTS值
					approvl.setComments(comments);
					
					String statusEpolicy = recordEle.elementTextTrim("STATUS_EPOLICY"); // 拿到RESULT节点下的子节点STATUS_EPOLICY值
					approvl.setStatusEpolicy(statusEpolicy);

					String fileEpolicy = recordEle.elementTextTrim("FILE_EPOLICY"); // 拿到RESULT节点下的子节点FILE_EPOLICY值
					approvl.setFileEpolicy(fileEpolicy);
					return approvl;
				}
			} catch (DocumentException e) {
				log.error("错误信息:" + e.getStackTrace());
				return null;
			}

		}
		return null;
	}
}
