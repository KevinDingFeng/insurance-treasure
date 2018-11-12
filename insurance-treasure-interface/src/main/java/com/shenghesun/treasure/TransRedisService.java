package com.shenghesun.treasure;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.shenghesun.treasure.code.service.GoodsCodeService;
import com.shenghesun.treasure.code.service.TransCodeService;
import com.shenghesun.treasure.system.code.GoodsCode;
import com.shenghesun.treasure.system.code.TransCode;
import com.shenghesun.treasure.utils.RedisUtil;


@Component
@Order(value = 1)
public class TransRedisService implements ApplicationRunner{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private TransCodeService transCodeService;
	@Autowired
	private GoodsCodeService goodsCodeService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<TransCode> transCodeList = transCodeService.find();
		List<GoodsCode> goodsList = goodsCodeService.find();
		if(!CollectionUtils.isEmpty(transCodeList)) {
			logger.info("redis缓存运输方式代码表:"+transCodeList.size());
			long start = System.currentTimeMillis();
			for(int i=0;i<transCodeList.size();i++) {
				TransCode transCode = transCodeList.get(i);
				String key = transCode.getTransCode();
				redisUtil.set(key, transCode);
			}
			long end = System.currentTimeMillis(); 
			logger.info("redis缓存运输方式代码表结束===运行时间:"+(end - start)+"毫秒");
		}
		if(!CollectionUtils.isEmpty(goodsList)) {
			logger.info("redis缓存货物名称代码表:"+goodsList.size());
			long start = System.currentTimeMillis();
			for(int i=0;i<goodsList.size();i++) {
				GoodsCode goodsCode = goodsList.get(i);
				String key = goodsCode.getGoodsCode();
				redisUtil.set(key, goodsCode);
			}
			long end = System.currentTimeMillis(); 
			logger.info("redis缓存货物名称代码表结束===运行时间:"+(end - start)+"毫秒");
		}
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

}
