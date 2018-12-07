package com.shenghesun.treasure.union.controller.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.system.union.code.UnionPackageCode;
import com.shenghesun.treasure.system.union.code.UnionTransCode;
import com.shenghesun.treasure.union.service.UnionPackageCodeService;
import com.shenghesun.treasure.union.service.UnionTransCodeService;
import com.shenghesun.treasure.utils.RedisUtil;

@Service
public class UnionOrderService {

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private UnionTransCodeService unionTransCodeService;
	@Autowired
	private UnionPackageCodeService unionPackageCodeService;
	/**
	 * 完善订单信息方法入口
	 * @param order
	 * @return
	 */
	public OrderMessage unionComplete(OrderMessage order) {
		order = unionGoods(order);
		order = unionTrans(order);
		order = unionPackage(order);
		order = unionCurrency(order);
		return order;
	}
	/**
	 * 完善货物名称信息
	 */
	public OrderMessage unionGoods(OrderMessage order) {
		if(redisUtil.exists(order.getFirstGoodsName())) {
			String string = redisUtil.get(order.getFirstGoodsName());
			order.setGoodsCode(string);
		}else {
			order.setGoodsCode("600");
		}
		return order;
	}
	/**
	 * 完善运输信息
	 */
	public OrderMessage unionTrans(OrderMessage order) {
		if(redisUtil.exists(order.getTransCode())) {
			String string = redisUtil.get(order.getTransCode());
			order.setTransCode(string);
		}else {
			UnionTransCode trans = unionTransCodeService.findByTransCode(order.getTransCode());
			if(trans!=null) {
				redisUtil.setString(trans.getUnionTranscode(), trans.getWBTranscode());
			}
		}
		return order;
	}
	/**
	 * 完善包装信息
	 */
	public OrderMessage unionPackage(OrderMessage order) {
		if(redisUtil.exists(order.getPackCode())) {
			String string = redisUtil.get(order.getPackCode());
			order.setPackCode(string);
		}else {
			UnionPackageCode pack = unionPackageCodeService.findByPackageCode(order.getPackCode());
			if(pack!=null) {
				redisUtil.setString(pack.getUnionPackagecode(), pack.getWBPackagecode());
			}
		}
		return order;
	}
	/**
	 * 完善币种信息
	 */
	public OrderMessage unionCurrency(OrderMessage order) {
		if(redisUtil.exists(order.getCurrencyCode())){
			String string = redisUtil.get(order.getCurrencyCode());
			order.setCurrencyCode(string);
		}else {
			redisUtil.setString("RMB", "01");
		}
		return order;
	}
}
