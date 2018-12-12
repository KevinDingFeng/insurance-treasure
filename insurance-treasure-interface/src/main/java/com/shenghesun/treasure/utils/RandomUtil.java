package com.shenghesun.treasure.utils;

import java.util.Random;

public class RandomUtil {

	//获取随机验证码，包含字母和数字
	public static String randomString(int n) {
		String pool = "asdfghjklqwertyuiopzxcvbnm0123456789";
		return randomString(n,pool);
	}
	//获取随机大写英文码
	public static String randomUpString(int n) {
		String pool = "QWERTYUIOPASDFGHJKKLZXCVBNM";
		return randomString(n,pool);
	}
	public static String randomString(int n,String pool) {
		if(n < 1) {
			return "";
		}
		//String pool = "asdfghjklqwertyuiopzxcvbnm0123456789";
		Random r = new Random();
		StringBuilder b = new StringBuilder();
		b.append(pool.charAt(r.nextInt(pool.length())));
		for(int i = 0 ; i < n - 1 ; i ++) {
			b.append(pool.charAt(r.nextInt(pool.length())));
		}
		return b.toString();
	}
	
//	 //生成随机数字和字母,  
//  public static String getStringRandom(int length) {  
//        
//      String val = "";  
//      Random random = new Random();  
//        
//      //参数length，表示生成几位随机数  
//      for(int i = 0; i < length; i++) {  
//            
//          String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
//          //输出字母还是数字  
//          if( "char".equalsIgnoreCase(charOrNum) ) {  
//              //输出是大写字母还是小写字母  
//              //int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
//              val += (char)(random.nextInt(26) + 97);  
//          } else if( "num".equalsIgnoreCase(charOrNum) ) {  
//              val += String.valueOf(random.nextInt(10));  
//          }  
//      }  
//      return val;  
//  } 
	public static String randomString() {
		return randomString(6);
	}

	public static String randomNum(int n) {
		if(n < 1) {
			return "";
		}
		Random r = new Random();
		StringBuilder b = new StringBuilder();
		b.append(r.nextInt(10));
		for(int i = 0 ; i < n - 1 ; i ++) {
			b.append(r.nextInt(10));	
		}
		return b.toString();
	}
	public static String randomNum() {
		return randomNum(6);
	}
	
	//获取险种代码前两位
	public static String getClassCode(String code) {
		return code.substring(0, 2);
	}
	//
	
	public static void main(String[] args) {
		System.out.println(randomUpString(1));
	}
	
}
