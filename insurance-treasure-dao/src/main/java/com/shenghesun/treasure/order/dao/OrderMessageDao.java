package com.shenghesun.treasure.order.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.order.OrderMessage;

@Repository
public interface OrderMessageDao extends JpaRepository<OrderMessage, Long>, JpaSpecificationExecutor<OrderMessage> {
	
	//List<OrderMessage> findByUserId(Long id);
	
	Page<OrderMessage> findByUserId(Long id,Pageable page);
	
	OrderMessage findByOrderNo(String orderNo);
}
