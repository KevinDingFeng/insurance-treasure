package com.shenghesun.treasure.compay.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.shenghesun.treasure.auth.support.LoginService;
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
	@Autowired
	private LoginService loginService;
	/**
	 * 完善公司信息
	 * @return
	 */
	@PostMapping(value = "/complete")
	public JSONObject completeCompanyMessage(HttpServletRequest request,CompanyMessage companyMessage,@RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			//获取请求用户信息
			String token = HttpHeaderUtil.getToken((HttpServletRequest) request);
			Long userId = TokenUtil.getLoginUserId(token);
			Long companyId = TokenUtil.getLoginCompanyId(token);
			//图片上传
			if(file!=null) {
				String filePath = singleFileUpload(file);
				companyMessage.setCreditCard(filePath);
			}
			if(companyId==0) {         
				if(companyMessage.getCreditCard()==null) {
					return JsonUtil.getFailJSONObject("请上传公司凭证");
				}
				//保存公司信息
				CompanyMessage company = companyService.save(companyMessage);
				//更新当前用户的公司信息
				SysUser user = sysUserService.findById(userId);
				user.setCompanyId(company.getId());
				sysUserService.save(user);
				String newToken = loginService.login(user.getId(), user.getAccount(),user.getCompanyId());
				return JsonUtil.getSuccessJSONObject(newToken);
				
			}else {
				CompanyMessage company = companyService.findById(companyId);
				//保存公司信息
				company.setCompanyName(companyMessage.getCompanyName());
				company.setContactName(companyMessage.getContactName());
				company.setContactCell(companyMessage.getContactCell());
				company.setEmail(companyMessage.getEmail());
				company.setCreditCode(companyMessage.getCreditCode());
				if(companyMessage.getCreditCard()!=null) {
					company.setCreditCard(companyMessage.getCreditCard());
				}
				companyService.save(company);
			}
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
		String returnPath="";
		try {
			//格式化日期，用于创建日期目录
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd/");
			//获取文件后缀
			String fileName = file.getOriginalFilename();
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			//生成文件名称
			String name = RandomUtil.randomString(4)+suffix;
			//模块+日期子目录
			String dir = CustomConfig.MODEL+sdf.format(new Date());
			//判断上传文件路径是否存在，不存在则创建
			filePath = filePath+dir;
			File f = new File(filePath);
			if (!f.exists()) {
				f.mkdirs();
			}
			//创建文件流，开始文件上传
			InputStream is = file.getInputStream();
			byte[] bytes = new byte[is.available()];
			OutputStream os = new FileOutputStream(filePath+name);
			is.read(bytes);
			os.write(bytes);
			is.close();
			os.close();
			//将目录和文件名返回调用方法端，用于存储上传文件数据
			returnPath = dir+name;
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
