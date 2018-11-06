package com.shenghesun.treasure.order.support;

import org.springframework.stereotype.Service;

import com.shenghesun.treasure.system.order.OrderMessage;

@Service
public class OrderService {

	public OrderMessage complete(OrderMessage orderMessage) {
		return orderMessage;
	}

}
