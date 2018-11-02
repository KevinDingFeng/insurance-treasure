package com.shenghesun.treasure.test.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
/**
 * 通过测试，把 @CacheEvict 等缓存使用的注解放置到 controller 中会失效，原因不明，所以使用时只能通过 @service 载入
 * @author 程任强
 *
 */
@Service
public class TestCacheService {

	@CacheEvict(cacheNames = "codes", key = "#key")
	public void removedByKey(String key) {
		return ;
	}
	
	@CachePut(cacheNames = "codes", key = "#key")
	public String updateByKey(String key, String value) {
		return value;
	}
	
	@CachePut(cacheNames = "codes", key = "#key")
	public String insertByKey(String key, String value) {
		return value;
	}

	@Cacheable(cacheNames = "codes", key = "#key")
	public String findByKey(String key) {
		System.out.println("没有读取缓存");
		switch (key) {
		case "A":
			return "A1";
		case "B":
			return "B1";
		case "C":
			return "C1";
		default:
			return "ZZ";
		}
	}
}
