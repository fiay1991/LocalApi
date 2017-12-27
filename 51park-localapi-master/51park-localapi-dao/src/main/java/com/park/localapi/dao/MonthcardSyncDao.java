package com.park.localapi.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.park.localapi.dao.mybatis.BaseDao;
import com.park.localapi.domain.MonthOperRecord;
import com.park.localapi.domain.MonthProduct;
/**
 * 月卡信息同步DAO
 * @author fangct
 *
 */
@Repository(value="monthcardSyncDao")
public interface MonthcardSyncDao extends BaseDao<MonthOperRecord, Serializable>{
	
	/**
	 * 根据产品ID获取产品信息
	 * @return
	 */
	public MonthProduct getMonthProductById(int id);
}
