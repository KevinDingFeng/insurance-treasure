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
import com.shenghesun.treasure.base.service.BaseBusinessService;
import com.shenghesun.treasure.base.service.BaseCityService;
import com.shenghesun.treasure.base.service.BaseDictionaryService;
import com.shenghesun.treasure.base.service.BaseGoodsService;
import com.shenghesun.treasure.base.service.BaseTransportService;
import com.shenghesun.treasure.system.dictionary.BaseBusiness;
import com.shenghesun.treasure.system.dictionary.BaseCity;
import com.shenghesun.treasure.system.dictionary.BaseDictionary;
import com.shenghesun.treasure.system.dictionary.BaseGoods;
import com.shenghesun.treasure.system.dictionary.BaseTransport;
import com.shenghesun.treasure.utils.JsonUtil;

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
	BaseDictionaryService baseDictionaryService;
	@Autowired
	BaseBusinessService baseBusinessService;
	@Autowired
	BaseCityService baseCityService;
	/**
	 * 页面加载获取全部基础数据字典
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public JSONObject getAll(String country) {
		Map<String, Object> map=new HashMap<>();
		try {
			//业务类型
			List<BaseBusiness> businessList = baseBusinessService.find();
			//币种
			List<BaseDictionary> currencyList = baseDictionaryService.findByCountryAndType(country, "1");
			//包装类型
			List<BaseDictionary> packList = baseDictionaryService.findByType("2");
			//城市
			List<BaseCity> cityList = baseCityService.find();
			map.put("business", businessList);
			map.put("currency", currencyList);
			map.put("pack", packList);
			map.put("city", cityList);
		} catch (Exception e) {
			log.error("base_all error");
			e.printStackTrace();
		}
		return JsonUtil.getSuccessJSONObject(JSON.toJSONString(map));
	}
	/**
	 * 根据业务类型获取一级运输方式和一级货物名称
	 */
	@RequestMapping(value = "/transportAndGoods", method = RequestMethod.GET)
	public JSONObject getTransport(String code,String country) {
		Map<String, Object> map=new HashMap<>();
		try {
			List<BaseTransport> transportList = baseTransportService.findByBusinessCodeAndCountry(code, country);
			List<BaseGoods> goodsList = baseGoodsService.findByParentCode(code);
			map.put("transportList", transportList);
			map.put("goodsList", goodsList);
		} catch (Exception e) {
			log.error("base_transport_goods error");
			e.printStackTrace();
		}
		return JsonUtil.getSuccessJSONObject(JSON.toJSONString(map));
	}
	/**
	 * 根据一级货物名称获取二级
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/secondGoods", method = RequestMethod.GET)
	public JSONObject getFirst(String code,String id) {
		List<BaseGoods> dictionaryList=null;
		try {
			dictionaryList = baseGoodsService.findByParentCodeAndParentId(code, id);
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject();
		}
		return JsonUtil.getSuccessJSONObject(JSON.toJSONString(dictionaryList));
	}
	
	/**
	 * 根据一级运输方式获取二级运输
	 */
	@RequestMapping(value = "/secondTransport", method = RequestMethod.GET)
	public JSONObject getSecTransport(String code,String parentCode) {
		List<BaseTransport> transportList=null;
		try {
			transportList = baseTransportService.findByBusinessCode(code,parentCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JsonUtil.getSuccessJSONObject(JSON.toJSONString(transportList));
	}



}
