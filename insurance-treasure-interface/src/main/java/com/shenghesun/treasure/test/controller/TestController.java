package com.shenghesun.treasure.test.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.order.service.FundDetailsService;
import com.shenghesun.treasure.system.entity.SysRole;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.model.FundShow;
import com.shenghesun.treasure.system.order.FundDetails;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.test.service.TestCacheService;
import com.shenghesun.treasure.utils.JsonUtil;

/**
 * 测试入口 测试 cache 的使用
 * 
 * @author 程任强
 *
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {
	
	@Autowired
	private TestCacheService testCacheService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private FundDetailsService fundDetailsService;

	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public String find(@RequestParam(value = "key") String key) {
		return testCacheService.findByKey(key);
	}

	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public String insert(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value) {
		return testCacheService.insertByKey(key, value);
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value) {
		return testCacheService.updateByKey(key, value);
	}

	@RequestMapping(value = "/removed", method = RequestMethod.GET)
	public String removed(@RequestParam(value = "key") String key) {
		testCacheService.removedByKey(key);
		return "OK";
	}
	
	@RequestMapping(value = "/findFund", method = RequestMethod.GET)
	public JSONObject removed() {
		DozerBeanMapper mapper = new DozerBeanMapper();
		List<FundDetails> list = fundDetailsService.findByCompanyId("4");
		List<FundShow> fundShowList = null;
		for(int i=0;i<list.size();i++) {
			FundShow fundShow = mapper.map(list.get(i), FundShow.class);
			fundShowList.add(fundShow);
		}
		return JsonUtil.getSuccessJSONObject(fundShowList);
	}
	
	
	@RequestMapping(value = "/base", method = RequestMethod.GET)
	public JSONObject base(String account) {
		SysUser user = sysUserService.findByAccount(account);
		Set<SysRole> roles = user.getRoles();
		if (roles != null && roles.size() > 0) {
			Iterator<SysRole> roleIts = roles.iterator();
			JSONObject rolesObj = null;
			while (roleIts.hasNext()) {
				if (rolesObj == null) {
					rolesObj = new JSONObject();
				}
/*				rolesObj.put(role.getName(), role.getId());// 添加角色信息
				Set<SysPermission> perms = role.getPermissions();
				Iterator<SysPermission> permIts = perms.iterator();
				while (permIts.hasNext()) {
					if (permsObj == null) {
						permsObj = new JSONObject();
					}
					SysPermission sysPermission = permIts.next();
					permsObj.put(sysPermission.getPerm(), sysPermission.getId());// 添加权限信息
				}*/
			}
		
		//List<BaseDictionary> findById = baseDictionaryService.findByParentCode("21");
		//管理员更新资金详情
	/*	SysUser user = sysUserService.findById(2l);
		//user.setBalance(180);
		FundDetails fd = new FundDetails();
		fd.setPrice(180);
		fd.setPlusOrMinus("+");
		//fd.setSysUser(user);
		fundDetailsService.save(fd);*/
		//查看资金明细
		//List<FundDetails> fundDetails = user.getFundDetails();
		//String string = fundDetails.toString();
		
		}
		return JsonUtil.getSuccessJSONObject();
	}
	
}
