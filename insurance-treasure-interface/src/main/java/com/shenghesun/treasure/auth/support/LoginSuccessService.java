package com.shenghesun.treasure.auth.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.company.service.CompanyMessageService;
import com.shenghesun.treasure.core.constant.BaseConstant;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;

@Service
public class LoginSuccessService {
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private CompanyMessageService companyService;
	/**
	 * 	@Title
	 *  @param user
	 *  @return Map<String,Object>
	 *  @author zdd
	 *	@date 2018年12月13日下午2:36:44
	 *  @Description 添加登陆成功后的返回信息
	 *  			1.返回账户余额
	 *  			2.返回公司编码
	 *  			3.返回token
	 */
	public Map<String, Object> setReturnMessage(SysUser user) {
		Map<String, Object> returnMap = new HashMap<>();
		Long companyId = user.getCompanyId();
		//生成token
		String token = loginService.login(user.getId(), user.getAccount(),user.getCompanyId()==null?0:user.getCompanyId());
		//返回公司是否存在，存在时1  不存在是0
		if(companyId!=null) {
			CompanyMessage company = companyService.findById(companyId);
			returnMap.put("balance", company.getBalance());
			returnMap.put("company", companyId);
		}else {
			returnMap.put("balance", BaseConstant.ZERO);
			returnMap.put("company", BaseConstant.ZERO);
		}
		returnMap.put("token", token);
		return returnMap;
	}

	
}
