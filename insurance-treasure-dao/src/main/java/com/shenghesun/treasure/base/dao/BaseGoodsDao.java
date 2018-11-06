package com.shenghesun.treasure.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.dictionary.BaseGoods;

@Repository
public interface BaseGoodsDao extends JpaRepository<BaseGoods, Long>, JpaSpecificationExecutor<BaseGoods>{

	List<BaseGoods> findByTypeIn(List<String> list);
	
	List<BaseGoods> findByParentCode(String code);
	
	List<BaseGoods> findByParentId(String parentId);
	
	List<BaseGoods> findByParentCodeAndParentId(String code,String id);
}
