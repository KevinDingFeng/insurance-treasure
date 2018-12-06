package com.shenghesun.treasure.cpic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.cpic.Approvl;

@Repository
public interface ApprovlDao extends PagingAndSortingRepository<Approvl, Long>, JpaSpecificationExecutor<Approvl>{
	
	public Approvl findByApplyId(String applyId);
	
	public Approvl findByOrderNo(String orderNo);
	
	public Approvl findByApplyNo(String applyNo);
}
