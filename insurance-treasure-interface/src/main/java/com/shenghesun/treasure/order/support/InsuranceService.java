package com.shenghesun.treasure.order.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.service.CompanyMessageService;
import com.shenghesun.treasure.core.constant.OrderConstant;
import com.shenghesun.treasure.cpic.service.AsyncService;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InsuranceService {
	@Autowired
	AsyncService asyncService;
	@Autowired
	CompanyMessageService companyService;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderMessageService orderMessageService;
	/**
	 * 投保
	 */
	public JSONObject insurance(HttpServletRequest request,@Validated OrderMessage order,String comFrom) {
		//获取公司信息
		String token = HttpHeaderUtil.getToken((HttpServletRequest) request); 
		Long userId = TokenUtil.getLoginUserId(token);
		Long companyId = TokenUtil.getLoginCompanyId(token);
		CompanyMessage company = companyService.findById(companyId);
		log.info("投保用户ID："+userId+"投保公司ID: "+companyId);
		//完善订单信息
		Map<String,Object> orderMap = orderService.complete(request,order);
		order = (OrderMessage) orderMap.get(OrderConstant.Order);
		//判断完善信息过程中是否出现运输代码查找错误和货物代码错误
		JSONObject check = orderService.check(orderMap);
		if(check!=null) {
			return check;
		}
		//支付扣款并且进行投保
		return pay(company,order,comFrom);
	}
	/**
	 * 支付
	 */
	public JSONObject pay(CompanyMessage company,OrderMessage order,String comFrom) {
		Map<String, Object> map =null;
		if(company!=null&&order!=null) {
			//保费
			Double preminum = Double.parseDouble(order.getPreminum());
			//如果余额大于保单金额,才进行支付扣款
			if(company.getBalance()>=preminum) {
				company.setBalance(company.getBalance()-preminum);
				//修改订单状态
				order.setPayStatus(1);
				//调用投保
				map = approvl(comFrom,order);
			}else {
				orderMessageService.save(order);
				return JsonUtil.getFailJSONObject("余额不足，请联系管理员充值");
			}
			orderMessageService.save(order);
		}else {
			return JsonUtil.getFailJSONObject("公司不存在或订单不存在");
		}
		return JsonUtil.getSuccessJSONObject(map);
	}
	
	/**
	 * 系统使用需要及时返回信息，异步调用，提供的外部接口需要及时获取结果，调用方式为同步
	 * @param comFrom
	 * @param order
	 * @return
	 */
	public Map<String,Object> approvl(String comFrom,OrderMessage order){
		if(comFrom.equals(OrderConstant.SYS_LOCAL)) {
			//异步调用
			return asyncService.executeAsync(order);
		}else {
			//同步调用
			return asyncService.executeApprovl(order);
		}
	}
	/**
	 * 完成订单支付
	 * @param request
	 * @param orderNo
	 * @param sysLocal
	 * @return
	 */
	public JSONObject completePay(HttpServletRequest request, String orderNo, String sysLocal) {
		//获取登陆用户信息
		String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
		Long companyId = TokenUtil.getLoginCompanyId(token);
		//根据订单id查找到订单信息
		OrderMessage order = orderMessageService.findByOrderNo(orderNo);
		CompanyMessage company = companyService.findById(companyId);
		return pay(company,order,sysLocal);
	}
	
}
