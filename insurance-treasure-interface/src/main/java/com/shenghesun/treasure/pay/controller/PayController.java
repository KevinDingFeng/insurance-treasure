package com.shenghesun.treasure.pay.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.auth.support.UserService;
import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.order.service.FundDetailsService;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.order.FundDetails;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;

@RestController
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
	UserService userService;
	/**
	 * 支付保单
	 * @return
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public JSONObject pay(HttpServletRequest request,Long orderId) {
		CompanyMessage company = null;
		FundDetails fundDetails = new FundDetails();
		try {
			Long userId = userService.getUser(request);
			//查找订单所属用户
			SysUser user = sysUserService.findById(userId);
			//根据订单id查找到订单信息
			OrderMessage order = orderMessageService.findById(orderId);
			if(user!=null) {
				Long companyId = user.getCompanyId();
				company = companyService.findById(companyId);
			}
			if(company!=null&&order!=null) {
				//保单金额
				Integer price = order.getOrderAmount();
				//如果余额大于保单金额,才进行支付扣款
				if((company.getBalance()==null?0:company.getBalance())>order.getOrderAmount()) {
					fundDetails.setOrderAmount(price);
					fundDetails.setPlusOrMinus("0");
					//fundDetails.setOrderMessage(order);
					company.setBalance(company.getBalance()==null?price:company.getBalance()-price);
				}else {
					return JsonUtil.getFailJSONObject("余额不足，请联系管理员充值");
				}
				//修改订单状态
				order.setPayStatus(1);
				fundDetailsService.save(fundDetails);
			}else {
				return JsonUtil.getFailJSONObject("公司不存在或订单不存在");
			}
			return JsonUtil.getSuccessJSONObject(JSON.toJSONString(company));
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject("特殊错误");
		}
		
	}
}
