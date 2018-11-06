package com.shenghesun.treasure.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.dictionary.BaseBusiness;

@Repository
public interface BaseBusinessDao extends JpaRepository<BaseBusiness, Long>, JpaSpecificationExecutor<BaseBusiness>{

}
