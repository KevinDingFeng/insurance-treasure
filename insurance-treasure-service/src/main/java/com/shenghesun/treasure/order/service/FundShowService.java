package com.shenghesun.treasure.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.order.dao.FundShowDao;
import com.shenghesun.treasure.system.model.FundShow;

@Service
public class FundShowService {
	@Autowired
	private FundShowDao fundShowDao;
	public List<FundShow> findByCompanyId(Long companyid){
		return fundShowDao.findByCompanyId(companyid);
	}
}
