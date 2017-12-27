package com.park.localapi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.base.common.DataChangeTools;
import com.park.base.common.ToolsUtil;
import com.park.localapi.dao.NoPlateRecordDao;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.domain.NoPlateRecord;
import com.park.localapi.service.SendService;
import com.park.localapi.service.mq.ProducerMessageSend;

@Service
public class NoPlateEnterServiceImpl implements SendService{

	@Autowired
	private ParkDao parkDao;
	@Autowired
	private ProducerMessageSend producerMessageSend;
	@Autowired
	private NoPlateRecordDao noPlateRecordDao;
	
	public String send(String params) {
		Map<String, String> paramsMap = DataChangeTools.json2Map(params);
		
		int id = ToolsUtil.parseInt(paramsMap.get("businessKey"));
		String businessCode = (String)paramsMap.get("businessCode");
		NoPlateRecord record = noPlateRecordDao.select(id);
		
		String parkId = (String)paramsMap.get("parkId");
		// 进场下发
		Map<String, Object> enterRecord = new HashMap<String, Object>();
		String parkCode = parkDao.getParkCodeById(parkId);
		enterRecord.put("parkid", parkCode);
		enterRecord.put("channelid", record.getEn_channel_id());
		enterRecord.put("plate", record.getTemp_plate());
		enterRecord.put("entertime", record.getEnter_time());
		return producerMessageSend.issueMQLocal(enterRecord, parkId, parkCode, null, businessCode, "noPlateEnter");
	}

}
