package com.shenghesun.treasure.approvl.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.cpic.service.ApprovlService;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.system.cpic.Approvl;
import com.shenghesun.treasure.system.cpic.ReturnApprovl;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.util.cpic.XmlUtils;

import cn.com.cpic.wss.propertyinsurance.commonservice.freight.types.ApprovalResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApprovlResultService {

	@Autowired
	private ApprovlService approvlService;
	@Autowired
	private OrderMessageService orderMessageService;

	/**
	 * 处理投保结果
	 */
	public Map<String,Object> manageApprovl(ApprovalResponse reposne,OrderMessage orderMessage) {
		Map<String,Object> map = new HashMap<String,Object>();
		Approvl approvl =  xml2Approvl(reposne.getPolicyInfo(),orderMessage);
		if(approvl != null) {
			//设置外部接口调用返回数据
			ReturnApprovl returnApprovl = setOutApprovl(approvl);
			map.put("approvl", returnApprovl);
			//系统内部对返回结果处理
			setLocalArrrovl(approvl);
			//修改订单
			map = changeOrder(approvl,orderMessage,map);
		}
		return map;
	}
	
	/**
	 * 系统内部对投保返回对象处理
	 */
	public void setLocalArrrovl(Approvl approvl) {
		Approvl approvlDB = approvlService.findByApplyId(approvl.getApplyId());
		if(approvlDB != null) {
			//已有则修改
			BeanUtils.copyProperties(approvl, approvlDB);
			approvlService.save(approvlDB);
		}else {
			//新增
			approvlService.save(approvl);
		}
	}
	/**
	 * 接口返回对象设置
	 */
	public ReturnApprovl setOutApprovl(Approvl approvl) {
		ReturnApprovl returnApprovl = new ReturnApprovl();
		returnApprovl.setOrderNo(approvl.getOrderNo());
		returnApprovl.setApplyId(approvl.getApplyId());
		returnApprovl.setType(approvl.getType());
		returnApprovl.setWorkType(approvl.getWorkType());
		returnApprovl.setApplyNo(approvl.getApplyNo());
		returnApprovl.setPolicyNo(approvl.getPolicyNo());
		returnApprovl.setStatus(approvl.getStatus());
		returnApprovl.setComments(approvl.getComments());
		returnApprovl.setStatusEpolicy(approvl.getStatusEpolicy());
		return returnApprovl;
	}
	/**
	 * 根据投保返回结果修改订单相关信息
	 */
	public Map<String,Object> changeOrder(Approvl approvl,OrderMessage orderMessage,Map<String,Object> map) {
		/**
		 * 保单状态
		 * 7  待审核-----投保单录入成功，提交人工审核
		   36 待签发-----投保单录入成功，自动核保通过 （非在线支付情况，状态 10 --保单生效）
		   19 提交失败----投保单录入成功，系统提交核保失败，需联系技术人员处理
		 * 
		 */
		String status = approvl.getStatus();
		if(StringUtils.isNotEmpty(status)) {
			orderMessage.setInsuranceStatus(Integer.parseInt(status));
			orderMessage.setApply_no(approvl.getApplyNo());
			orderMessageService.save(orderMessage);
			if("10".equals(status)) {
				map.put("flag", true);
				return map;
			}else if("19".equals(status)) {
				map.put("flag", false);
				return map;
			}
		}
		map.put("flag", false);
		return map;
	}

	/**
	 * 货运险承保接口应答报文转对象
	 * @Title: xml2Approvl 
	 * @Description: TODO 
	 * @param xml  void 
	 * @author yangzp
	 * @date 2018年10月10日下午5:18:00
	 **/ 
	private Approvl xml2Approvl(String xml, OrderMessage payMessage) {
		if(StringUtils.isNotEmpty(xml)) {
			Approvl approvl = new Approvl();
			approvl.setOrderNo(payMessage.getOrderNo());
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
