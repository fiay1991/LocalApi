package com.park.localapi.service;

import java.util.Map;

public interface SendStatusService {
	/**
	 * 更新下发业务数据的状态
	 * @param parmasMap
	 * @param code
	 * @return
	 */
	public String updateStatus(Map<String, String> parmasMap, String code, String parkid);
	/**
	 * 添加MQ请求记录	
	 * @param parkid
	 * @param businessKey
	 * @param businessCode
	 * @param topic
	 * @param key
	 * @param body
	 * @param msgId
	 */
	public void addMqRecord(String parkid, String businessKey, String businessCode, String topic, String key, String body, String msgId);
	/**
	 * 更新本地返回结果
	 * @param params
	 * @param msgid
	 * @param parkid
	 * @param type
	 */
	public int backUpdate(String params, String msgid, String parkid, String type);
}
