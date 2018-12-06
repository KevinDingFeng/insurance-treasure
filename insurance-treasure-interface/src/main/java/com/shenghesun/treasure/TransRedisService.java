package com.shenghesun.treasure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.shenghesun.treasure.base.service.BaseCityService;
import com.shenghesun.treasure.code.service.CodeListService;
import com.shenghesun.treasure.code.service.GoodsCodeService;
import com.shenghesun.treasure.code.service.TransCodeService;
import com.shenghesun.treasure.system.code.CodeList;
import com.shenghesun.treasure.system.code.GoodsCode;
import com.shenghesun.treasure.system.code.TransCode;
import com.shenghesun.treasure.system.dictionary.BaseCity;
import com.shenghesun.treasure.system.entity.SysUserType;
import com.shenghesun.treasure.system.service.SysUserTypeService;
import com.shenghesun.treasure.utils.RedisUtil;

import lombok.extern.slf4j.Slf4j;


@Component
@Order(value = 1)
@Slf4j
public class TransRedisService implements ApplicationRunner{
	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private TransCodeService transCodeService;
	@Autowired
	private GoodsCodeService goodsCodeService;
	@Autowired
	private BaseCityService baseCityService;
	@Autowired
	private SysUserTypeService sysUserTypeService;
	@Autowired
	private com.shenghesun.treasure.union.controller.support.UnionRedisService unionRedisService;
	@Autowired
	private CodeListService codeListService;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//添加运输代码对照表
		setTransport();
		//添加货物代码对照表
		setGoods();
		//添加对照表集合
		setCodeList();
		//添加城市
		setCity();
		//添加访问接口用户类型
		setType();
		//添加联盟速运数据字典
		unionRedisService.setToRedis();
	}
	/**
	 * 向redis中添加运输方式代码表
	 */
	public void setTransport() {
		List<TransCode> transCodeList = transCodeService.find();
		if(!CollectionUtils.isEmpty(transCodeList)) {
			log.info("redis缓存运输方式代码表:"+transCodeList.size());
			long start = System.currentTimeMillis();
			for(int i=0;i<transCodeList.size();i++) {
				TransCode transCode = transCodeList.get(i);
				String key = transCode.getTransCode();
				redisUtil.set(key, transCode);
			}
			long end = System.currentTimeMillis(); 
			log.info("redis缓存运输方式代码表结束===运行时间:"+(end - start)+"毫秒");
		}
	}
	/**
	 * 向redis中添加货物名称对应代码表
	 */
	public void setGoods() {
		List<GoodsCode> goodsList = goodsCodeService.find();

		if(!CollectionUtils.isEmpty(goodsList)) {
			log.info("redis缓存货物名称代码表:"+goodsList.size());
			long start = System.currentTimeMillis();
			for(int i=0;i<goodsList.size();i++) {
				GoodsCode goodsCode = goodsList.get(i);
				String key = goodsCode.getGoodsCode();
				redisUtil.set(key, goodsCode);
			}
			long end = System.currentTimeMillis(); 
			log.info("redis缓存货物名称代码表结束===运行时间:"+(end - start)+"毫秒");
		}
	}
	/**
	 * 向redis中添加codeList、包括费率对照表，包装代码对应表、币种对应表
	 */
	public void setCodeList() {
		List<CodeList> codeList = codeListService.findAll();
		
		if(!CollectionUtils.isEmpty(codeList)) {
			log.info("redis缓存对照表集合信息:"+codeList.size());
			long start = System.currentTimeMillis();
			for(int i=0;i<codeList.size();i++) {
				CodeList code = codeList.get(i);
				redisUtil.setString(code.getCodeKey(),code.getCodeValue());
			}
			long end = System.currentTimeMillis(); 
			log.info("redis缓存对照表集合信息结束===运行时间:"+(end - start)+"毫秒");
		}
	}
	
	/**
	 * 向redis中添加城市信息
	 */
	public void setCity() {
		List<BaseCity> cityList = baseCityService.find();
		
		if(!CollectionUtils.isEmpty(cityList)) {
			log.info("redis缓存城市信息:"+cityList.size());
			long start = System.currentTimeMillis();
			for(int i=0;i<cityList.size();i++) {
				BaseCity baseCity = cityList.get(i);
				String key = baseCity.getName();
				redisUtil.setString(key, key);
			}
			long end = System.currentTimeMillis(); 
			log.info("redis缓存城市信息结束===运行时间:"+(end - start)+"毫秒");
		}
	}
	/**
	 * 向redis中添加接口用户类型信息   sysUserTypeService
	 */
	public void setType() {
		List<SysUserType> userTypeList = sysUserTypeService.findAll();
		if(!CollectionUtils.isEmpty(userTypeList)) {
			log.info("redis缓存用户类型信息:"+userTypeList.size());
			long start = System.currentTimeMillis();
			for(int i=0;i<userTypeList.size();i++) {
				SysUserType userType = userTypeList.get(i);
				redisUtil.setString(userType.getAccount(), userType.getType());
			}
			long end = System.currentTimeMillis(); 
			log.info("redis缓存用户类型信息结束===运行时间:"+(end - start)+"毫秒");
		}
	}
}
