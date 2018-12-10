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
	 * 1.解析token获取用户和公司信息
	 * 2.计算订单的保费与公司的余额进行比较，余额不足直接返回信息，不进行订单数据处理
	 * 3.获取用户的登陆类型，根据用户的不同类型选择不同的数据翻译
	 * 4.用户登陆类型不存在，返回无权访问接口信息，存在时对订单信息进行翻译
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
			//Dto对象转投保实体对象
			order = new OrderMessage();
			BeanUtils.copyProperties(orderDto, order);
			if(type!=null) {
				//根据不同类型，选择不同的翻译方式
				order = translationCode(type,order);
			}/*else {
				//return JsonUtil.getFailJSONObject("无权访问该接口，请联系相关人员处理");
			}*/
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
			case InterfaceConstant.TYPE1:
				return order = unionOrderService.unionComplete(order);
			case InterfaceConstant.TYPE2:
				return order;
			 default:
				return null;
		}	
	}
}
