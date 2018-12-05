package com.shenghesun.treasure.cpic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.cpic.Policy;

@Repository
public interface PolicyDao extends PagingAndSortingRepository<Policy, Long>, JpaSpecificationExecutor<Policy>{
	
	Policy findByApplyno(String applyNo);
}
