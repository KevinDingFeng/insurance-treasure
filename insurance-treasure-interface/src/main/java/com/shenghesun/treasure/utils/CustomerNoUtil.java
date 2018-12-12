package com.shenghesun.treasure.utils;

public class CustomerNoUtil {

	//生成第一个客户编号
	public static String getFirstNo(){
		return "C01";
	}
	//获取客户编号
	public static String getNo(String no) {
		char first =  no.charAt(0);
		String last = no.substring(1,3);
		if(last.equals("99")) {
			first = (char)(first +1);
			last="01";
		}else {
			last = Integer.parseInt(last)+01+"";
		}
		if(last.length()==1) {
			return first+"0"+last;
		}
		return first+last;
	}
}
