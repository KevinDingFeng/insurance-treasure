package com.shenghesun.treasure.auth.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;

@Service
public class LoginSuccessService {
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private CompanyMessageService companyService;
	/**
	 * 添加登陆成功后的返回信息
	 * @param returnMap
	 * @return
	 */
	public Map<String, Object> setReturnMessage(SysUser user) {
		Map<String, Object> returnMap = new HashMap<>();
		Long companyId = user.getCompanyId();
		//返回token
		String token = loginService.login(user.getId(), user.getAccount());
		//返回公司是否存在，存在时1  不存在是0
		if(companyId!=null) {
			CompanyMessage company = companyService.findById(companyId);
			Integer balance = company==null?0:company.getBalance();
			returnMap.put("balance", balance);
			returnMap.put("company", "1");
		}else {
			returnMap.put("balance", 0);
			returnMap.put("company", "0");
		}
		returnMap.put("token", token);
		return returnMap;
		
	}

	
}
