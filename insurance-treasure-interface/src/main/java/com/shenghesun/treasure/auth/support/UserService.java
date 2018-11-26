package com.shenghesun.treasure.auth.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.company.service.CompanyMessageService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.TokenUtil;

@Service
public class UserService {

	@Autowired
	private static SysUserService sysUserService;
	@Autowired
	private static CompanyMessageService companyService;
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public SysUser getUser(HttpServletRequest request) {
		String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
		Long userId = TokenUtil.getLoginUserId(token);
		return sysUserService.findById(userId);
	}
	/**
	 * 获取公司信息
	 */
	public CompanyMessage getCompany(HttpServletRequest request) {
		String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
		Long companyId = TokenUtil.getLoginCompanyId(token);
		return companyService.findById(companyId);
	}
}
