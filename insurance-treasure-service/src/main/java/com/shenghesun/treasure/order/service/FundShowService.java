package com.shenghesun.treasure.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.order.dao.FundShowDao;
import com.shenghesun.treasure.system.model.FundShow;

@Service
public class FundShowService {
	@Autowired
	private FundShowDao fundShowDao;
/*	public Page<FundShow> findByCompanyId(Long companyid,Pageable pageable){
		return fundShowDao.findByCompanyId(companyid,pageable);
	}*/
	public List<FundShow> findByCompanyId(Long companyid,Integer page,Integer size){
		int start = (page-1)*size;
		return fundShowDao.findByCompanyId(companyid,start,size);
	}
	public Integer findCount(Long companyId) {
		return fundShowDao.findCount(companyId);
	}
}
