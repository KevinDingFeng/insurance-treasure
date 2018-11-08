package com.shenghesun.treasure.order.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.shenghesun.treasure.system.code.GoodsCode;
import com.shenghesun.treasure.system.code.TransCode;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.utils.RedisUtil;

@Service
public class OrderService {
	
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 完善订单信息
	 * @param orderMessage
	 * @return
	 */
	public OrderMessage complete(OrderMessage orderMessage) {
		//设置运输相关信息
		orderMessage = this.setTansport(orderMessage);
		//设置货物名称相关信息
		orderMessage = this.setGoods(orderMessage);
		
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
			orderMessage.setClasstype(transCode.getClassType());
			orderMessage.setClassestype(transCode.getClassType());
			orderMessage.setKind(transCode.getKind());
			orderMessage.setKindname(transCode.getKindName());
			orderMessage.setItemcontent(transCode.getItemContent());
			orderMessage.setItemcode(transCode.getItemCode());
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
			orderMessage.setItemcode(goods.getItemCode());
			orderMessage.setItem(goods.getItemName());
		}
		return orderMessage;
	}
}
