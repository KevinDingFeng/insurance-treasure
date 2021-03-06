package com.shenghesun.treasure.utils;

import com.alibaba.fastjson.JSONObject;

public class JsonUtil {
	//TODO 可以自定义一个配置类，用来定义 code、message、data 等字面量，使用 static final 关键字修饰

	/**
	 * 获取一个成功的json对象
	 * @return
	 */
	public static JSONObject getSuccessJSONObject() {
		JSONObject json = new JSONObject();
		json.put("code", "200");
		json.put("message", "success");
		return json;
	}
	/**
	 * 获取一个成功的包括数据data的json对象
	 * @param data
	 * @return
	 */
	public static JSONObject getSuccessJSONObject(Object data) {
		JSONObject json = getSuccessJSONObject();
		json.put("data", data);
		return json;
	}
	
	/**
	 * 获取一个失败的json对象
	 * @return
	 */
	public static JSONObject getFailJSONObject() {
		JSONObject json = new JSONObject();
		json.put("code", "400");
		json.put("message", "fail");
		return json;
	}
	
	/**
	 * 获取一个失败的json对象
	 * @return
	 */
	public static JSONObject getFailJSONObject(Object data) {
		JSONObject json = getFailJSONObject();
		json.put("data", data);
		return json;
	}
	
	/**
	 * 获取一个失败的json对象
	 * @return
	 */
	public static JSONObject getFailJSONObject(Object data,String msg) {
		JSONObject json = getFailJSONObject(data);
		json.put("msg", msg);
		return json;
	}
	/**
	 * tokenFilter返回数据
	 * @return
	 */
	public static JSONObject getFailJSONToken(Object obj) {
		JSONObject json = new JSONObject();
		json.put("code", "-1");
		json.put("message", obj);
		return json;
	}
	/**
	 * jwt失效，token验证失败后返回新的token
	 */
	public static JSONObject getFailToken(Object obj,Object data) {
		JSONObject json = new JSONObject();
		json.put("code", "-2");
		json.put("data", data);
		json.put("message", obj);
		return json;
	}
}
