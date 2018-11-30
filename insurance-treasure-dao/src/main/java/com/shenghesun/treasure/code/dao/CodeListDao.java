package com.shenghesun.treasure.code.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.code.CodeList;

@Repository
public interface CodeListDao extends JpaRepository<CodeList, Long>, JpaSpecificationExecutor<CodeList>{
	
	CodeList findByCodeKey(String key);
}
