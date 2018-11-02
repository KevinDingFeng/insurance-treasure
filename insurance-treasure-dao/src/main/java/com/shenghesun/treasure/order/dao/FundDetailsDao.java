package com.shenghesun.treasure.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.order.FundDetails;
import java.lang.String;
import java.util.List;

@Repository
public interface FundDetailsDao extends JpaRepository<FundDetails, Long>, JpaSpecificationExecutor<FundDetails>{

	List<FundDetails> findByCompanyId(String companyid);
}
