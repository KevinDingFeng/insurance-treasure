package com.shenghesun.treasure.order.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;

@RestController
public class OrderController {

	@Autowired
	OrderMessageService orderMessageService;
	@Autowired
	SysUserService sysUserService;
	
	/**
	 * 下单
	 * @return
	 */
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public JSONObject completeCompanyMessage(OrderMessage orderMessage) {
		SysUser user = sysUserService.findById(orderMessage.getUserid());
		orderMessage.setSysUser(user);
		orderMessageService.save(orderMessage);
		return JsonUtil.getSuccessJSONObject();
	}
	/**
	 * 我的保单
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/ShowOrder", method = RequestMethod.GET)
	public JSONObject getOrder(Long id) {
		SysUser findById = sysUserService.findById(id);
		//Set<OrderMessage> set = findById.getOrderMessage();
		return JsonUtil.getSuccessJSONObject(JSON.toJSONString(""));
	}
}
