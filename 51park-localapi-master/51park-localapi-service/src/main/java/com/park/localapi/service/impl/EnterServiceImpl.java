package com.park.localapi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.base.common.ResultTools;
import com.park.base.common.domain.ObjectResponse;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.common.tools.HttpRequestTools;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.AbstractService;
import com.park.localapi.service.ProduceFactory;
import com.park.localapi.service.PublicService;
import com.park.localapi.service.config.CloudCoreURLConfig;

@Service(value = "enterService")
public class EnterServiceImpl extends AbstractService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PublicService publicService;
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private ProduceFactory produceFactory;
	@Autowired
	private CloudCoreURLConfig cloudCoreURLConfig;

	@Override
	public void setMustParams() {
		// 必传参数
		this.mustParams = new String[] { "parkid", "local_orderid", "plate", "devicetype", "entertime", "type",
				"cartype" };
	}

	@SuppressWarnings("unchecked")
	@Override
	public String executeService(Map<String, String> paramsMap) {
		if (parkDao.getParkIdByCode(paramsMap.get("parkid")) == null) {
			logger.info("请求参数 车场ID不合法 parkcode=" + paramsMap.get("parkid"));
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}
		paramsMap = publicService.initParamsMap(paramsMap);
		// 判断该进场记录是否已存在
		Map<String, String> checkoutExistingMap = new HashMap<String, String>();
		// 后期需要改为根据localorderid做校验
		checkoutExistingMap.put("plateNumber", paramsMap.get("plate"));
		checkoutExistingMap.put("parkId", paramsMap.get("parkid"));
		checkoutExistingMap.put("serviceStatus", "1");
		String checkoutExistingUrl = cloudCoreURLConfig.getFindsByCarinfoOrderInfo();
		ObjectResponse checkoutExistingResponse = HttpRequestTools.requestCloudCoreForList(checkoutExistingMap,
				checkoutExistingUrl);
		if ("404".equals(checkoutExistingResponse.getCode())) {
			// 新增车辆订单记录
			paramsMap.put("servicestatus", "1");
			Map<String, String> produceOrderInfoMap = produceFactory.produceOrderInfoMap(paramsMap);
			String orderEnterUrl = cloudCoreURLConfig.getAddOrderInfo();
			ObjectResponse orderEnterResponse = HttpRequestTools.requestCloudCore(produceOrderInfoMap, orderEnterUrl);
			if (!"200".equals(orderEnterResponse.getCode())) {
				logger.info("新增车辆订单记录失败;请求参数:" + produceOrderInfoMap);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_404,
						LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
			}
			Map<String, String> dataMap = (Map<String, String>) orderEnterResponse.getData();
			// 新增车辆进出场记录
			paramsMap.put("recordtype", "1");
			paramsMap.put("orderid", dataMap.get("orderNum"));
			paramsMap.put("image", paramsMap.get("enterimage"));
			Map<String, String> produceOrderExEnMap = produceFactory.produceOrderExEnMap(paramsMap);
			String exenRecordUrl = cloudCoreURLConfig.getAddOrderEnExRecord();
			ObjectResponse exenRecordResponse = HttpRequestTools.requestCloudCore(produceOrderExEnMap, exenRecordUrl);
			if (!"200".equals(exenRecordResponse.getCode())) {
				logger.info("新增车辆进出场记录失败;请求参数:" + produceOrderExEnMap);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_404,
						LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
			}
			// // 新增第三方平台进场纪录
			// Map<String, Object> thirdRecordMap = new HashMap<String, Object>();
			// thirdRecordMap.put("order_num", orderEnterResponseMap.get("orderNum"));
			// thirdRecordMap.put("record_type", 1);
			// thirdRecordMap.put("park_id", paramsMap.get("parkid"));
			// thirdRecordMap.put("plate_number", paramsMap.get("plate"));
			// thirdRecordMap.put("enter_name", paramsMap.get("entername"));
			// thirdRecordMap.put("is_send", 1);
			// thirdRecordMap.put("create_time", DateTools.phpnowDate());
			// thirdRecordMap.put("update_time", DateTools.phpnowDate());
			// enterDao.addEnterThirdRecord(thirdRecordMap);
			Map<String, Object> responseMap = new HashMap<String, Object>();
			responseMap.put("orderid", dataMap.get("orderNum"));
			return ResultTools.setObjectResponse(LocalCodeConstants.SUCCESS,
					LocalCodeConstants.getName(LocalCodeConstants.SUCCESS), responseMap);
		} else if ("200".equals(checkoutExistingResponse.getCode())) {
			// 重复进场
			logger.info("重复进场 请求参数:" + paramsMap);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_405,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_405));
		} else {
			return ResultTools.setResponse(LocalCodeConstants.ERROR,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
	}

}
