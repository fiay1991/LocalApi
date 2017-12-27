package com.park.localapi.dao;

import java.io.Serializable;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.localapi.dao.mybatis.BaseDao;
import com.park.localapi.domain.LocalUserOnduty;

/**
 * 收费员当班DAO
 * @author fangct 
 * created on 2017年9月23日
 */
@Repository(value="localUserOndutyDao")
public interface LocalUserOndutyDao  extends BaseDao<LocalUserOnduty, Serializable>{
	/**
	 * 删除当班人员
	 * @param parkId
	 * @param userAccount
	 */
	public int deleteOnduty(@Param("parkId")String parkId, @Param("userAccount")String userAccount);
}
