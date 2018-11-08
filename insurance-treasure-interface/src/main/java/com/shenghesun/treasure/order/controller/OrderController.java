package com.shenghesun.treasure.order.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.order.support.OrderService;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.TokenUtil;

@RestController()
@RequestMapping("/order")
public class OrderController {

	@Autowired
	OrderMessageService orderMessageService;
	@Autowired
	OrderService orderService;
	@Autowired
	SysUserService sysUserService;
	/**
	 * 下单
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public JSONObject saveOrder(HttpServletRequest request,OrderMessage orderMessage) {
		try {
			//获取用户信息
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long userId = TokenUtil.getLoginUserId(token);
			//完善下单信息
			orderMessage.setUserid(userId);
			orderMessage = orderService.complete(orderMessage);
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
	public JSONObject getOrder(HttpServletRequest request) {
		String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
		try {
			Long id = TokenUtil.getLoginUserId(token);
			SysUser user = sysUserService.findById(id);
			return JsonUtil.getSuccessJSONObject(JSON.toJSONString(user));
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject("特殊错误");
		}
	}
}
