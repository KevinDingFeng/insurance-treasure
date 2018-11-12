package com.shenghesun.treasure.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.cpic.service.AsyncService;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.order.support.OrderService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.TokenUtil;

import lombok.extern.slf4j.Slf4j;


@RestController()
@RequestMapping("/api/order")
@Slf4j
public class OrderController {

	@Autowired
	OrderMessageService orderMessageService;
	@Autowired
	OrderService orderService;
	@Autowired
	SysUserService sysUserService;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	CompanyMessageService companyService;
	/**
	 * 下单
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public JSONObject saveOrder(HttpServletRequest request,OrderMessage orderMessage) {
		try {
			orderMessage = orderService.complete(request,orderMessage);
			//保存下单信息
			orderMessageService.save(orderMessage);
			return JsonUtil.getSuccessJSONObject();
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject("特殊错误");
		}
		
	}
	/**
	 * 我的保单
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public JSONObject getOrder(HttpServletRequest request,Integer page,Integer size) {
		String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
		try {
			Long id = TokenUtil.getLoginUserId(token);
			PageRequest pageRequest = new PageRequest(page, size);
			Page<OrderMessage> orderList = orderMessageService.findByUserId(id,pageRequest);
			return JsonUtil.getSuccessJSONObject(JSON.toJSONString(orderList));
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject("特殊错误");
		}
	}
	/**
	 * 系统提前使用，用户获取token后直接进行使用
	 * @param request
	 * @param orderMessage
	 * @return
	 */
	@RequestMapping(value = "/order", method = RequestMethod.POST)
	public JSONObject order(HttpServletRequest request,OrderMessage order) {
		
		CompanyMessage company = null;
		try {
			//获取用户id
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long userId = TokenUtil.getLoginUserId(token);
			Long companyId = TokenUtil.getLoginCompanyId(token);
			//查找订单所属用户
			SysUser user = sysUserService.findById(userId);
			order = orderService.complete(request,order);
			//保存下单信息
			orderMessageService.save(order);
				company = companyService.findById(companyId);
			
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
