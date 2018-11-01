package com.shenghesun.treasure.company.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shenghesun.treasure.system.company.CompanyMessage;

public interface CompanyMessageDao extends JpaRepository<CompanyMessage, Long>, JpaSpecificationExecutor<CompanyMessage>{

}
