package com.shenghesun.treasure.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.order.OrderMessage;

@Repository
public interface OrderMessageDao extends JpaRepository<OrderMessage, Long>, JpaSpecificationExecutor<OrderMessage> {
	
	OrderMessage findByUserid(Long id);
}
