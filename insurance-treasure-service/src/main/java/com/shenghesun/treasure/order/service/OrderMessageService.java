package com.shenghesun.treasure.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.order.dao.OrderMessageDao;
import com.shenghesun.treasure.system.order.OrderMessage;

@Service
public class OrderMessageService {

	@Autowired
	private OrderMessageDao orderMessageDao;
	
	public OrderMessage findById(Long id) {
		return orderMessageDao.getOne(id);
	}
	
	public OrderMessage save(OrderMessage orderMessage) {
		return orderMessageDao.save(orderMessage);
	}
	
	public OrderMessage findBy(OrderMessage orderMessage) {
		return orderMessageDao.save(orderMessage);
	}
}
