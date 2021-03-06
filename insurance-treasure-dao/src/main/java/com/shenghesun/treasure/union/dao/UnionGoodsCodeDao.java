package com.shenghesun.treasure.union.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.union.code.UnionGoodsCode;

@Repository
public interface UnionGoodsCodeDao extends JpaRepository<UnionGoodsCode, Long>, JpaSpecificationExecutor<UnionGoodsCode>{
	
	UnionGoodsCode findByUnionGoodsName(String name);
}
