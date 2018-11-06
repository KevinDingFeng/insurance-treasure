package com.shenghesun.treasure.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.dictionary.BaseDictionary;

@Repository
public interface BaseDictionaryDao extends JpaRepository<BaseDictionary, Long>, JpaSpecificationExecutor<BaseDictionary>{
	
	List<BaseDictionary> findByCountryAndType(String country,String type);
	
	List<BaseDictionary> findByType(String type);
}
