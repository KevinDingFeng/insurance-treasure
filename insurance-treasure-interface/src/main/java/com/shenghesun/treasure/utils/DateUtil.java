package com.shenghesun.treasure.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	//获取年月日字符串
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	public static void main(String[] args) {
	}
}
