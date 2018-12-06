package com.shenghesun.treasure.order.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.service.CompanyMessageService;
import com.shenghesun.treasure.core.constant.OrderConstant;
import com.shenghesun.treasure.core.constant.Presentation;
import com.shenghesun.treasure.core.constant.TokenConstant;
import com.shenghesun.treasure.cpic.service.ApprovlService;
import com.shenghesun.treasure.cpic.service.AsyncService;
import com.shenghesun.treasure.order.model.OrderCondition;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.order.support.InsuranceService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.model.OrderShow;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.system.service.SysUserTypeService;
import com.shenghesun.treasure.union.controller.support.UnionOrderService;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.RedisUtil;
import com.shenghesun.treasure.utils.TokenUtil;

import lombok.extern.slf4j.Slf4j;


@RestController()
@RequestMapping("/api/order")
@Slf4j
public class OrderController {
	
	@Autowired
	SysUserService sysUserService;
	@Autowired
	AsyncService asyncService;
	@Autowired
	OrderMessageService orderMessageService;
	@Autowired
	InsuranceService insuranceService;
	@Autowired
	UnionOrderService unionOrderService;
	@Autowired
	SysUserTypeService sysUserTypeService;
	@Autowired
	ApprovlService approvlService;
	@Autowired
	private CompanyMessageService companyMessageService;
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 内部投保接口，用户获取token后直接进行使用
	 * @param request
	 * @param orderMessage
	 * @return
	 */
	@RequestMapping(value = "/approvl", method = RequestMethod.POST)
	public JSONObject save(HttpServletRequest request,@Validated OrderMessage order) {
		System.out.println(order);
		try {
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request); 
			Map<String, Object> map = TokenUtil.decode(token);
			Long companyId = Long.parseLong(map.get(TokenConstant.COMPANY_KEY).toString());
			CompanyMessage company = companyMessageService.findById(companyId);
			return insuranceService.insurance(map,order,company,OrderConstant.SYS_LOCAL);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
	
	/**
	 * 设置允许自动绑定的属性名称
	 * 
	 * @param binder
	 * @param req
	 */
	@InitBinder("order")
	private void initBinder(ServletRequestDataBinder binder, HttpServletRequest req) {
		List<String> fields = new ArrayList<String>(Arrays.asList("city", "firstGoodsName", "goodsValue", "currencyCode", "packCode"
				,"transCode","startPort","endPort","saildate","mark","applyName","insurantName","rate","incrate","goodsCode"));
		switch (req.getMethod()) {
		case "POST": // 新增
			binder.setAllowedFields(fields.toArray(new String[fields.size()]));
			break;
		case "PUT": // 修改
			binder.setAllowedFields(fields.toArray(new String[fields.size()]));
			break;
		default:
			break;
		}
	}
	
	/**
	 * 预处理，一般用于新增和修改表单提交后的预处理
	 * 
	 * @param id
	 * @param req
	 * @return
	 */
	@ModelAttribute("order")
	public OrderMessage prepare(@RequestParam(value = Presentation.KEY_ID, required = false)Long id,HttpServletRequest req) {
		if (id != null && id > 0) {// 修改表单提交后数据绑定之前执行
			OrderMessage orderMessage = orderMessageService.findById(id);
			return orderMessage;
		}else {
			OrderMessage orderMessage = new OrderMessage();
			return orderMessage;
		}
	}
	
	/**
	 * 订单支付
	 * @param request
	 * @param orderNo
	 * @return
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public JSONObject pay(HttpServletRequest request,String orderNo) {
		try {
			return insuranceService.completePay(request,orderNo,OrderConstant.SYS_LOCAL);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
	
	/**
	 * @Title: form 
	 * @Description: 保单详情 
	 * @param orderNo
	 * @return  JSONObject 
	 * @author yangzp
	 * @date 2018年11月20日下午3:54:40
	 **/ 
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public JSONObject form(String orderNo) {
		OrderShow orderShow=null;
		try {
			OrderMessage orderMessage = orderMessageService.findByOrderNo(orderNo);
			orderShow = new OrderShow();
			BeanUtils.copyProperties(orderMessage, orderShow);
			if(redisUtil.exists(orderShow.getCurrencyCode()+OrderConstant.CURRENCY_SUFFIX)) {
				orderShow.setCurrencyName(redisUtil.get(orderShow.getCurrencyCode()+OrderConstant.CURRENCY_SUFFIX).toString());
			}
			if(redisUtil.exists(orderShow.getPackCode()+OrderConstant.PACKAGE_SUFFIX)) {
				orderShow.setPackageType(redisUtil.get(orderShow.getPackCode()+OrderConstant.PACKAGE_SUFFIX).toString());
			}
		} catch (BeansException e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();		
		}
		return JsonUtil.getSuccessJSONObject(orderShow);
	}
	
	/**
	 * @Title: list 
	 * @Description: 按照条件查询我的保单 
	 * @param request
	 * @param pageable
	 * @param condition
	 * @return  JSONObject 
	 * @author yangzp
	 * @date 2018年11月20日下午4:34:15
	 **/ 
	@RequestMapping(value = "/list", method = {RequestMethod.POST})
	public JSONObject list(HttpServletRequest request,
			@PageableDefault(page = 0, value = Presentation.DEFAULT_PAGE_SIZE, sort = {
					Presentation.DEFAULT_ORDER_FIELD }, direction = Direction.DESC) Pageable pageable,
			OrderCondition condition) {
		try {
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long id = TokenUtil.getLoginUserId(token);
			condition.setUserId(id);
			Page<OrderMessage> page = orderMessageService.findByConditions(condition, pageable);
			return JsonUtil.getSuccessJSONObject(page);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
}
