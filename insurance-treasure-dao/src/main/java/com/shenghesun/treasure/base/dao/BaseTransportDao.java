package com.shenghesun.treasure.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.dictionary.BaseTransport;

@Repository
public interface BaseTransportDao extends JpaRepository<BaseTransport, Long>, JpaSpecificationExecutor<BaseTransport>{

	List<BaseTransport> findByBusinessCodeAndParentCode(String business,String parentCode);
	
	List<BaseTransport> findByBusinessCodeAndParentCodeAndCity(String business,String parentCode,String city);
	
	List<BaseTransport> findByCodeAndCity(String code,String city);
	/*
	
	List<BaseTransport> findByBusinessCodeAndTparentCode(String code,String parentCode);
	
	List<BaseTransport> findByCountryAndTparentCode(String country,String code);*/
}
