package com.shenghesun.treasure.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shenghesun.treasure.test.service.TestCacheService;

/**
 * 测试入口 测试 cache 的使用
 * 
 * @author 程任强
 *
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {
	
	@Autowired
	private TestCacheService testCacheService;

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
	
}
