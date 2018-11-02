package com.shenghesun.treasure.base.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.base.service.BaseDictionaryService;
import com.shenghesun.treasure.system.dictionary.BaseDictionary;
import com.shenghesun.treasure.utils.JsonUtil;

@RestController()
@RequestMapping(value="/base")
public class BaseDictionaryController {

	@Autowired
	BaseDictionaryService baseDictionaryService;
	
	/**
	 * 获取数据字典目录
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/dictionary", method = RequestMethod.GET)
	public JSONObject getDictionary() {
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		List<BaseDictionary> dictionaryList=null;
		try {
			dictionaryList = baseDictionaryService.findByType(list);
		} catch (Exception e) {
			JsonUtil.getFailJSONObject("特殊错误");
		}
		return JsonUtil.getSuccessJSONObject(JSON.toJSONString(dictionaryList));
	}
	/**
	 * 获取二级目录信息
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/first", method = RequestMethod.GET)
	public JSONObject getFirst(String parentCode) {
		List<BaseDictionary> dictionaryList=null;
		try {
			dictionaryList = baseDictionaryService.findByParentCode(parentCode);
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject();
		}
		return JsonUtil.getSuccessJSONObject(JSON.toJSONString(dictionaryList));
	}
	/**
	 * 获取二级目录信息
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/second", method = RequestMethod.GET)
	public JSONObject getSecond(String parentId) {
		List<BaseDictionary> dictionaryList=null;
		try {
			dictionaryList = baseDictionaryService.findByParentId(parentId);
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject();
		}
		return JsonUtil.getSuccessJSONObject(JSON.toJSONString(dictionaryList));
	}
}
