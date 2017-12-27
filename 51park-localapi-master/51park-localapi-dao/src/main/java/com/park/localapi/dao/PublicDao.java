package com.park.localapi.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository(value="publicDao")
public interface PublicDao {

	int findUidByPlateNum(String license_plate);

	String findCardidByUid(int uid);

	String getLocalUrl(Map<String, String> map);

	int checkReconciliation(Map<String, String> checkReconciliationMap);

	int checkPayRecordReconciliation(String trade_no);

	void addMqRecord(Map<String, String> map);

	String findOrderNum(String local_orderid);

	int checkBillPathExist(Map<String, String> checkReconciliationMap);
	
	

}
