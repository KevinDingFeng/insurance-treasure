package com.shenghesun.treasure.fund.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.order.service.FundDetailsService;
import com.shenghesun.treasure.order.service.FundShowService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.model.FundShow;
import com.shenghesun.treasure.system.order.FundDetails;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping(value="/api/fund")
@Slf4j
public class FundController {
	
	@Autowired
	SysUserService sysUserService;
	@Autowired
	CompanyMessageService companyMessageService;
	@Autowired
	FundDetailsService fundDetailsService;
	@Autowired
	FundShowService fundShowService;
	/**
	 * 查询资金明细
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public JSONObject completeCompanyMessage(HttpServletRequest request,Integer page,Integer size) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			//获取请求用户公司信息
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long companyId = TokenUtil.getLoginCompanyId(token);
			Integer totalElements = fundShowService.findCount(companyId);
			Integer totalPages = (int) Math.ceil(totalElements/size);
			//根据公司查询资金明细
			List<FundShow> fundList = fundShowService.findByCompanyId(companyId,page,size);
			map.put("totalElements", totalElements);
			map.put("totalPages", totalPages);
			map.put("fundList", fundList);
			return JsonUtil.getSuccessJSONObject(map);
		} catch (Exception e) {
			log.error("fund_detail error");
			return JsonUtil.getFailJSONObject();
		}
		
		
	}
	
	/**
	 * 管理用户增加公司余额
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/balance", method = RequestMethod.POST)
	public JSONObject updateBalance(HttpServletRequest request,FundDetails fundDetails) {
		try {
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long userId = TokenUtil.getLoginUserId(token);
			fundDetails.setPlusOrMinus("1");
			fundDetails.setUserId(userId);
			CompanyMessage company = companyMessageService.findById(fundDetails.getCompanyId());
			if(company!=null) {
				company.setBalance(company.getBalance()==null?fundDetails.getOrderAmount():company.getBalance()+fundDetails.getOrderAmount());
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
