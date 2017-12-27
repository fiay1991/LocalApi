package com.park.localapi.dao;

import org.springframework.stereotype.Repository;

@Repository(value="billInfoDao")
public interface BillInfoDao {

	String findParkkeyByParkid(String parkcode);



}
