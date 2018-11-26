package com.shenghesun.treasure.utils;

import java.util.Map;

public class TokenUtil {
	
	/**
	 * 获取包含有登录用户 id 和 用户名的 token
	 * @param userId
	 * @param account
	 * @return
	 */
	public static String create(Long userId, String account,Long companyId) {
		return JWTUtil.create(userId + "", account,companyId + "", System.currentTimeMillis());
	}
	
	/**
	 * 解析 token，可以获取到 token 中包含的用户信息
	 * @param token
	 * @return
	 */
	public static Map<String, Object> decode(String token){
		return JWTUtil.decode(token);
	}
	
	/**
	 * 解析 token 中的 登录用户的 id 
	 * @param token
	 * @return
	 */
	public static Long getLoginUserId(String token) {
		Map<String, Object> map = decode(token);
		return getLoginUserId(map);
	}
	
	public static Long getLoginUserId(Map<String, Object> map) {
		return Long.parseLong((String)map.get(JWTUtil.ID_KEY));
	}

	/**
	 * 解析 token 中的 登录用户的 account 
	 * @param token
	 * @return
	 */
	public static String getLoginUserAccount(String token) {
		Map<String, Object> map = decode(token);
		return getLoginUserAccount(map);
	}
	
	public static String getLoginUserAccount(Map<String, Object> map) {
		return (String)map.get(JWTUtil.ACCOUNT_KEY);
	}
	/**
	 * 解析token中登陆用户的companyId
	 */
	public static Long getLoginCompanyId(String token) {
		Map<String, Object> map = decode(token);
		return getLoginCompanyId(map);
	}
	
	public static Long getLoginCompanyId(Map<String, Object> map) {
		return Long.parseLong((String)map.get(JWTUtil.COMPANY_KEY));
	}
}
