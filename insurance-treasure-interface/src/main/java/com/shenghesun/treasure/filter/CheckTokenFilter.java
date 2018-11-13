package com.shenghesun.treasure.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.util.StringUtils;
import com.shenghesun.treasure.config.CustomConfig;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JWTUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.RedisUtil;
import com.shenghesun.treasure.utils.TokenUtil;

public class CheckTokenFilter implements Filter{
	
	@Autowired
	private RedisUtil redisUtil;
 
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
 
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(request instanceof HttpServletRequest) {
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			if(StringUtils.isEmpty(token)) {
				this.setReturnResponse((HttpServletResponse) response, "invalid token");
				return;
			}
			Map<String, Object> userInfoMap=null;
			try {
				userInfoMap = TokenUtil.decode(token);
				if(userInfoMap.get(JWTUtil.ERR_MSG) != null) {
					this.setReturnResponse((HttpServletResponse) response, "invalid token");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String userInfoId = redisUtil.get(CustomConfig.REDIS_TOKEN_PREFIX + token);
			//TODO 如果有必要，可以再加更严谨的校验方式，比如解析 token 获取到的数据和数据库进行匹配
			if(StringUtils.isEmpty(userInfoId)) {
				this.setReturnResponse((HttpServletResponse) response, "invalid token");
				return;
			}
			//更新有效时长
			redisUtil.set(CustomConfig.REDIS_TOKEN_PREFIX + token, userInfoId, CustomConfig.EXPIRE_TIME_SECOND);// 缓存token 到 redis ，使用配置中的时长
			chain.doFilter(request, response);
		}else {
			return;
		}
	}
	private void setReturnResponse(HttpServletResponse response, String message) {
		response.setCharacterEncoding("UTF-8");  
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null ;
		try{
		    out = response.getWriter();
		    out.append(JsonUtil.getFailJSONObject(message).toJSONString());
		}
		catch (Exception e){
		    e.printStackTrace();
		    try {
				response.sendError(500);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
 
	@Override
	public void destroy() {
		
	}
 
	


}
