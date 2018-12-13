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
	 * 1.将联盟速运数据字典翻译成物流宝宝数据字典
	 * 2.翻译内容包括货物名称、运输方式、包装类型、币种代码
	 * @param order
	 * @return
	 */
	public OrderMessage unionComplete(OrderMessage order) {
		unionGoods(order);
		unionTrans(order);
		unionPackage(order);
		unionCurrency(order);
		return order;
	}
	/**
	 * 完善货物名称信息
	 */
	public void unionGoods(OrderMessage order) {
		if(redisUtil.exists(order.getFirstGoodsName())) {
			String string = redisUtil.getString(order.getFirstGoodsName());
			order.setGoodsCode(string);
		}else {
			order.setGoodsCode("600");
		}
	}
	/**
	 * 完善运输信息
	 */
	public void unionTrans(OrderMessage order) {
		if(redisUtil.exists(order.getTransCode())) {
			String string = redisUtil.getString(order.getTransCode());
			order.setTransCode(string);
		}else {
			UnionTransCode trans = unionTransCodeService.findByTransCode(order.getTransCode());
			if(trans!=null) {
				redisUtil.setString(trans.getUnionTranscode(), trans.getWBTranscode());
			}
		}
	}
	/**
	 * 完善包装信息
	 */
	public void unionPackage(OrderMessage order) {
		if(redisUtil.exists(order.getPackCode())) {
			String string = redisUtil.getString(order.getPackCode());
			order.setPackCode(string);
		}else {
			UnionPackageCode pack = unionPackageCodeService.findByPackageCode(order.getPackCode());
			if(pack!=null) {
				redisUtil.setString(pack.getUnionPackagecode(), pack.getWBPackagecode());
			}
		}
	}
	/**
	 * 完善币种信息
	 */
	public void unionCurrency(OrderMessage order) {
		if(redisUtil.exists(order.getCurrencyCode())){
			String string = redisUtil.getString(order.getCurrencyCode());
			order.setCurrencyCode(string);
		}else {
			redisUtil.setString("RMB", "01");
		}
	}
}
