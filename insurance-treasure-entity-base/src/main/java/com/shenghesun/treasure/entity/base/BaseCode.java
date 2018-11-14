package com.shenghesun.treasure.entity.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class BaseCode {

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonIgnore
	protected Long id;
	
	@Version
	@JsonIgnore
	protected long version=0l;

	
	@Column(length = 50)
	private String name;
	
	@Column(length = 10)
	private String code;

	//父级code
	@Column(length = 10)
	@JsonIgnore
	private String parentCode;
	
}
