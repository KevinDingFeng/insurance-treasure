package com.shenghesun.treasure.order.external.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.service.CompanyMessageService;
import com.shenghesun.treasure.core.constant.InterfaceConstant;
import com.shenghesun.treasure.core.constant.OrderConstant;
import com.shenghesun.treasure.cpic.service.ApprovlService;
import com.shenghesun.treasure.cpic.service.AsyncService;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.order.support.InsuranceService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.dto.OrderDto;
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
public class ExternalOrderController {

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
	CompanyMessageService companyMessageService;
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 外部投保接口，用户获取token后直接进行使用
	 * @param request
	 * @param orderMessage
	 * @return
	 */
	@RequestMapping(value = "/insure", method = RequestMethod.POST)
	public JSONObject order(HttpServletRequest request,@Validated OrderDto orderDto) {
		try {
			//解析token获取用户信息
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request); 
			CompanyMessage company = companyMessageService.findById(TokenUtil.getLoginCompanyId(token));
			float preminum = 0.01f*Float.parseFloat(orderDto.getGoodsValue())*Float.parseFloat(orderDto.getRate());
			if(company.getBalance()<preminum) {
				return JsonUtil.getFailJSONObject("账户余额不足，请联系管理员充值");
			}
			//对外接口 用户类型
			String type = null;
			OrderMessage order = null;
			if(redisUtil.exists(TokenUtil.getLoginUserAccount(token))){
				type = redisUtil.get(TokenUtil.getLoginUserAccount(token));
			}
			if(type!=null) {
				//Dto对象转投保实体对象
				order = new OrderMessage();
				BeanUtils.copyProperties(orderDto, order);
				//根据不同类型，选择不同的翻译方式
				order = translationCode(type,order);
			}else {
				return JsonUtil.getFailJSONObject("无权访问该接口，请联系相关人员处理");
			}
			return insuranceService.insurance(token,order,company,OrderConstant.SYS_OUT);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
	}
	/**
	 * 根据类型分支走不同的数据处理
	 * @param type
	 * @param order
	 * @return
	 */
	private OrderMessage translationCode(String type,OrderMessage order) {
		switch(type) {
			case InterfaceConstant.TYPE:
				return order = unionOrderService.unionComplete(order);
			 default:
				return null;
		}	
	}
}
