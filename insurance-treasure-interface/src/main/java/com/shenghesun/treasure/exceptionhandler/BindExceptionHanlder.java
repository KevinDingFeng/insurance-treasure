package com.shenghesun.treasure.exceptionhandler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.shenghesun.treasure.model.exception.BindExceptResult;
import com.shenghesun.treasure.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class BindExceptionHanlder {

	@ExceptionHandler(BindException.class)
	public Object MethodArgumentNotValidException(BindException ex, HttpServletRequest request,
			HttpServletResponse response) {
		log.error("Exception {} in {} ", ex.getMessage(), request.getRequestURL().toString());
		// MethodArgumentNotValidException c = (MethodArgumentNotValidException) ex;
//		List<ObjectError> errors = c.getBindingResult().getAllErrors();
//		errors.stream().forEach(x -> errorMsg.append(x.getDefaultMessage()).append(";"));
		List<BindExceptResult> results = new ArrayList<>();
		List<ObjectError> fieldErrors = ex.getAllErrors();
		if(CollectionUtils.isNotEmpty(fieldErrors)) {
			for(ObjectError oe: fieldErrors) {
				FieldError fieldError = (FieldError) oe;
				BindExceptResult br = new BindExceptResult();
				br.setField(fieldError.getField());
				br.setDefaultMessage(fieldError.getDefaultMessage());
				results.add(br);
			}
		}
//		StringBuilder sb = new StringBuilder();
//		fieldErrors.stream().forEach(fieldError -> errorMsg.append(fieldError.getDefaultMessage()).append(";"));
//
//		pouplateExceptionResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, errorMsg.toString());
		
		return JsonUtil.getFailJSONObject(results);
	}
}
