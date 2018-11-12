package com.shenghesun.treasure.base.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.base.service.BaseCityService;
import com.shenghesun.treasure.base.service.BaseGoodsService;
import com.shenghesun.treasure.base.service.BaseTransportService;
import com.shenghesun.treasure.system.code.TransCode;
import com.shenghesun.treasure.system.dictionary.BaseCity;
import com.shenghesun.treasure.system.dictionary.BaseGoods;
import com.shenghesun.treasure.system.dictionary.BaseTransport;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping(value="/base")
@Slf4j
public class BaseDictionaryController {

	@Autowired
	BaseGoodsService baseGoodsService;
	@Autowired
	BaseTransportService baseTransportService;
	@Autowired
	BaseCityService baseCityService;
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 页面加载获取全部基础数据字典
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public JSONObject getAll(String country) {
		Map<String, Object> map=new HashMap<>();
		try {
			//业务类型
			List<BaseGoods> businessList = baseGoodsService.findByParentCode("-1");
			//城市
			List<BaseCity> cityList = baseCityService.find();
			//一级运输方式
			List<BaseTransport> transList = baseTransportService.findByBusinessCodeAndParentCode(country,"-1");
			map.put("business", businessList);
			map.put("city", cityList);
			map.put("trans", transList);
		} catch (Exception e) {
			log.error("base_all error");
			e.printStackTrace();
		}
		return JsonUtil.getSuccessJSONObject(JSON.toJSONString(map));
	}
	/**
	 * 获取货物名称
	 */
	@RequestMapping(value = "/goods", method = RequestMethod.GET)
	public JSONObject getTransport(String code,String country) {
		List<BaseGoods> goodsList=null;
		try {
			goodsList = baseGoodsService.findByParentCode(code);
		} catch (Exception e) {
			log.error("base_firstGoods error");
			e.printStackTrace();
		}
		return JsonUtil.getSuccessJSONObject(JSON.toJSONString(goodsList));
	}
	/**
	 * 根据一级货物名称获取二级
	 * @param type
	 * @return
	 */

	/**
	 * 获取运输方式
	 */
	@RequestMapping(value = "/transport", method = RequestMethod.GET)
	public JSONObject getransport(String code,String business) {
		List<BaseTransport> transportList=null;
		try {
			transportList = baseTransportService.findByBusinessCodeAndParentCode(business,code);
		} catch (Exception e) {
			log.error("base_secondTransport error");
			return JsonUtil.getFailJSONObject();
		}
		return JsonUtil.getSuccessJSONObject(JSON.toJSONString(transportList));
	}
	/**
	 * 根据运输方式代码获取保险条款
	 */
	@RequestMapping(value = "/getItem", method = RequestMethod.GET)
	public JSONObject getInsurance(String business,String transport) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			TransCode transCode = null;
			String itemName = null;
			String rate = null;
			if(redisUtil.exists(transport)){
				String string = redisUtil.get(transport);
				transCode = JSON.parseObject(string, TransCode.class);
			}
			if(redisUtil.exists(business+"rate")){
				rate = redisUtil.get(business+"rate");
				System.out.println(rate);
			}
			if(transCode!=null) {
				itemName = transCode.getItemName();
			}
			map.put("itemName", itemName);
			map.put("rate", rate);
			return JsonUtil.getSuccessJSONObject(JSON.toJSONString(map));
		} catch (Exception e) {
			log.error("base_secondTransport error");
			return JsonUtil.getFailJSONObject();
		}
		
	}
}
