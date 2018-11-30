package com.shenghesun.treasure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.shenghesun.treasure.base.service.BaseCityService;
import com.shenghesun.treasure.code.service.GoodsCodeService;
import com.shenghesun.treasure.code.service.TransCodeService;
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
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//添加接口对照信息
		setUserType();
		//添加运输代码对照表
		setTransport();
		//添加货物代码对照表
		setGoods();
		//添加费率信息
		setRate();
		//添加包装
		setPackage();
		//添加币种
		setCurrency();
		//添加城市
		setCity();
		//添加联盟速运数据字典
		unionRedisService.setToRedis();
	}
	/**
	 * 向redis中添加接口对照信息
	 */
	private void setUserType() {
		List<SysUserType> userTypeList = sysUserTypeService.findAll();
		if(!CollectionUtils.isEmpty(userTypeList)) {
			log.info("redis缓存用户访问接口类型表:"+userTypeList.size());
			long start = System.currentTimeMillis();
			for(int i=0;i<userTypeList.size();i++) {
				SysUserType userType = userTypeList.get(i);
				String key = userType.getAccount();
				redisUtil.set("union"+key, userType);
			}
			long end = System.currentTimeMillis(); 
			log.info("redis缓存用户访问接口类型表结束===运行时间:"+(end - start)+"毫秒");
		}
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
	 * 向redis中添加codeList
	 */
	
	
	
	
	
	public void setRate() {
		//向redis中存储费率对照表
		redisUtil.set("B01rate", 0.02);
		redisUtil.set("B02rate", 0.03);
		redisUtil.set("B03rate", 0.05);
		redisUtil.set("B04rate", 0.04);
		redisUtil.set("B05rate", 0.04);
		redisUtil.set("B06rate", 0.05);
		redisUtil.set("B07rate", 0.04);
		redisUtil.set("B08rate", 0.06);
		redisUtil.set("B09rate", 0.03);
		redisUtil.set("B10rate", 0.05);
	}
	/**
	 * 向redis中添加包装代码对应
	 */
	public void setPackage() {
		redisUtil.set("01pack", "箱装");
		redisUtil.set("02pack", "袋装");
		redisUtil.set("03pack", "托盘");
		redisUtil.set("04pack", "散装");
		redisUtil.set("05pack", "裸装");
		redisUtil.set("06pack", "桶装");
		redisUtil.set("07pack", "罐装");
		redisUtil.set("08pack", "盘卷");
	}
	/**
	 * 向redis中添加币种代码对应
	 */
	public void setCurrency() {
		redisUtil.set("01curr", "人民币");
		redisUtil.set("02curr", "美元");
		redisUtil.set("03curr", "港元");
		redisUtil.set("04curr", "日元");
		redisUtil.set("06curr", "英镑");
		redisUtil.set("07curr", "欧元");
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
				redisUtil.set(key+"city", key);
			}
			long end = System.currentTimeMillis(); 
			log.info("redis缓存城市信息结束===运行时间:"+(end - start)+"毫秒");
		}
	}
}
