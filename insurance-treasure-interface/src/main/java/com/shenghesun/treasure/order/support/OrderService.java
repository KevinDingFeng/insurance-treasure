package com.shenghesun.treasure.order.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.shenghesun.treasure.system.code.GoodsCode;
import com.shenghesun.treasure.system.code.TransCode;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.RandomUtil;
import com.shenghesun.treasure.utils.RedisUtil;
import com.shenghesun.treasure.utils.TokenUtil;

@Service
public class OrderService {
	
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 完善订单信息
	 * @param orderMessage
	 * @return
	 */
	public OrderMessage complete(HttpServletRequest request,OrderMessage orderMessage) {
		//获取用户信息
		String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
		Long userId = TokenUtil.getLoginUserId(token);
		Long companyId = TokenUtil.getLoginCompanyId(token);
		//完善下单信息
		orderMessage.setUserId(userId);
		orderMessage.setCompanyId(companyId);
		orderMessage.setPlusOrMinus("0");
		//设置订单号
		String orderNo = System.currentTimeMillis() + RandomUtil.randomString(2);
		orderMessage.setOrderNo(orderNo);
		//设置运输相关信息
		orderMessage = this.setTansport(orderMessage);
		//设置货物名称相关信息
		orderMessage = this.setGoods(orderMessage);
		//设置起保日期
		orderMessage.setEffectDate(orderMessage.getSaildate());
		//获取保险进出口类型
		String city = orderMessage.getCity();
		if(city.equals("0")) {
			//设置国内保险金额和保费
			orderMessage.setOrderAmount(orderMessage.getGoodsValue());
			//设置发票金额
			orderMessage.setInvamount(orderMessage.getOrderAmount());
			//设置类型
			orderMessage.setClassType("1");
			//设置保费
			float preminum = orderMessage.getOrderAmount()*Float.parseFloat(orderMessage.getRate());
			orderMessage.setPreminum((int)Math.ceil(preminum));
		}else {
			//设置国内保险金额和保费
			orderMessage.setOrderAmount((orderMessage.getGoodsValue())*(1+orderMessage.getIncrate()));
			//设置发票金额
			orderMessage.setInvamount(orderMessage.getOrderAmount());
			//设置类型
			orderMessage.setClassType("0");
			//设置保费
			float preminum = orderMessage.getOrderAmount()*Float.parseFloat(orderMessage.getRate());
			orderMessage.setPreminum((int)Math.ceil(preminum));
		}
		return orderMessage;
	}
	/**
	 * 根据运输编号，获取运输工具、险种、主险条款代码等信息
	 * @param orderMessage
	 * @return
	 */
	public OrderMessage setTansport(OrderMessage orderMessage) {
		String t_code = orderMessage.getTransCode();
		TransCode transCode = null;
		if(redisUtil.exists(t_code)){
			String string = redisUtil.get(t_code);
			transCode = JSON.parseObject(string, TransCode.class);
		}
		if(transCode!=null) {
			orderMessage.setClassesType(transCode.getClassType());
			orderMessage.setKind(transCode.getKind());
			orderMessage.setKindName(transCode.getKindName());
			orderMessage.setItemContent(transCode.getItemContent());
			orderMessage.setMainItemCode(transCode.getItemCode());
		}
		return orderMessage;
	}
	/**
	 * 设置货物代码
	 * @param orderMessage
	 * @return
	 */
	public OrderMessage setGoods(OrderMessage orderMessage) {
		String g_code = orderMessage.getGoodsCode();
		GoodsCode goods = null;
		if(redisUtil.exists(g_code)){
			String string = redisUtil.get(g_code);
			goods = JSON.parseObject(string, GoodsCode.class);
		}
		if(goods!=null) {
			orderMessage.setItemCode(goods.getItemCode());
			orderMessage.setItem(goods.getItemName());
		}
		return orderMessage;
	}
}
