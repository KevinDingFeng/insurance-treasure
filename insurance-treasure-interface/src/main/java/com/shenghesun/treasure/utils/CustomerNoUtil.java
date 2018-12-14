package com.shenghesun.treasure.utils;

import org.apache.commons.lang3.StringUtils;

public class CustomerNoUtil {

	/**
	 * 	@Title
	 *  @return String
	 *  @author zdd
	 *	@date 2018年12月13日上午10:45:22
	 *  @Description 生成默认初始编号
	 */
	public static String getFirstNo(){
		return "C01";
	}
	/**
	 * 	@Title
	 *  @param no
	 *  @return String
	 *  @author zdd
	 *	@date 2018年12月13日上午10:44:30
	 *  @Description 用于生成客户编号，生成规则从英文字母C开始一直到英文字母Z后面跟两位数字依次递增
	 *	
	 */
	public static String getNo(String no) {
		if(StringUtils.isNotEmpty(no)) {
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
		}else {
			return getFirstNo();
		}
	}
}
