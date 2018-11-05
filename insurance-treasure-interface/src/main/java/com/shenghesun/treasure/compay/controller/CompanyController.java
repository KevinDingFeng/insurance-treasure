package com.shenghesun.treasure.compay.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.JsonUtil;

@RestController()
@RequestMapping(value="/company")
public class CompanyController {
	
	@Autowired
	CompanyMessageService companyService;
	@Autowired
	SysUserService sysUserService;
	/**
	 * 完善公司信息
	 * @return
	 */
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public JSONObject completeCompanyMessage(CompanyMessage companyMessage) {
		try {
			companyService.save(companyMessage);
		} catch (Exception e) {
			return JsonUtil.getFailJSONObject();
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
	    	 return JsonUtil.getFailJSONObject();
	    }
	    return JsonUtil.getSuccessJSONObject(filePath);
	}
}
