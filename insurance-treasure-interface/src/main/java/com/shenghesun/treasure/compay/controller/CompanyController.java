package com.shenghesun.treasure.compay.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.service.CompanyMessageService;
import com.shenghesun.treasure.compay.support.CompanyService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping(value="/api/company")
@Slf4j
public class CompanyController {

	@Autowired
	CompanyMessageService companyService;
	@Autowired
	SysUserService sysUserService;

	@Autowired
	private CompanyService company;
	/**
	 * 完善公司信息
	 * @return
	 */
	@PostMapping(value = "/complete")
	public JSONObject completeCompanyMessage(HttpServletRequest request,@Validated CompanyMessage companyMessage,@RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			return company.complte(request,file,companyMessage);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
	
	/**
	 * 查看公司信息
	 */
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public JSONObject findCompany(HttpServletRequest request) {
		CompanyMessage company = null;
		try {
			//获取请求用户信息
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long companyId = TokenUtil.getLoginCompanyId(token);
			//查看公司信息
			company = companyService.findById(companyId);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
		return JsonUtil.getSuccessJSONObject(company);
	}
}
