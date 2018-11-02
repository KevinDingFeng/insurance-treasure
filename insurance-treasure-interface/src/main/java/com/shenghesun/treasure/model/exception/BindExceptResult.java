package com.shenghesun.treasure.model.exception;

import lombok.Data;

/**
 * 后台校验异常结果
 * @ClassName: BindExceptResult 
 * @Description: TODO
 * @author: yangzp
 * @date: 2018年11月2日 下午1:43:26  
 */
@Data
public class BindExceptResult {
	
	/**
	 * 非法字段
	 */
	private String field;
	
	/**
	 * 提示信息
	 */
	private String defaultMessage;
}
