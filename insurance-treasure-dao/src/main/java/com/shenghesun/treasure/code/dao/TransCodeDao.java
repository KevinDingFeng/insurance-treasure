package com.shenghesun.treasure.code.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.code.TransCode;

@Repository
public interface TransCodeDao extends JpaRepository<TransCode, Long>, JpaSpecificationExecutor<TransCode>{

}
