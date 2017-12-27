package com.park.localapi.service.impl;

import java.util.HashMap;
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
import com.park.localapi.service.AbstractService;
import com.park.localapi.service.BillInfoService;
import com.park.localapi.service.PublicService;
import com.park.localapi.service.mq.ProducerMessageSend;

@Service(value = "billInfoService")
public class BillInfoServiceImpl extends AbstractService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ParkDao parkDao;
	@Autowired
	private ProducerMessageSend producerMessageSend;
	@Autowired
	private PublicService publicService;

	@Override
	public void setMustParams() {
		// 必传参数
		this.mustParams = new String[] { "parkId", "orderNum", "plateNumber" };
	}

	@Override
	public String executeService(Map<String, String> paramsMap) {
		// 验证入参有效性
		logger.info("请求参数 :" + paramsMap);
		String parkId = paramsMap.get("parkId");
		String parkCode = parkDao.getParkCodeById(parkId);
		Map<String, Object> billInfoMap = new HashMap<String, Object>();
		billInfoMap.put("parkid", parkCode);
		billInfoMap.put("plate", paramsMap.get("plateNumber"));
		billInfoMap.put("orderid", paramsMap.get("orderNum"));
		String msgId = producerMessageSend.issueMQLocal(billInfoMap, parkId, parkCode, null, SendType.缴费查询.getCode(),
				"billInfo");
		logger.info("***缴费查询 , 通知MQ的返回结果:" + msgId);
		if (msgId == null || msgId.trim().length() == 0) {
			logger.info("***订单号:{}, 缴费查询, 未成功从MQ获取msgid..", paramsMap.get("orderNum"));
			return ResultTools.setResponse(LocalCodeConstants.ERROR_404,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		}
		// 从redis读取msgId的数据
		Map<String, String> responseMap = publicService.getValueFromRedis(msgId, TimeOutConstants.REDIS_BILLINFO);
		if (responseMap != null && responseMap.size() > 0) {
			return ResultTools.setObjectResponse(LocalCodeConstants.SUCCESS,
					LocalCodeConstants.getName(LocalCodeConstants.SUCCESS), responseMap);
		} else {
			logger.info("***未从本地返回结果中读取到data结果集, msgId:{}", msgId);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_404,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		}

	}
}
