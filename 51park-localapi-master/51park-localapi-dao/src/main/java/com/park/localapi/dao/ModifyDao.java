package com.park.localapi.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository(value="modifyDao")
public interface ModifyDao {

	void updateThirdRecord(Map<String, Object> thirdRecordMap);

	

}
