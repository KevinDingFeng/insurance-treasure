package com.shenghesun.treasure.system.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.entity.FundDetails;

@Repository
public interface FundDetailsDao extends JpaRepository<FundDetails, Long>, JpaSpecificationExecutor<FundDetails>{

}
