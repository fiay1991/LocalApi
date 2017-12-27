package com.park.localapi.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.park.localapi.domain.ExenRecord;
import com.park.localapi.domain.OrderInfo;
import com.park.localapi.domain.OrderParkLocal;

@Repository(value="enterDao")
public interface EnterDao {

	ExenRecord checkoutExisting(Map<String, String> parmasMap);

	void addOrderInfo(OrderInfo orderInfo);

	void addlocalOrder(OrderParkLocal localOrder);

	void addEnterThirdRecord(Map<String, Object> thirdRecordMap);

}
