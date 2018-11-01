package com.shenghesun.treasure.compay.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CompanyController {
	
	@Autowired
	CompanyMessageService companyMessageService;
	@Autowired
	SysUserService sysUserService;
	/**
	 * 完善公司信息
	 * @return
	 */
	@RequestMapping(value = "/complete", method = RequestMethod.GET)
	public JSONObject completeCompanyMessage(CompanyMessage companyMessage,String cellphone) {
		SysUser user=null;
		try {
			if(cellphone!=null) {
				user = sysUserService.findByCell(cellphone);
			}
			if(user!=null) {
				user.setCompanyMessage(companyMessage);
			}else {
				return JsonUtil.getFailJSONObject("用户信息有误");
			}
			sysUserService.save(user);
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject("特殊错误");
		}
		return JsonUtil.getSuccessJSONObject();
	}
	/**
	 * 上传统一信用证
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("/upload") 
	public JSONObject singleFileUpload(@RequestParam("file") MultipartFile file) {
	    if (file.isEmpty()) {
	        return JsonUtil.getFailJSONObject("上传内容为空");
	    }
	    String filePath = "";
	    try {
	        // Get the file and save it somewhere
	        byte[] bytes = file.getBytes();
	        filePath = "C:\\" + file.getOriginalFilename();
	        Path path = Paths.get(filePath);
	        System.out.println(path);
	        Files.write(path, bytes);
	    } catch (IOException e) {
	    	 return JsonUtil.getFailJSONObject("特殊错误");
	    }
	    return JsonUtil.getSuccessJSONObject(filePath);
	}
}
