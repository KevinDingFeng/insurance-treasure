package com.shenghesun.treasure.cpic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.cpic.InsurancesStatus;

@Repository
public interface InsurancesStatusDao extends PagingAndSortingRepository<InsurancesStatus, Long>, JpaSpecificationExecutor<InsurancesStatus>{

}
