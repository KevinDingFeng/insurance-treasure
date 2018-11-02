package com.shenghesun.treasure.order.controller;

import java.util.Iterator;
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
	public JSONObject saveOrder(OrderMessage orderMessage) {
		try {
			Long userid = orderMessage.getUserid();
			SysUser user=null;
			if(userid!=null) {
				user = sysUserService.findById(userid);
			}
			if(user!=null) {
				orderMessage.setSysUser(user);
				orderMessageService.save(orderMessage);
			}else {
				return JsonUtil.getFailJSONObject("用户不存在");
			}
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
	@RequestMapping(value = "/showOrder", method = RequestMethod.GET)
	public JSONObject getOrder(Long id) {
		Set<OrderMessage> set;
		try {
			SysUser user = sysUserService.findById(id);
			return JsonUtil.getSuccessJSONObject(JSON.toJSONString(user));
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject("特殊错误");
		}
	
	}
}
