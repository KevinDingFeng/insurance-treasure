package com.shenghesun.treasure.union.controller.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.shenghesun.treasure.core.constant.OrderConstant;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.system.union.code.UnionGoodsCode;
import com.shenghesun.treasure.system.union.code.UnionPackageCode;
import com.shenghesun.treasure.system.union.code.UnionTransCode;
import com.shenghesun.treasure.union.core.constant.UnionBaseConstant;
import com.shenghesun.treasure.union.service.UnionGoodsCodeService;
import com.shenghesun.treasure.union.service.UnionPackageCodeService;
import com.shenghesun.treasure.union.service.UnionTransCodeService;
import com.shenghesun.treasure.utils.RedisUtil;

@Service
public class UnionOrderService {

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private UnionGoodsCodeService unionGoodsCodeService;
	@Autowired
	private UnionTransCodeService unionTransCodeService;
	@Autowired
	private UnionPackageCodeService unionPackageCodeService;
	/**
	 * 完善订单信息方法入口
	 * @param order
	 * @return
	 */
	public OrderMessage union_complete(OrderMessage order) {
		order = union_goods(order);
		order = union_trans(order);
		order = union_package(order);
		order = union_currency(order);
		return order;
	}
	/**
	 * 完善货物名称信息
	 */
	public OrderMessage union_goods(OrderMessage order) {
		String goodsName = "union"+order.getFirstGoodsName();
		UnionGoodsCode goodsCode = null;
		if(redisUtil.exists(goodsName)) {
			String string = redisUtil.get(goodsName);
			goodsCode = JSON.parseObject(string,UnionGoodsCode.class);
		}else {
			UnionGoodsCode unionGoodsCode = unionGoodsCodeService.findByGoodsName(order.getFirstGoodsName());
			if(unionGoodsCode==null) {
			}else {
				redisUtil.set("union"+unionGoodsCode.getUnionGoodsName(), unionGoodsCode);
			}
		}
		if(goodsCode!=null) {
			order.setGoodsCode(goodsCode.getWBGoodsCode());
		}else {
			order.setGoodsCode(UnionBaseConstant.BASE_GOODS);
		}
		return order;
	}
	/**
	 * 完善运输信息
	 */
	public OrderMessage union_trans(OrderMessage order) {
		String transCode = "union"+order.getTransCode();
		UnionTransCode unionTransCode = null;
		if(redisUtil.exists(transCode)) {
			String string = redisUtil.get(transCode);
			unionTransCode = JSON.parseObject(string,UnionTransCode.class);
		}else {
			UnionTransCode trans = unionTransCodeService.findByTransCode(order.getTransCode());
			if(trans!=null) {
				redisUtil.set("union"+trans.getUnionTranscode(), trans);
			}
		}
		if(unionTransCode!=null) {
			order.setTransCode(unionTransCode.getWBTranscode());
		}
		return order;
	}
	/**
	 * 完善包装信息
	 */
	public OrderMessage union_package(OrderMessage order) {
		String packageCode = "union"+order.getPackCode();
		UnionPackageCode unionPackageCode = null;
		if(redisUtil.exists(packageCode)) {
			String string = redisUtil.get(packageCode);
			unionPackageCode = JSON.parseObject(string,UnionPackageCode.class);
		}else {
			UnionPackageCode pack = unionPackageCodeService.findByPackageCode(order.getPackCode());
			if(pack!=null) {
				redisUtil.set("union"+pack.getUnionPackagecode(), pack);
			}
		}
		if(unionPackageCode!=null) {
			order.setPackCode(unionPackageCode.getWBPackagecode());
		}
		return order;
	}
	/**
	 * 完善币种信息
	 */
	public OrderMessage union_currency(OrderMessage order) {
		String currencyCode = "union"+order.getCurrencyCode();
		String unionCurrencyCode = null;
		if(redisUtil.exists(currencyCode)) {
			unionCurrencyCode = redisUtil.get(currencyCode).replaceAll("\"", "");
		}else {
			redisUtil.set("unionRMB", "01");
		}
		if(unionCurrencyCode!=null) {
			order.setCurrencyCode(unionCurrencyCode);
		}
		return order;
	}
}
