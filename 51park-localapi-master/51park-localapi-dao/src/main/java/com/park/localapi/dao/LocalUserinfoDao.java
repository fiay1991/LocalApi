package com.park.localapi.dao;

import java.io.Serializable;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.park.localapi.dao.mybatis.BaseDao;
import com.park.localapi.domain.LocalUserinfoRecord;
/**
 * 本地用户信息DAO
 * @author fangct
 *
 */
@Repository(value="localUserinfoDao")
public interface LocalUserinfoDao extends BaseDao<LocalUserinfoRecord, Serializable>{
	
	/**
	 * 查询该用户是否存在
	 * @param userAccount 账号
	 * @param parkId 车场ID
	 * @return
	 */
	public int existUserAccount(@Param("userAccount")String userAccount,
			@Param("parkId")String parkId);
}
