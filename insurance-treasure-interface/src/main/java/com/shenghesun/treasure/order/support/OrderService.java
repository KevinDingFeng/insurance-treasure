package com.shenghesun.treasure.order.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.core.constant.InsuranceCompanyConstant;
import com.shenghesun.treasure.core.constant.OrderConstant;
import com.shenghesun.treasure.system.code.GoodsCode;
import com.shenghesun.treasure.system.code.TransCode;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.utils.DateUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.RandomUtil;
import com.shenghesun.treasure.utils.RedisUtil;

@Service
public class OrderService {

	@Autowired
	private RedisUtil redisUtil;
	
	/**
	 * 	@Title
	 *  @param orderMessage
	 *  @param companyMessage
	 *  @return Map<String,Object>
	 *  @author zdd
	 *	@date 2018年12月13日上午11:58:01
	 *  @Description 翻译完善表单信息
	 *  			1.检验表单数据正确性
	 *  			2.设置公共的保单信息
	 *  			3.根据条件设置国内相关投保数据或者国外投保相关数据
	 */
	public Map<String,Object> complete(OrderMessage orderMessage,CompanyMessage companyMessage) {
		Map<String,Object> map =new HashMap<String,Object>();
		//校验表单数据
		checkCode(orderMessage,map);
		//设置公共投保信息
		this.setPublic(orderMessage,map,companyMessage);
		//获取保险进出口类型
		if(orderMessage.getCity().equals(OrderConstant.INLAND)) {
			//完善国内投保数据
			this.inland(orderMessage);
		}else {
			//完善国际投保数据
			this.international(orderMessage);
		}
		return map;
	}
	/**
	 * 	@Title
	 *  @param orderMessage
	 *  @param map void
	 *  @author zdd
	 *	@date 2018年12月13日下午12:11:31
	 *  @Description 设置运输相关信息
	 *  			   根据运输编号，获取运输工具、险种、主险条款代码等信息
	 */
	public void setTansport(OrderMessage orderMessage,Map<String,Object> map) {
		String trans = orderMessage.getTransCode();
		TransCode transCode = null;
		if(redisUtil.exists(trans)){
			transCode = JSON.parseObject(redisUtil.get(trans), TransCode.class);
		}
		if(transCode!=null) {
			orderMessage.setClassType(transCode.getClassType());
			orderMessage.setKind(transCode.getKind());
			orderMessage.setKindName(transCode.getKindName());
			orderMessage.setItemContent(transCode.getItemContent());
			orderMessage.setMainItemCode(transCode.getItemCode());
		}
	}
	/**
	 * 	@Title
	 *  @param orderMessage
	 *  @param map
	 *  @return OrderMessage
	 *  @author zdd
	 *	@date 2018年12月13日下午12:13:43
	 *  @Description 设置货物名称相关信息
	 *  			 
	 */
	public void setGoods(OrderMessage orderMessage,Map<String,Object> map) {
		String goodsCode = orderMessage.getGoodsCode();
		GoodsCode goods = null;
		if(redisUtil.exists(goodsCode)){
			goods = JSON.parseObject(redisUtil.get(goodsCode), GoodsCode.class);
		}
		if(goods!=null) {
			orderMessage.setItemCode(goods.getItemCode());
			orderMessage.setItem(goods.getItemName());
		}
	}
	/**
	 * 	@Title
	 *  @param orderMessage
	 *  @param map
	 *  @param companyMessage void
	 *  @author zdd
	 *	@date 2018年12月13日上午11:59:46
	 *  @Description 设置公共投保数据
	 *  			1.设置账户出帐标识
	 *  			2.设置运输相关翻译信息
	 *  			3.设置货物名称翻译信息
	 *  			4.设置起保日期
	 *  			5.设置订单号
	 */
	public void setPublic(OrderMessage orderMessage,Map<String,Object> map,CompanyMessage companyMessage) {
		//完善下单信息
		orderMessage.setPlusOrMinus(OrderConstant.FUND_OUT);
		//设置运输相关信息
		this.setTansport(orderMessage,map);
		//设置货物名称相关信息
		this.setGoods(orderMessage,map);
		//设置起保日期
		orderMessage.setEffectDate(orderMessage.getSaildate());
		if(orderMessage.getOrderNo()==null) {
			setPolicyOrderNo(orderMessage,companyMessage.getCustomerNo());
		}
	}
	/**
	 * 	@Title
	 *  @param orderMessage void
	 *  @author zdd
	 *	@date 2018年12月13日下午1:56:40
	 *  @Description 国内投保完善数据
	 *  			1.设置保险金额
	 *  			2.设置保费
	 *  			3.设置发票金额
	 *  			4.设置险类为国内
	 */
	public void inland(OrderMessage orderMessage) {
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
	}
	/**
	 * 	@Title
	 *  @param orderMessage void
	 *  @author zdd
	 *	@date 2018年12月13日下午1:57:43
	 *  @Description 国际投保完善数据
	 *  			1.设置保险金额
	 *  			2.设置保费
	 *  			3.设置发票金额
	 *  			4.设置险类为国际
	 *  			5.设置航行区域代码
	 *  			6.设置理赔代理地编码
	 *  			7.设置价格条件
	 *  			8.设置赔款偿付地代码
	 *  			9.设置赔款偿付地
	 */
	public void international(OrderMessage orderMessage) {
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
	}
	/**
	 * 	@Title
	 *  @param orderMessage
	 *  @param map void
	 *  @author zdd
	 *	@date 2018年12月13日下午2:10:00
	 *  @Description 1.检验包装代码和币种代码是否存在，不存在返回异常信息
	 *  			 2.检验费率是否低于最小费率
	 *  			 3.检验货物价值是否小于0
	 */
	public void checkCode(OrderMessage orderMessage,Map<String,Object> map) {
		String pack = orderMessage.getPackCode()+OrderConstant.PACKAGE_SUFFIX;
		String currency = orderMessage.getCurrencyCode()+OrderConstant.CURRENCY_SUFFIX;
		//代码如果不存在redis中，返回代码不存在异常
		if(!redisUtil.exists(pack)){
			map.put(OrderConstant.PACK_ERROR, OrderConstant.PACK_MESSAGE);
		}
		if(!redisUtil.exists(currency)){
			map.put(OrderConstant.CURRENCY_ERROR, OrderConstant.CURRENCY_MESSAGE);
		}
		//验证订单费率是否小于最低费率
		if(Float.parseFloat(orderMessage.getRate())<OrderConstant.MIN_RATE) {
			map.put(OrderConstant.RATE_ERROR, OrderConstant.RATE_MESSAGE);
		}
		//验证货物价值是否小于0
		if(Float.parseFloat(orderMessage.getGoodsValue())<0) {
			map.put(OrderConstant.GOODSVALUE_ERROR, OrderConstant.GOODSVALUE_MESSAGE);
		}
	}
	/**
	 * 	@Title
	 *  @param orderMap
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午2:04:12
	 *  @Description 判断完善信息过程中是否出现运输代码查找错误和货物代码错误
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
	
	/**
	 * 	@Title
	 *  @param orderMessage
	 *  @param customerNo void
	 *  @author zdd
	 *	@date 2018年12月13日下午2:02:16
	 *  @Description 设置订单号，订单号生成规则
	 *  			险种代码+对接保险公司编号+客户编号+日期+8位随机数字+1位随机英文字母
	 */
	public void setPolicyOrderNo(OrderMessage orderMessage,String customerNo) {
		//设置订单号
		//获取险种代码
		String classCode = RandomUtil.getClassCode(orderMessage.getTransCode());
		//获取8位数字随机码
		String num = RandomUtil.randomNum(8);
		//获取1位大写英文码
		String upString = RandomUtil.randomUpString(1);
		//获取当前年月
		String date = DateUtil.getDate();
		String orderNo = classCode+InsuranceCompanyConstant.COMPANY1+customerNo+date+num+upString;
		orderMessage.setOrderNo(orderNo);
	}
}
