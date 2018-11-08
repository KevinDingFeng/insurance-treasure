package com.shenghesun.treasure.order.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shenghesun.treasure.system.model.FundShow;

@Repository
public interface FundShowDao extends JpaRepository<FundShow, Long>, JpaSpecificationExecutor<FundShow>{
	@Query(value="select u.* from (select o.id,o.last_modified,o.version,o.order_amount,o.plus_or_minus,o.creation from order_message o where o.company_id=:companyid " + 
			"union all\r\n" + 
			"select f.id,f.last_modified,f.version,f.order_amount,f.plus_or_minus,f.creation from fund_details f where f.company_id=:companyid) as u order by u.creation DESC",nativeQuery=true)
	List<FundShow> findByCompanyId(@Param("companyid") Long companyid);
}
