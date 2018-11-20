package com.shenghesun.treasure.compay.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.treasure.company.CompanyMessageService;
import com.shenghesun.treasure.config.CustomConfig;
import com.shenghesun.treasure.system.company.CompanyMessage;
import com.shenghesun.treasure.system.entity.SysUser;
import com.shenghesun.treasure.system.service.SysUserService;
import com.shenghesun.treasure.utils.HttpHeaderUtil;
import com.shenghesun.treasure.utils.JsonUtil;
import com.shenghesun.treasure.utils.RandomUtil;
import com.shenghesun.treasure.utils.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping(value="/api/company")
@Slf4j
public class CompanyController {
	
	@Autowired
	CompanyMessageService companyService;
	@Autowired
	SysUserService sysUserService;
	@Value("${spring.servlet.multipart.file-path}")
	private String filePath;
	/**
	 * 完善公司信息
	 * @return
	 */
	@PostMapping(value = "/complete")
	public JSONObject completeCompanyMessage(HttpServletRequest request,CompanyMessage companyMessage,@RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			//获取请求用户信息
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			System.out.println(token);
			Long userId = TokenUtil.getLoginUserId(token);
			//图片上传
			if(file!=null) {
				String filePath = singleFileUpload(file);
				companyMessage.setCreditCard(filePath);
			}
			//保存公司信息
			CompanyMessage company = companyService.save(companyMessage);
			//更新当前用户的公司信息
			SysUser user = sysUserService.findById(userId);
			user.setCompanyId(company.getId());
			sysUserService.save(user);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
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
	public String singleFileUpload(MultipartFile file) {
	   /* if (companyFile.isEmpty()) {
	        return JsonUtil.getFailJSONObject("上传内容为空");
	    }*/
	   // String filePath = "";
	    String returnPath="";
	    try {
	    	//判断上传路径是否存在
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd/");
	        String fileName = file.getOriginalFilename();
	        /*String suffix = fileName.substring(fileName.lastIndexOf("."));
	        System.out.println(suffix);*/
			returnPath = CustomConfig.MODEL+sdf.format(new Date())+ RandomUtil.randomString(4);
	    	filePath = filePath+returnPath;
	    	File f = new File(filePath);
	        if (!f.exists()) {
	            f.mkdirs();
	        }
	        // Get the file and save it somewhere
	        byte[] bytes = file.getBytes();
	        Path path = Paths.get(filePath);
	        Files.write(path, bytes);
	    } catch (IOException e) {
	    	log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
	    }
	    return returnPath;
	}
	/**
	 * 查看公司信息
	 */
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public JSONObject findCompany(HttpServletRequest request) {
		CompanyMessage company = null;
		try {
			//获取请求用户信息
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long companyId = TokenUtil.getLoginCompanyId(token);
			//查看公司信息
			company = companyService.findById(companyId);
		} catch (Exception e) {
			log.error("Exception {} in {}", e.getStackTrace(), Thread.currentThread().getName());
			return JsonUtil.getFailJSONObject();
		}
		return JsonUtil.getSuccessJSONObject(company);
	}
}
