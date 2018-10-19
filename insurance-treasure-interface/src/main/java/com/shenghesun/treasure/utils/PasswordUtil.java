package com.shenghesun.treasure.utils;


import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.shenghesun.treasure.config.CustomConfig;

public class PasswordUtil {

	public static String encrypt(String password, String salt) {
		return encrypt(password, salt, CustomConfig.HASH_ALGORITHM_NAME, CustomConfig.HASH_ITERATIONS);
	}

	public static String encrypt(String password, String salt, String algorithm, int iterations) {
		// 加密操作
		ByteSource source = ByteSource.Util.bytes(salt);
		// 加密方式，明文密码，盐值byte，加密次数
		Object result = new SimpleHash(CustomConfig.HASH_ALGORITHM_NAME, password, source, CustomConfig.HASH_ITERATIONS);
		return result.toString();
	}
	
	public static void main(String[] args) {
		/*String p = "123";
		String salt = RandomUtil.randomString(16);
		
//		String salt = "glnfe62dfz0q1dxa";
		System.out.println(salt);
		System.out.println(encrypt(p, salt));
		*/
		System.out.println(RandomUtil.randomString(32));
	}
	
}
