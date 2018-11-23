package com.shenghesun.treasure.order.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.core.constant.Presentation;
import com.shenghesun.treasure.cpic.service.AsyncService;
import com.shenghesun.treasure.order.model.OrderCondition;
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
	 * 系统提前使用，用户获取token后直接进行使用
	 * @param request
	 * @param orderMessage
	 * @return
	 */
	@RequestMapping(value = "/approvl", method = RequestMethod.POST)
	public JSONObject order(HttpServletRequest request,@Validated OrderMessage order) {
		try {
			JSONObject insurance = insurance(request,order);
			return insurance;
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
	/**
	 * 投保
	 */
	public JSONObject insurance(HttpServletRequest request,@Validated OrderMessage order) {
		//获取公司信息
		String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
		Long companyId = TokenUtil.getLoginCompanyId(token);
		Long userId = TokenUtil.getLoginUserId(token);
		CompanyMessage company = companyService.findById(companyId);
		log.info("投保用户ID："+userId+"投保公司ID: "+companyId);
		//完善订单信息
		Map<String,Object> orderMap = orderService.complete(request,order);
		order = (OrderMessage) orderMap.get("order");
		//判断完善信息过程中是否出现运输代码查找错误和货物代码错误
		if(orderMap.get("trans_error")!=null) {
			log.error("运输代码不存在");
			return JsonUtil.getFailJSONObject(orderMap.get("trans_error"));
		}
		if(orderMap.get("goods_error")!=null) {
			log.error("货物代码不存在");
			return JsonUtil.getFailJSONObject(orderMap.get("goods_error"));
		}
		if(orderMap.get("pack_error")!=null) {
			return JsonUtil.getFailJSONObject(orderMap.get("pack_error"));
		}
		if(orderMap.get("currency_error")!=null) {
			return JsonUtil.getFailJSONObject(orderMap.get("currency_error"));
		}
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
				orderMessageService.save(order);
				return JsonUtil.getFailJSONObject("余额不足，请联系管理员充值");
			}
		}else {
			return JsonUtil.getFailJSONObject("公司不存在或订单不存在");
		}
		return JsonUtil.getSuccessJSONObject(map);
	}
	
	////////////////////////////////////////////////////////////////
	
	/**
	 * @Title: form 
	 * @Description: 保单详情 
	 * @param orderNo
	 * @return  JSONObject 
	 * @author yangzp
	 * @date 2018年11月20日下午3:54:40
	 **/ 
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public JSONObject form(String orderNo) {
		OrderMessage orderMessage = orderMessageService.findByOrderNo(orderNo);
		return JsonUtil.getSuccessJSONObject(orderMessage);
	}
	
	/**
	 * @Title: list 
	 * @Description: 按照条件查询我的保单 
	 * @param request
	 * @param pageable
	 * @param condition
	 * @return  JSONObject 
	 * @author yangzp
	 * @date 2018年11月20日下午4:34:15
	 **/ 
	@RequestMapping(value = "/list", method = {RequestMethod.POST})
	public JSONObject list(HttpServletRequest request,
			@PageableDefault(page = 0, value = Presentation.DEFAULT_PAGE_SIZE, sort = {
					Presentation.DEFAULT_ORDER_FIELD }, direction = Direction.DESC) Pageable pageable,
			OrderCondition condition) {
		try {
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long id = TokenUtil.getLoginUserId(token);
			condition.setUserId(id);
			Page<OrderMessage> page = orderMessageService.findByConditions(condition, pageable);
			return JsonUtil.getSuccessJSONObject(page);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
	
}
