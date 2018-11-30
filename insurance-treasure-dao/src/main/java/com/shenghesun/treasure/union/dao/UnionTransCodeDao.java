package com.shenghesun.treasure.union.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.union.code.UnionTransCode;

@Repository
public interface UnionTransCodeDao extends JpaRepository<UnionTransCode, Long>, JpaSpecificationExecutor<UnionTransCode>{
	public UnionTransCode findByUnionTranscode(String transCode);
}
