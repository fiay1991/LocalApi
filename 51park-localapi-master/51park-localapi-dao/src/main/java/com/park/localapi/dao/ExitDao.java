package com.park.localapi.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository(value="exitDao")
public interface ExitDao {

	void addThirdRecord(Map<String, Object> thirdRecordMap);

	void addUnusualOrder(Map<String, String> exceptionMap);

	void addExceptionOrder(Map<String, String> exceptionMap);

	int checkOutUnusualOrder(Map<String, String> ckMap);

	void updateUnusualOrder(Map<String, String> exceptionMap);

}
