package com.shenghesun.treasure.system.cpic.webservice;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@XStreamAlias("SALERINFOS")
public class Salerinfos {
	@XStreamAlias("SALERINFONAME")
	private String salerinfoname;
	@XStreamAlias("SALERINFOCERT")
	private String salerinfocert;
}
