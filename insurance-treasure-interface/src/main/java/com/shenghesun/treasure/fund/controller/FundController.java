package com.shenghesun.treasure.fund.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.service.CompanyMessageService;
import com.shenghesun.treasure.core.constant.BaseConstant;
import com.shenghesun.treasure.core.constant.OrderConstant;
import com.shenghesun.treasure.core.constant.Presentation;
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
	 * 	@Title
	 *  @param request
	 *  @param page
	 *  @param size
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午3:32:50
	 *  @Description 查询资金明细
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public JSONObject completeCompanyMessage(HttpServletRequest request,@RequestParam(value="page", defaultValue=BaseConstant.ZERO) Integer page,
			@RequestParam(value="size", defaultValue= Presentation.PAGE_SIZE) Integer size) {
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
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
	
	/**
	 * 	@Title
	 *  @param request
	 *  @param fundDetails
	 *  @return JSONObject
	 *  @author zdd
	 *	@date 2018年12月13日下午3:31:26
	 *  @Description 管理用户增加公司余额
	 *  			 1.设置资金入账标识
	 *  			 2.设置充值人id
	 *  			 3.查找公司更新公司余额
	 */
	@RequestMapping(value = "/balance", method = RequestMethod.POST)
	public JSONObject updateBalance(HttpServletRequest request,FundDetails fundDetails) {
		try {
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long userId = TokenUtil.getLoginUserId(token);
			fundDetails.setPlusOrMinus(OrderConstant.FUND_IN);
			fundDetails.setUserId(userId);
			CompanyMessage company = companyMessageService.findById(fundDetails.getCompanyId());
			if(company!=null) {
				company.setBalance(company.getBalance()+fundDetails.getPreminum());
				fundDetailsService.save(fundDetails);
			}else {
				return JsonUtil.getFailJSONObject(BaseConstant.COMPANY_ERROR);
			}
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
		return JsonUtil.getSuccessJSONObject();
	}
	
	
}
