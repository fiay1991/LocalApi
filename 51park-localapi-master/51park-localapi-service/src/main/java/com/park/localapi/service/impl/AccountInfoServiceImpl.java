package com.park.localapi.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.park.base.common.DataChangeTools;
import com.park.base.common.ResultTools;
import com.park.base.common.ToolsUtil;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.dao.LocalUserinfoDao;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.domain.LocalUserinfoRecord;
import com.park.localapi.domain.request.AccountInfoRequest;
import com.park.localapi.service.AccountInfoService;
import com.park.localapi.service.mq.ProducerMessageSend;

@Service(value = "accountInfoService")
public class AccountInfoServiceImpl implements AccountInfoService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "localUserinfoDao")
	private LocalUserinfoDao localUserinfoDao;
	@Resource(name = "parkDao")
	private ParkDao parkDao;
	@Resource(name = "ProducerMessageSend")
	private ProducerMessageSend producerMessageSend;

	public String accountInfoSync(String params) {
		Map<String, String> paramsMap = DataChangeTools.json2Map(params);
		String parkId = paramsMap.get("parkId");
		String businessKey = paramsMap.get("businessKey");
		String businessCode = paramsMap.get("businessCode");
		LocalUserinfoRecord userinfoRecord = localUserinfoDao.get(LocalUserinfoRecord.class, businessKey);

		if (userinfoRecord == null) {
			logger.info("***根据ID: {}, 未查询到人员信息操作记录 ", businessKey);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		} else {
			if (userinfoRecord.getPark_id() != ToolsUtil.parseInt(parkId)) {
				logger.info("***数据库表中的车场ID {} 和参数中的车场ID {} 不相同 ", userinfoRecord.getPark_id(), parkId);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_402, LocalCodeConstants.getName(LocalCodeConstants.ERROR_402));
			}
			/*
			 * 封装请求本地的对象
			 */
			String parkCode = parkDao.getParkCodeById(parkId);
			AccountInfoRequest requestObj = buildRequest(userinfoRecord, parkCode);

			// 将数据下发到MQ
			return producerMessageSend.issueMQLocal(requestObj, parkId, parkCode, businessKey, businessCode,
					"accountInfo");
		}
	}

	/**
	 * 生成请求对象
	 * 
	 * @param userinfoRecord
	 * @param parkCode
	 * @return
	 */
	private AccountInfoRequest buildRequest(LocalUserinfoRecord userinfoRecord, String parkCode) {
		AccountInfoRequest requestObj = new AccountInfoRequest();
		requestObj.setParkid(parkCode);
		requestObj.setUseraccount(userinfoRecord.getUser_account());
		requestObj.setUserpassword(userinfoRecord.getUser_password());
		requestObj.setUsername(userinfoRecord.getUser_name());
		requestObj.setOptype(userinfoRecord.getOptype());
		requestObj.setOptime(userinfoRecord.getOptime());
		requestObj.setUsertype(userinfoRecord.getUser_type());
		return requestObj;
	}

}
