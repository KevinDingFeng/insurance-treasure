package com.shenghesun.treasure.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.dao.OrderMessageDao;
import com.shenghesun.treasure.system.entity.OrderMessage;

@Service
public class OrderMessageService {

	@Autowired
	private OrderMessageDao orderMessageDao;
	
	public OrderMessage findById(Long id) {
		return orderMessageDao.getOne(id);
	}
}
