package com.shenghesun.treasure.fund.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.order.service.FundDetailsService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.order.FundDetails;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;

@RestController
public class FundController {
	
	@Autowired
	SysUserService sysUserService;
	@Autowired
	CompanyMessageService companyMessageService;
	@Autowired
	FundDetailsService fundDetailsService;
	/**
	 * 查询资金明细
	 * @return
	 */
	@RequestMapping(value = "/fundDetail", method = RequestMethod.GET)
	public JSONObject completeCompanyMessage(Long id) {
		CompanyMessage company = null;
		try {
			SysUser user = sysUserService.findById(id);
			company = user.getCompanyMessage();
			return JsonUtil.getSuccessJSONObject(JSON.toJSONString(company));
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject("特殊错误");
		}
		
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
			fundDetails.setPrice(price);
			fundDetails.setPlusOrMinus("+");
			CompanyMessage company = companyMessageService.findById(id);
			if(company!=null) {
				company.setBalance(company.getBalance()==null?price:company.getBalance()+price);
				fundDetails.setCompanyMessage(company);
				fundDetailsService.save(fundDetails);
			}else {
				return JsonUtil.getFailJSONObject("公司不存在");
			}
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject("特殊错误");
		}
		return JsonUtil.getSuccessJSONObject();
	}
	
	
}
