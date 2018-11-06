package com.shenghesun.treasure.fund.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.auth.support.UserService;
import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.order.service.FundDetailsService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.order.FundDetails;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping(value="/fund")
@Slf4j
public class FundController {
	
	@Autowired
	SysUserService sysUserService;
	@Autowired
	UserService userService;
	@Autowired
	CompanyMessageService companyMessageService;
	@Autowired
	FundDetailsService fundDetailsService;
	/**
	 * 查询资金明细
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public JSONObject completeCompanyMessage(HttpServletRequest request) {
		try {
			Long id = userService.getUser(request);
			SysUser user = sysUserService.findById(id);
			Long companyId =null;
			if(user!=null) {
				companyId = user.getCompanyId();
			}else {
				return JsonUtil.getFailJSONObject();
			}
			if(companyId!=null) {
				//List<FundDetails> fundDetails = fundDetailsService.findByCompanyId(companyId);
			}
			
			
		} catch (Exception e) {
			log.error("fund_detail error");
			return JsonUtil.getFailJSONObject();
		}
		return null;
		
	}
	
	/**
	 * 管理用户增加公司余额
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public JSONObject updateBalance(Long id,Integer price) {
		FundDetails fundDetails = new FundDetails();
		try {
			fundDetails.setOrderAmount(price);
			fundDetails.setPlusOrMinus("1");
			CompanyMessage company = companyMessageService.findById(id);
			if(company!=null) {
				company.setBalance(company.getBalance()==null?price:company.getBalance()+price);
				//fundDetails.setCompanyMessage(company);
				fundDetailsService.save(fundDetails);
			}else {
				return JsonUtil.getFailJSONObject("公司不存在");
			}
		} catch (Exception e) {
			log.error("manager update balance error");
			return JsonUtil.getFailJSONObject();
		}
		return JsonUtil.getSuccessJSONObject();
	}
	
	
}
