package com.park.localapi.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.park.localapi.dao.mybatis.BaseDao;
import com.park.localapi.domain.LoginOutRecord;

@Repository(value="loginOutDao")
public interface LoginOutDao  extends BaseDao<LoginOutRecord, Serializable>{
	
}
