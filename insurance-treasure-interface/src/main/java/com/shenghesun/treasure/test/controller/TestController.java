package com.shenghesun.treasure.test.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.cpic.service.AsyncService;
import com.shenghesun.treasure.system.code.GoodsCode;
import com.shenghesun.treasure.system.model.FundShow;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.test.service.TestCacheService;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.RedisUtil;

/**
 * 测试入口 测试 cache 的使用
 * 
 * @author 程任强
 *
 */
@RestController
@RequestMapping(value = "/api")
public class TestController {
	
	@Autowired
	private TestCacheService testCacheService;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private RedisUtil redisUtil;

	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public String find(@RequestParam(value = "key") String key) {
		return testCacheService.findByKey(key);
	}

	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public String insert(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value) {
		return testCacheService.insertByKey(key, value);
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value) {
		return testCacheService.updateByKey(key, value);
	}

	@RequestMapping(value = "/removed", method = RequestMethod.GET)
	public String removed(@RequestParam(value = "key") String key) {
		testCacheService.removedByKey(key);
		return "OK";
	}
	
	@RequestMapping(value = "/insurance", method = RequestMethod.GET)
	public String insu(OrderMessage orderMessage) {		
		asyncService.executeAsync(orderMessage);
		return "OK";
	}
	
	@RequestMapping(value = "/redis", method = RequestMethod.GET)
	public String redis(String code) {		
		/*if(redisUtil.exists(code)){
			String string = redisUtil.get(code);
			System.out.println(string);
			//TransCode tc = JSON.parseObject(string, TransCode.class);
		}*/
		GoodsCode goods = null;
		if(redisUtil.exists(code)){
			String string = redisUtil.get(code);
			goods = JSON.parseObject(string, GoodsCode.class);
		}
		return "OK";
	}

	
}
