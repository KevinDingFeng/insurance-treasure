package com.shenghesun.treasure.utils;

import javax.servlet.http.HttpServletRequest;

public class HttpHeaderUtil {

	public static final String CUSTOM_TOKEN_NAME = "treasure_token";

	public static String getToken(HttpServletRequest req) {
		return req.getHeader(CUSTOM_TOKEN_NAME);
	}
}
