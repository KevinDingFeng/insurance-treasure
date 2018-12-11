package com.shenghesun.treasure.utils;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@SuppressWarnings("unchecked")
@Component
public class RedisUtil {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除key
	 * 
	 * @param pattern
	 */
	public void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
			redisTemplate.delete(keys);
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public String get(final String key) {
		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
		try {
			result = operations.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result == null) {
			return null;
		}
	/*	String str = result.toString();
		if(str.indexOf("\"")==0) { 
			str = str.substring(1,str.length());
		}  //去掉第一个 "
		if(str.lastIndexOf("\"")==(str.length()-1)) {
			str = str.substring(0,str.length()-1);  //去掉最后一个 " 
		}*/
		return result.toString();
	}
	
	public String getString(final String key) {
		String result = null;
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		ValueOperations<Serializable, String> operations = redisTemplate.opsForValue();
		try {
			result = operations.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result == null) {
			return null;
		}
		return result;
	}
	
	public Object getObj(final String key) {
		Object result = null;
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
		try {
			result = operations.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result == null) {
			return null;
		}
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean setString(final String key, String value) {
		boolean result = false;
		try {
			redisTemplate.setValueSerializer(new StringRedisSerializer());
			ValueOperations<Serializable, String> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean hmset(String key, Map<String, String> value) {
		boolean result = false;
		try {
			redisTemplate.opsForHash().putAll(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Map<String, String> hmget(String key) {
		Map<String, String> result = null;
		try {
			result = redisTemplate.opsForHash().entries(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查找所有符合给定模式( pattern)的 key 。
	 * @Title: keys 
	 * @Description: TODO 
	 * @param pattern
	 * @return  Set<Object> 
	 * @author yangzp
	 * @date 2018年10月19日上午11:28:02
	 **/ 
	public Set<Object> keys(String pattern) {
		Set<Object>  resultSet = null;
		try {
			resultSet = redisTemplate.keys(pattern);
		}catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		return resultSet;
	}
}
