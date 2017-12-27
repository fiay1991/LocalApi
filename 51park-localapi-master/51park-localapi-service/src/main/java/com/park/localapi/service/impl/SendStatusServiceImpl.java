package com.park.localapi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.park.base.common.DataChangeTools;
import com.park.base.common.HttpTools;
import com.park.base.common.RSATools;
import com.park.base.common.domain.ObjectResponse;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.dao.SendStatusDao;
import com.park.localapi.service.PublicService;
import com.park.localapi.service.SendStatusService;
import com.park.localapi.service.config.KeysConfig;
import com.park.localapi.service.config.TimeTaskURLConfig;
@Repository(value="sendStatusServiceImpl")
public class SendStatusServiceImpl implements SendStatusService {
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SendStatusDao sendStatusDao;
	@Autowired
	private PublicService publicService;
	@Autowired
	private KeysConfig keysConfig;
	@Autowired
	private TimeTaskURLConfig timeTaskURLConfig;
	
	public String updateStatus(Map<String, String> parmasMap, String code, String parkid) {
		String msgid = parmasMap.get("MessageID");
		String type = parmasMap.get("Type");
		try {
			//更新本地返回的结果
			int n =backUpdate(DataChangeTools.bean2gson(parmasMap), msgid, parkid, type);
			if(n == 0){
				logger.info("***未找到对应msgid的记录, msgid:{}", msgid);
				return "FAIL";
			}
			
			//查询业务信息
			Map<String, Object> businessInfo = sendStatusDao.getBusinessInfo(msgid, parkid, type);
			
			/*
			 *** 请求TimeTask，通知业务处理结果 
			 */
			Map<String, String> sendRequest = new HashMap<String, String>();
			sendRequest.put("parkId", parkid);
			sendRequest.put("businessKey", ""+businessInfo.get("businessKey"));
			sendRequest.put("businessCode", ""+businessInfo.get("businessCode"));
			if(LocalCodeConstants.SUCCESS.equals(code)){
				sendRequest.put("result", "1");
			}else{
				sendRequest.put("result", "2");
			}
			
			//请求参数
			String json = DataChangeTools.bean2gson(sendRequest);
		
			Map<String, String> header = publicService.getHeaders(json);
			String url = timeTaskURLConfig.getSendTaskResult();
			String responseJson = HttpTools.HttpClientPost(url, RSATools.encrpyt(json, keysConfig.getPublic_key()), header);
			logger.info("***TimeTask返回结果: {}", responseJson);
			if(responseJson==null){
				logger.info("***TimeTask返回为空, msgid:{}", msgid);
				return "FAIL";
			}else{
				ObjectResponse response = DataChangeTools.gson2bean(responseJson, ObjectResponse.class);
				if(LocalCodeConstants.SUCCESS.equals(response.getCode())){
					logger.info("***TimeTask返回处理成功, msgid:{}", msgid);
					return "SUCCESS";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "FAIL";
		}
		logger.info("失败");
		return "FAIL";
	}
	
	public void addMqRecord(String parkId, String businessKey, String businessCode, String topic, String key, String dataMap_Json, String msgId) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("park_id", parkId);
		map.put("business_key", businessKey);
		map.put("business_code", businessCode);
		map.put("topic", topic);
		map.put("key", key);
		map.put("body", dataMap_Json);
		map.put("message_id", msgId);
		sendStatusDao.addMqRecord(map);
	}

	public int backUpdate(String params, String msgid, String parkid, String type){
		Map<String, String> map = new HashMap<String, String>();
		/*
		 *** 回填本地返回的业务结果
		 */
		map.put("park_id", parkid);
		map.put("message_id", msgid);
		map.put("business_code", type);
		map.put("result", params);
		try {
			//更新本地返回的结果
			return sendStatusDao.updateResult(map);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("***保存本地返回结果失败, msgid:{}", msgid);
			return 0;
		}
	}
}
