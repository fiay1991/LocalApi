package com.park.localapi.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ProduceFactory {

	/**
	 * 装载OrderInfo的Map对象
	 * 
	 * @param parmasMap
	 * @return
	 */
	public Map<String, String> produceOrderInfoMap(Map<String, String> paramsMap) {
		Map<String, String> orderInfoMap = new HashMap<String, String>();
		orderInfoMap.put("orderNum", paramsMap.get("orderid"));
		orderInfoMap.put("parkId", paramsMap.get("parkid"));
		orderInfoMap.put("plateNumber", paramsMap.get("plate"));
		orderInfoMap.put("cardId", paramsMap.get("cardid"));
		orderInfoMap.put("ticketId", paramsMap.get("ticketid"));
		orderInfoMap.put("carType", paramsMap.get("cartype"));
		orderInfoMap.put("enterTime", paramsMap.get("entertime"));
		orderInfoMap.put("exitTime", paramsMap.get("exittime"));
		orderInfoMap.put("costBefore", paramsMap.get("total_amount"));
		orderInfoMap.put("costAfter", paramsMap.get("paid_amount"));
		orderInfoMap.put("discountAmount", paramsMap.get("discount_amount"));
		orderInfoMap.put("type", paramsMap.get("type"));
		orderInfoMap.put("carDesc", paramsMap.get("vehicledesc"));
		orderInfoMap.put("serviceStatus", paramsMap.get("servicestatus"));
		orderInfoMap.put("localOrderId", paramsMap.get("local_orderid"));
		return orderInfoMap;
	}

	/**
	 * 装载OrderExEn的Map对象
	 * 
	 * @param parmasMap
	 * @return
	 */
	public Map<String, String> produceOrderExEnMap(Map<String, String> paramsMap) {
		Map<String, String> orderExEnMap = new HashMap<String, String>();
		orderExEnMap.put("recordType", paramsMap.get("recordtype"));
		orderExEnMap.put("orderNum", paramsMap.get("orderid"));
		orderExEnMap.put("cardId", paramsMap.get("cardid"));
		orderExEnMap.put("parkId", paramsMap.get("parkid"));
		orderExEnMap.put("deviceType", paramsMap.get("devicetype"));
		orderExEnMap.put("enterTime", paramsMap.get("entertime"));
		orderExEnMap.put("exitTime", paramsMap.get("exittime"));
		orderExEnMap.put("plateNumber", paramsMap.get("plate"));
		orderExEnMap.put("carBrand", paramsMap.get("carbrand"));
		orderExEnMap.put("carColor", paramsMap.get("carcolor"));
		orderExEnMap.put("carDesc", paramsMap.get("vehicledesc"));
		orderExEnMap.put("enterName", paramsMap.get("entername"));
		orderExEnMap.put("exitName", paramsMap.get("exitname"));
		orderExEnMap.put("image", paramsMap.get("image"));
		orderExEnMap.put("type", paramsMap.get("type"));
		orderExEnMap.put("carType", paramsMap.get("cartype"));
		return orderExEnMap;
	}

}
