package com.shenghesun.treasure.order.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.cpic.service.AsyncService;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.order.support.OrderService;
import com.shenghesun.treasure.system.company.CompanyMessage;
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
	public JSONObject saveOrder(HttpServletRequest request,OrderMessage order) {
		try {
			Map<String,Object> orderMessage = orderService.complete(request,order);
			//保存下单信息
			orderMessageService.save((OrderMessage)orderMessage.get("order"));
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
			return JsonUtil.getSuccessJSONObject(orderList);
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
	public JSONObject order(HttpServletRequest request,@Validated OrderMessage order) {
		try {
			//获取公司信息
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long companyId = TokenUtil.getLoginCompanyId(token);
			CompanyMessage company = companyService.findById(companyId);
			//完善订单信息
			Map<String,Object> orderMap = orderService.complete(request,order);
			order = (OrderMessage) orderMap.get("order");
			//判断完善信息过程中是否出现运输代码查找错误和货物代码错误
			if(orderMap.get("trans_error")!=null) {
				return JsonUtil.getFailJSONObject(orderMap.get("trans_error"));
			}
			if(orderMap.get("goods_error")!=null) {
				return JsonUtil.getFailJSONObject(orderMap.get("goods_error"));
			}
			//保存下单信息
			orderMessageService.save(order);
			Map<String, Object> map =null;
			if(company!=null&&order!=null) {
				//保费
				Double preminum = Double.parseDouble(order.getPreminum());
				//如果余额大于保单金额,才进行支付扣款
				if(company.getBalance()>=preminum) {
					company.setBalance(company.getBalance()-preminum);
					orderMessageService.save(order);
					map = asyncService.executeAsync(order);
					//修改订单状态
					order.setPayStatus(1);
				}else {
					return JsonUtil.getFailJSONObject("余额不足，请联系管理员充值");
				}
			}else {
				return JsonUtil.getFailJSONObject("公司不存在或订单不存在");
			}
			return JsonUtil.getSuccessJSONObject(map);
		} catch (Exception e) {
			log.error("pay error");
			return JsonUtil.getFailJSONObject();
		}
	}
}
