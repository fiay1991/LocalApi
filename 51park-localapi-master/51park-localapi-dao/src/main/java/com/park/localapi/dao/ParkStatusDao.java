package com.park.localapi.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository(value="parkStatusDao")
public interface ParkStatusDao {

	void addStatusInfo(Map<String, Object> parkStatusMap);
	
	void updateParkCarnum(Map<String, Object> parkCarnumMap);

	Map<String, Object> checkoutExisting(String parkid);

	void updateStatusInfo(Map<String, Object> parkStatusMap);

	void addOffLine(Map<String, Object> offLineMap);



}
