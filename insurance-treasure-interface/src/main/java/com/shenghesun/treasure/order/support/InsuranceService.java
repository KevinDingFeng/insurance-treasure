package com.shenghesun.treasure.order.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.service.CompanyMessageService;
import com.shenghesun.treasure.core.constant.OrderConstant;
import com.shenghesun.treasure.cpic.service.AsyncService;
import com.shenghesun.treasure.external.service.ExternalOrderService;
import com.shenghesun.treasure.order.service.OrderMessageService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.cpic.ReturnApprovl;
import com.shenghesun.treasure.system.order.OrderMessage;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.TokenUtil;

@Service
public class InsuranceService {
	@Autowired
	AsyncService asyncService;
	@Autowired
	CompanyMessageService companyService;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderMessageService orderMessageService;
	@Autowired
	ExternalOrderService externalOrderService;
	/*@Autowired
	private SmsCodeService smsCodeService;*/
	/**
	 * 支付成功通知模板code
	 */
	/*@Value("${sms.template.code}")
	private String templateCode;*/
	/**
	 * 1.投保前完善保单信息
	 * 2.检验提交表单中的数据是否正确，验证失败直接返回错误信息，验证通过后进行支付
	 */
	public JSONObject insurance(String token,@Validated OrderMessage order,CompanyMessage company,String comFrom) {
		//获取投保用户
		String account = TokenUtil.getLoginUserAccount(token);
		//完善订单信息
		order.setUserId(TokenUtil.getLoginUserId(token));
		order.setCompanyId(TokenUtil.getLoginCompanyId(token));
		Map<String,Object> orderMap = orderService.complete(order);
		order = (OrderMessage) orderMap.get(OrderConstant.Order);
		//判断完善信息过程中是否出现运输代码查找错误和货物代码错误
		JSONObject check = orderService.check(orderMap);
		if(check!=null) {
			return check;
		}
		//支付扣款并且进行投保
		return pay(company,order,comFrom,account);
	}
	/**
	 * 1.判断公司信息和办单信息是否为空，不为空进行支付操作
	 * 2.获取公司余额，判断是否大于保单的保费，如果余额不足则返回消息，并保存订单数据，便于日后进行支付操作
	 * 			余额充足，更新下保单后的剩余金额，保存公司信息。
	 * 3.修改订单支付状态，保存订单信息
	 * 4.支付完成后进行投保，根据接口访问的方式，区分为内部系统访问(需要异步进行投保)和外部系统访问(需要同步进行投保返回数据)，
	 * 5.内部系统异步投保后进行短信通知
	 */
	public JSONObject pay(CompanyMessage company,OrderMessage order,String comFrom,String account) {
		if(company!=null&&order!=null) {
			//保费
			Double preminum = Double.parseDouble(order.getPreminum());
			//如果余额大于保单金额,才进行支付扣款
			if(company.getBalance()>=preminum) {
				company.setBalance(company.getBalance()-preminum);
				companyService.save(company);
				//修改订单状态
				order.setPayStatus(1);
				orderMessageService.save(order);
				//调用投保
				if(comFrom.equals(OrderConstant.SYS_LOCAL)) {
					//异步调用
					asyncService.executeAsync(order);
					//发送成功短信
					//String smsStatus = smsCodeService.sendSms(account, "伟林易航",templateCode,"");
					//log.info("订单号为:"+order.getOrderNo()+";手机号为："+account+"的订单成功短信通知" + smsStatus);
				}else {
					//同步调用
					ReturnApprovl returnApprovl = externalOrderService.executeApprovl(order);
					return JsonUtil.getSuccessJSONObject(returnApprovl);
				}
			}else {
				orderMessageService.save(order);
				JSONObject json = JsonUtil.getFailJSONObject(order.getOrderNo());
				json.put("msg", "余额不足，请联系管理员充值");
				return json;
			}
			
		}else {
			return JsonUtil.getFailJSONObject("公司不存在或订单不存在");
		}
		return JsonUtil.getSuccessJSONObject(order.getOrderNo());
	}
}
