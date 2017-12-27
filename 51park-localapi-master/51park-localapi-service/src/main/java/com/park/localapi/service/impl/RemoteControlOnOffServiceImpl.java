package com.park.localapi.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.base.common.DataChangeTools;
import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.common.constants.SendType;
import com.park.localapi.common.constants.TimeOutConstants;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.PublicService;
import com.park.localapi.service.RemoteControlOnOffService;
import com.park.localapi.service.mq.ProducerMessageSend;
@Service(value="remoteControlOnOffService")
public class RemoteControlOnOffServiceImpl implements RemoteControlOnOffService {
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PublicService publicService;
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private ProducerMessageSend producerMessageSend;

	public String remoteControlOnOff(String params) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> paramsMap = DataChangeTools.json2Map(params);
		if (paramsMap.get("parkid") == null || paramsMap.get("deviceno") == null || 
				paramsMap.get("executetime") == null || paramsMap.get("type") == null || 
				paramsMap.get("sequenceid") == null ) {
			logger.info("必填参数无效,parmasMap:"+paramsMap);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}else{
			String parkid = paramsMap.get("parkid");
			String parkCode = parkDao.getParkCodeById(parkid);
			paramsMap.put("parkid", parkCode);
			
			String msgId = producerMessageSend.issueMQLocal(paramsMap, parkid, parkCode, null,
					SendType.远程控制开关闸.getCode(), "remoteControlOnOff");
			logger.info("***远程控制开关闸 , 通知MQ的返回结果:" + msgId);
			if (msgId == null || msgId.trim().length() == 0) {
				logger.info("***车场ID:{}, 远程控制开关闸, 未成功从MQ获取msgid.", parkid);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_409, LocalCodeConstants.getName(LocalCodeConstants.ERROR_409));
			}
			//从redis读取msgId的数据
			Map<String, String> responseMap = publicService.getValueFromRedis(msgId, TimeOutConstants.REDIS_REMOTEONOFF);
			if(responseMap != null && responseMap.size() > 0){
				responseMap.put("parkid", parkid);
				return ResultTools.setObjectResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS), responseMap);
			}else {
				logger.info("***未从本地返回结果中读取到data结果集, msgId:{}", msgId);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_409, LocalCodeConstants.getName(LocalCodeConstants.ERROR_409));
			}
		}
	}
}
