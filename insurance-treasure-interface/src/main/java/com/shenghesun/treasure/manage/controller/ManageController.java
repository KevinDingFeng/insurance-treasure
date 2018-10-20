package com.shenghesun.treasure.manage.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.TokenUtil;

@RestController
@RequestMapping(value = "/api")
public class ManageController {

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public JSONObject index(HttpServletRequest req) {
		
		String token = HttpHeaderUtil.getToken((HttpServletRequest) req);
		String account = TokenUtil.getLoginUserAccount(token);
		//两种不同的获取方式
		Map<String, Object> map = TokenUtil.decode(token);
		Long userId = TokenUtil.getLoginUserId(map);
		System.out.println("user Id = " + userId);
		
		return JsonUtil.getSuccessJSONObject(account);
	}
}
