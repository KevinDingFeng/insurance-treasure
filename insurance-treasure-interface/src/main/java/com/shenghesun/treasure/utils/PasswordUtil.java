package com.shenghesun.treasure.utils;


import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;

import com.shenghesun.treasure.config.CustomConfig;

public class PasswordUtil {

	public static String encrypt(String password, String salt) {
		return encrypt(password, salt, CustomConfig.HASH_ITERATIONS);
	}

	public static String encrypt(String password, String salt, int iterations) {
		// 加密操作
		ByteSource source = ByteSource.Util.bytes(salt);
		// 加密方式，明文密码，盐值byte，加密次数
//		Object result = new SimpleHash(CustomConfig.HASH_ALGORITHM_NAME, password, source, CustomConfig.HASH_ITERATIONS);simple 的模式下 支持的加密方式有限，比如 md5
		Sha256Hash result = new Sha256Hash(password, source, iterations);
		return result.toString();
	}
	
	public static void main(String[] args) {
		String p = "Shsun2018";
		String salt = RandomUtil.randomString(16);
		
//		String salt = "amxcjksao";
		System.out.println(salt);
		System.out.println(encrypt(p, salt));
		
//		System.out.println(RandomUtil.randomString(32));
	}
	
}
