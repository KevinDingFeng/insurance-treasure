package com.shenghesun.treasure.cpic.service;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.shenghesun.treasure.cpic.dao.ApprovlDao;
import com.shenghesun.treasure.system.cpic.Approvl;
import com.shenghesun.treasure.system.order.OrderMessage;

@Service
public class ApprovlService {

	@Autowired
	private ApprovlDao approvlDao;
	
	public Approvl save(Approvl approvl) {
		return approvlDao.save(approvl);
	}
	
	public Approvl findByApplyId(String applyId) {
		return approvlDao.findByApplyId(applyId);
	}
	public Approvl findByOrderNo(String orderNo) {
		return approvlDao.findByOrderNo(orderNo);
	}
	public List<Approvl> findAll() {
		return approvlDao.findAll(new Specification<Approvl>() {
			@Override
			public Predicate toPredicate(Root<Approvl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				predicate.getExpressions().add(cb.notEqual(root.get("status"), "10"));
				return predicate;
			}
		});
	}
}
