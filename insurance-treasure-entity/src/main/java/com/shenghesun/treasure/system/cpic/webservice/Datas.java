package com.shenghesun.treasure.system.cpic.webservice;

import com.shenghesun.treasure.system.order.OrderMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("DATAS")
public class Datas {
//	@XStreamAlias("DATA")
//	private Order order;
	
	@XStreamAlias("DATA")
	private OrderMessage orderMessage;
}
