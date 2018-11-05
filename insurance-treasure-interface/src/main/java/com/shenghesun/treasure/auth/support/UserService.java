package com.shenghesun.treasure.auth.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.TokenUtil;

@Service
public class UserService {
	
	public Long getUser(HttpServletRequest request) {
		String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
		if(StringUtils.isEmpty(token)) {
			return 0l;
		}
		Map<String, Object> userInfoMap = TokenUtil.decode(token);
		Long id = (Long) userInfoMap.get("id");
		return id;
	}
}
