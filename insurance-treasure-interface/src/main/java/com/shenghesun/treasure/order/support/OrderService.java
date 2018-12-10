package com.shenghesun.treasure.order.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.code.service.CodeListService;
import com.shenghesun.treasure.code.service.GoodsCodeService;
import com.shenghesun.treasure.code.service.TransCodeService;
import com.shenghesun.treasure.core.constant.OrderConstant;
import com.shenghesun.treasure.system.code.CodeList;
import com.shenghesun.treasure.system.code.GoodsCode;
import com.shenghesun.treasure.system.code.TransCode;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.RandomUtil;
import com.shenghesun.treasure.utils.RedisUtil;

@Service
public class OrderService {

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private CodeListService codeListService;
	@Autowired
	private TransCodeService transCodeService;
	@Autowired
	private GoodsCodeService goodsCodeService;
	/**
	 * 完善订单信息
	 * @param orderMessage
	 * @return
	 */
	public Map<String,Object> complete(OrderMessage orderMessage) {
		Map<String,Object> map =new HashMap<String,Object>();
		//校验表单数据
		checkCode(orderMessage,map);
		//设置公共投保信息
		orderMessage = this.all(orderMessage,map);
		//获取保险进出口类型
		if(orderMessage.getCity().equals(OrderConstant.INLAND)) {
			//完善国内投保数据
			orderMessage = this.inland(orderMessage);
		}else {
			//完善国际投保数据
			orderMessage = this.international(orderMessage);
		}
		map.put(OrderConstant.Order, orderMessage);
		return map;
	}
	/**
	 * 根据运输编号，获取运输工具、险种、主险条款代码等信息
	 * @param orderMessage
	 * @return
	 */
	public OrderMessage setTansport(OrderMessage orderMessage,Map<String,Object> map) {
		String t_code = orderMessage.getTransCode();
		TransCode transCode = null;
		if(redisUtil.exists(t_code)){
			String string = redisUtil.get(t_code);
			transCode = JSON.parseObject(string, TransCode.class);
		}else {
			transCode = transCodeService.findByTransCode(t_code);
			if(transCode==null) {
				map.put(OrderConstant.TRANS_ERROR, OrderConstant.TRANS_MESSAGE);
			}else {
				redisUtil.set(transCode.getTransCode(), transCode);
			}
		}
		if(transCode!=null) {
			orderMessage.setClassType(transCode.getClassType());
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
	public OrderMessage setGoods(OrderMessage orderMessage,Map<String,Object> map) {
		String g_code = orderMessage.getGoodsCode();
		GoodsCode goods = null;
		if(redisUtil.exists(g_code)){
			String string = redisUtil.get(g_code);
			goods = JSON.parseObject(string, GoodsCode.class);
		}else {
			GoodsCode goodsCode = goodsCodeService.findByGoodsCode(g_code);
			if(goodsCode==null) {
				map.put(OrderConstant.GOODS_ERROR, OrderConstant.GOODS_MESSAGE);
			}else {
				redisUtil.set(goodsCode.getGoodsCode(), goodsCode);
			}
		}
		if(goods!=null) {
			orderMessage.setItemCode(goods.getItemCode());
			orderMessage.setItem(goods.getItemName());
		}
		return orderMessage;
	}
	/**
	 * 完善公共投保数据
	 */
	public OrderMessage all(OrderMessage orderMessage,Map<String,Object> map) {
		//完善下单信息
		orderMessage.setPlusOrMinus(OrderConstant.FUND_OUT);
		//设置订单号
		if(orderMessage.getOrderNo()==null) {
			String orderNo = System.currentTimeMillis() + RandomUtil.randomNum(2);
			orderMessage.setOrderNo(orderNo);
		}
		//设置运输相关信息
		orderMessage = this.setTansport(orderMessage,map);
		//设置货物名称相关信息
		orderMessage = this.setGoods(orderMessage,map);
		//设置起保日期
		orderMessage.setEffectDate(orderMessage.getSaildate());
		return orderMessage;
	}
	/**
	 * 国内投保完善数据
	 */
	public OrderMessage inland(OrderMessage orderMessage) {
		//设置国内保险金额
		orderMessage.setOrderAmount(orderMessage.getGoodsValue());
		//设置保费
		float preminum = 0.01f*Float.parseFloat(orderMessage.getOrderAmount())*Float.parseFloat(orderMessage.getRate());
		if(preminum<5) {
			orderMessage.setPreminum("5");
		}else {
			orderMessage.setPreminum(Double.toString(preminum));
		}
		//设置发票金额
		orderMessage.setInvamount(orderMessage.getOrderAmount());
		orderMessage.setClassesType(OrderConstant.CLASSESTYPE_IN);
		return orderMessage;
	}
	/**
	 * 国际投保完善数据
	 */
	public OrderMessage international(OrderMessage orderMessage) {
		//设置国内保险金额和保费
		orderMessage.setOrderAmount(Float.toString((Float.parseFloat(orderMessage.getGoodsValue())*(1+Float.parseFloat(orderMessage.getIncrate())))));
		//设置保费
		float preminum = 0.01f*Float.parseFloat(orderMessage.getOrderAmount())*Float.parseFloat(orderMessage.getRate());
		if(preminum<2) {
			orderMessage.setPreminum("2");
		}else {
			orderMessage.setPreminum(Double.toString(preminum));
		}
		//设置发票金额 
		orderMessage.setInvamount(orderMessage.getOrderAmount());
		orderMessage.setClassesType(OrderConstant.CLASSESTYPE_OUT);
		orderMessage.setFlightareacode("EODE");
		orderMessage.setClaimagent("876264693");
		orderMessage.setPricecond("1");
		orderMessage.setClaimcurrencycode("01");
		orderMessage.setClaimpayplace("德国");
		return orderMessage;
	}
	/**
	 * 检验代码是否存在
	 * @return 
	 */
	public void checkCode(OrderMessage orderMessage,Map<String,Object> map) {
		String pack = orderMessage.getPackCode()+OrderConstant.PACKAGE_SUFFIX;
		String currency = orderMessage.getCurrencyCode()+OrderConstant.CURRENCY_SUFFIX;
		//代码如果不存在redis中，则去数据库中查找，查找到对象为空，则表示该代码不存在，返回代码不存在异常
		if(!redisUtil.exists(pack)){
			CodeList packCode = codeListService.findByKey(pack);
			if(packCode==null) {
				map.put(OrderConstant.PACK_ERROR, OrderConstant.PACK_MESSAGE);
			}else {
				redisUtil.set(packCode.getCodeKey(), packCode.getCodeValue());
			}
		}
		if(!redisUtil.exists(currency)){
			CodeList currCode = codeListService.findByKey(currency);
			if(currCode==null) {
				map.put(OrderConstant.CURRENCY_ERROR, OrderConstant.CURRENCY_MESSAGE);
			}else {
				redisUtil.set(currCode.getCodeKey(), currCode.getCodeValue());
			}
		}
		//验证订单费率是否小于最低费率
		if(Float.parseFloat(orderMessage.getRate())<0.02f) {
			map.put(OrderConstant.RATE_ERROR, OrderConstant.RATE_MESSAGE);
		}
		//验证货物价值是否小于0
		if(Float.parseFloat(orderMessage.getGoodsValue())<0) {
			map.put(OrderConstant.GOODSVALUE_ERROR, OrderConstant.GOODSVALUE_MESSAGE);
		}
	}
	
	/**
	 * 判断完善信息过程中是否出现运输代码查找错误和货物代码错误
	 * @return 
	 */
	public JSONObject check(Map<String,Object> orderMap) {
		if(orderMap.get(OrderConstant.TRANS_ERROR)!=null) {
			return JsonUtil.getFailJSONObject(orderMap.get(OrderConstant.TRANS_ERROR));
		}
		if(orderMap.get(OrderConstant.GOODS_ERROR)!=null) {
			return JsonUtil.getFailJSONObject(orderMap.get(OrderConstant.GOODS_ERROR));
		}
		if(orderMap.get(OrderConstant.PACK_ERROR)!=null) {
			return JsonUtil.getFailJSONObject(orderMap.get(OrderConstant.PACK_ERROR));
		}
		if(orderMap.get(OrderConstant.CURRENCY_ERROR)!=null) {
			return JsonUtil.getFailJSONObject(orderMap.get(OrderConstant.CURRENCY_ERROR));
		}
		if(orderMap.get(OrderConstant.RATE_ERROR)!=null) {
			return JsonUtil.getFailJSONObject(orderMap.get(OrderConstant.RATE_ERROR));
		}
		if(orderMap.get(OrderConstant.GOODSVALUE_ERROR)!=null) {
			return JsonUtil.getFailJSONObject(orderMap.get(OrderConstant.GOODSVALUE_ERROR));
		}
		return null;
	}

}
