package com.shenghesun.treasure.union.controller.support;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.shenghesun.treasure.system.union.code.UnionGoodsCode;
import com.shenghesun.treasure.system.union.code.UnionPackageCode;
import com.shenghesun.treasure.system.union.code.UnionTransCode;
import com.shenghesun.treasure.union.service.UnionGoodsCodeService;
import com.shenghesun.treasure.union.service.UnionPackageCodeService;
import com.shenghesun.treasure.union.service.UnionTransCodeService;
import com.shenghesun.treasure.utils.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UnionRedisService {

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private UnionGoodsCodeService unionGoodsCodeService;
	@Autowired
	private UnionTransCodeService unionTransCodeService;
	@Autowired
	private UnionPackageCodeService unionPackageCodeService;
	/**
	 * 向redis中添加联盟速运数据字典
	 */
	public void setToRedis() {
		try {
			//添加货物代码
			setGoods();
			//添加运输代码
			setTrans();
			//添加包装代码
			setPackage();
			//添加币种代码
			setCurrency();
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
		}
	}
	/**
	 * 向redis中添加货物代码
	 */
	public void setGoods() {
		List<UnionGoodsCode> goodsCodeList = unionGoodsCodeService.findAll();
		if(!CollectionUtils.isEmpty(goodsCodeList)) {
			log.info("redis缓存联盟货物代码表:"+goodsCodeList.size());
			long start = System.currentTimeMillis();
			for(int i=0;i<goodsCodeList.size();i++) {
				UnionGoodsCode goodsCode = goodsCodeList.get(i);
				String key = goodsCode.getUnionGoodsName();
				redisUtil.set("union"+key, goodsCode);
			}
			long end = System.currentTimeMillis(); 
			log.info("redis缓存联盟货物代码表结束===运行时间:"+(end - start)+"毫秒");
		}
	}
	/**
	 * 向redis中添加运输代码
	 */
	public void setTrans() {
		List<UnionTransCode> transCodeList = unionTransCodeService.findAll();
		if(!CollectionUtils.isEmpty(transCodeList)) {
			log.info("redis缓存联盟运输代码表:"+transCodeList.size());
			long start = System.currentTimeMillis();
			for(int i=0;i<transCodeList.size();i++) {
				UnionTransCode transCode = transCodeList.get(i);
				String key = transCode.getUnionTranscode();
				redisUtil.set("union"+key, transCode);
			}
			long end = System.currentTimeMillis(); 
			log.info("redis缓存联盟运输代码表结束===运行时间:"+(end - start)+"毫秒");
		}
	}
	/**
	 * 向redis中添加包装代码
	 */
	public void setPackage() {
		List<UnionPackageCode> packageCodeList = unionPackageCodeService.findAll();
		if(!CollectionUtils.isEmpty(packageCodeList)) {
			log.info("redis缓存联盟包装代码表:"+packageCodeList.size());
			long start = System.currentTimeMillis();
			for(int i=0;i<packageCodeList.size();i++) {
				UnionPackageCode packageCode = packageCodeList.get(i);
				String key = packageCode.getUnionPackagecode();
				redisUtil.set("union"+key, packageCode);
			}
			long end = System.currentTimeMillis(); 
			log.info("redis缓存联盟包装代码表结束===运行时间:"+(end - start)+"毫秒");
		}
	}
	/**
	 * 向redis中添加币种代码
	 */
	public void setCurrency() {
		redisUtil.set("unionRMB", "01");
	}
}
