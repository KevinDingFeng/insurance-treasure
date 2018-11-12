package com.shenghesun.treasure.pay.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.cpic.service.AsyncService;
import com.shenghesun.treasure.order.service.FundDetailsService;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api")
public class PayController {
	
	@Autowired
	SysUserService sysUserService;
	@Autowired
	CompanyMessageService companyService;
	@Autowired
	OrderMessageService orderMessageService;
	@Autowired
	FundDetailsService fundDetailsService;
	@Autowired
	private AsyncService asyncService;
	/**
	 * 支付保单
	 * @return
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public JSONObject pay(HttpServletRequest request,String orderNo) {
		CompanyMessage company = null;
		try {
			//获取用户id
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long userId = TokenUtil.getLoginUserId(token);
			//查找订单所属用户
			SysUser user = sysUserService.findById(userId);
			//根据订单id查找到订单信息
			OrderMessage order = orderMessageService.findByOrderNo(orderNo);
			if(user!=null) {
				Long companyId = user.getCompanyId();
				company = companyService.findById(companyId);
			}
			if(company!=null&&order!=null) {
				//保单金额
				Integer price = order.getOrderAmount();
				//如果余额大于保单金额,才进行支付扣款
				if(company.getBalance()>order.getOrderAmount()) {
					company.setBalance(company.getBalance()-price);
					//修改订单状态
					order.setPayStatus(1);
					orderMessageService.save(order);
					asyncService.executeAsync(order);
				}else {
					return JsonUtil.getFailJSONObject("余额不足，请联系管理员充值");
				}
			}else {
				return JsonUtil.getFailJSONObject("公司不存在或订单不存在");
			}
			return JsonUtil.getSuccessJSONObject();
		} catch (Exception e) {
			log.error("pay error");
			return JsonUtil.getFailJSONObject();
		}
		
	}
}
