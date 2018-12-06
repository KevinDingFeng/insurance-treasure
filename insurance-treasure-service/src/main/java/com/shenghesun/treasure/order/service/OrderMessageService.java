package com.shenghesun.treasure.order.service;

import java.sql.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.order.dao.OrderMessageDao;
import com.shenghesun.treasure.order.model.OrderCondition;
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
	public Page<OrderMessage> findByUserId(Long userId,Pageable page) {
		return orderMessageDao.findByUserId(userId,page);
	}
	public OrderMessage findByOrderNo(String orderNo) {
		return orderMessageDao.findByOrderNo(orderNo);
	}
	public OrderMessage findByApplyNo(String applyNo) {
		return orderMessageDao.findByApplyNo(applyNo);
	}
	public List<OrderMessage> findByByUserId(Long userId) {
		return orderMessageDao.findByUserId(userId);
	}
	/**
	 * @Title: findByConditions 
	 * @Description: 按照条件查询 
	 * @param condition
	 * @param pageable
	 * @return  Page<OrderMessage> 
	 * @author yangzp
	 * @date 2018年11月20日下午4:29:41
	 **/ 
	public Page<OrderMessage> findByConditions(OrderCondition condition, Pageable pageable) {
		return orderMessageDao.findAll(new Specification<OrderMessage>() {

			@Override
			public Predicate toPredicate(Root<OrderMessage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				
				if (condition.getUserId() != null) {
					predicate.getExpressions().add(cb.equal(root.get("userId"), condition.getUserId()));
				}
				if (condition.getInsuranceStatus() != null) {
					if(condition.getInsuranceStatus().equals("10")) {
						predicate.getExpressions().add(cb.equal(root.get("insuranceStatus"), condition.getInsuranceStatus()));
					}else {
						predicate.getExpressions().add(cb.notEqual(root.get("insuranceStatus"), "10"));
					}
				}
				
				if (StringUtils.isNotEmpty(condition.getKeyword())) {
					Predicate pre = cb.like(
							cb.concat(root.get("orderNo"), root.get("businessType")),
							"%" + condition.getKeyword() + "%");
					predicate.getExpressions().add(pre);
				}
				
				
				if (condition.getBeginDate() != null) {
					predicate.getExpressions()
							.add(cb.greaterThanOrEqualTo(root.get("creation").as(Date.class), condition.getBeginDate()));
				}
				
				if (condition.getEndDate() != null) {
					predicate.getExpressions()
							.add(cb.lessThanOrEqualTo(root.get("creation").as(Date.class), condition.getEndDate()));
				}
				
				return predicate;
			}
		}, pageable);
	}
}
