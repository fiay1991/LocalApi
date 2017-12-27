package com.park.localapi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.base.common.DateTools;
import com.park.base.common.ResultTools;
import com.park.base.common.domain.ObjectResponse;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.common.constants.StatusConstants;
import com.park.localapi.common.tools.HttpRequestTools;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.AbstractService;
import com.park.localapi.service.PublicService;
import com.park.localapi.service.config.CloudCoreURLConfig;

@Service(value = "modifyService")
public class ModifyServiceImpl extends AbstractService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PublicService publicService;
	@Autowired
	private CloudCoreURLConfig cloudCoreURLConfig;
	@Autowired
	private ParkDao parkDao;

	@SuppressWarnings("unchecked")
	@Override
	public String executeService(Map<String, String> parmasMap) {
		if (Integer.parseInt(parmasMap.get("time")) < 1483200000) {// 2017/01/01 00:00:00
			logger.info("请求参数 时间不合法 time=" + parmasMap.get("time"));
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}
		if (parkDao.getParkIdByCode(parmasMap.get("parkid")) == null) {
			logger.info("请求参数 车场ID不合法 parkcode=" + parmasMap.get("parkid"));
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}
		parmasMap = publicService.initParamsMap(parmasMap);
		try {
			// 查询在场车辆订单详情
			Map<String, String> checkoutExistingMap = new HashMap<String, String>();
			checkoutExistingMap.put("orderNum", parmasMap.get("orderid"));
			String checkoutExistingUrl = cloudCoreURLConfig.getFindByOneOrderInfo();
			ObjectResponse checkoutExistingResponse = HttpRequestTools.requestCloudCore(checkoutExistingMap,
					checkoutExistingUrl);
			if (!"200".equals(checkoutExistingResponse.getCode())) {
				logger.info("查询在场车辆订单详情失败;请求参数:" + checkoutExistingMap);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_404,
						LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
			}
			logger.info("请求CloudCore根据唯一条件查询订单详情 请求参数:" + checkoutExistingMap + "返回结果:" + checkoutExistingResponse);
			Map<String, String> checkoutExistingResponseMap = (Map<String, String>) checkoutExistingResponse.getData();
			// 判断事件类型
			if (parmasMap.get("action").equals("1")) {// 修改事件
				// 修改进出场记录
				Map<String, String> exenRecordMap = new HashMap<String, String>();
				exenRecordMap.put("parkId", parmasMap.get("parkid"));
				exenRecordMap.put("orderNum", parmasMap.get("orderid"));
				exenRecordMap.put("plateNumber", parmasMap.get("plate"));
				exenRecordMap.put("userAccount", parmasMap.get("useraccount"));
				String exenRecordUrl = cloudCoreURLConfig.getModOrderEnExRecord();
				ObjectResponse exenRecordResponse = HttpRequestTools.requestCloudCore(exenRecordMap, exenRecordUrl);
				if (!"200".equals(exenRecordResponse.getCode())) {
					logger.info("修改进出场记录失败;请求参数:" + exenRecordMap);
				}
				logger.info("请求CloudCore修改车辆进出场记录 请求参数:" + exenRecordMap + " 返回结果" + exenRecordResponse);
				// 修改在场订单记录
				Map<String, String> updateOrderEnterMap = new HashMap<String, String>();
				updateOrderEnterMap.put("orderNum", parmasMap.get("orderid"));
				updateOrderEnterMap.put("plateNumber", parmasMap.get("plate"));
				updateOrderEnterMap.put("type", parmasMap.get("type"));
				updateOrderEnterMap.put("carType", parmasMap.get("cartype"));
				updateOrderEnterMap.put("carDesc", parmasMap.get("vehicledesc"));
				String updateOrderEnterUrl = cloudCoreURLConfig.getModBaseOrderInfo();
				ObjectResponse updateOrderEnterResponse = HttpRequestTools.requestCloudCore(updateOrderEnterMap,
						updateOrderEnterUrl);
				if (!"200".equals(updateOrderEnterResponse.getCode())) {
					logger.info("修改订单记录失败;请求参数:" + updateOrderEnterMap);
				}
				logger.info("请求CloudCore修改车辆进在场记录 请求参数:" + updateOrderEnterMap + " 返回结果" + updateOrderEnterResponse);
				// // 修改第三方平台记录
				// Map<String, Object> thirdRecordMap = new HashMap<String, Object>();
				// thirdRecordMap.put("order_num", parmasMap.get("orderid"));
				// thirdRecordMap.put("plate_number", parmasMap.get("plate"));
				// thirdRecordMap.put("update_time", DateTools.phpnowDate());
				// modifyDao.updateThirdRecord(thirdRecordMap);
				// 储存 修改记录
				Map<String, String> historyMap = new HashMap<String, String>();
				historyMap.put("action", "1");
				historyMap.put("parkId", parmasMap.get("parkid"));
				historyMap.put("orderNum", parmasMap.get("orderid"));
				historyMap.put("oldVehicledesc", checkoutExistingResponseMap.get("carDesc"));
				historyMap.put("newVehicledesc", parmasMap.get("vehicledesc"));
				historyMap.put("modifyTime", "" + DateTools.phpnowDate());
				historyMap.put("useraccount", parmasMap.get("useraccount"));
				// 车牌改动
				if (!checkoutExistingResponseMap.get("plateNumber").equals(parmasMap.get("plate"))) {
					historyMap.put("modifyType", "1");
					historyMap.put("beforeModify", checkoutExistingResponseMap.get("plateNumber"));
					historyMap.put("afterModify", parmasMap.get("plate"));
					String historyUrl = cloudCoreURLConfig.getAddOrderModifyRecord();
					ObjectResponse historyResponse = HttpRequestTools.requestCloudCore(historyMap, historyUrl);
					if (!"200".equals(historyResponse.getCode())) {
						logger.info("车牌改动储存失败;请求参数:" + historyMap);
					}
				}
				// 车型改动
				if (!checkoutExistingResponseMap.get("carType").equals(parmasMap.get("cartype"))) {
					historyMap.put("modifyType", "2");
					historyMap.put("beforeModify", checkoutExistingResponseMap.get("carType"));
					historyMap.put("afterModify", parmasMap.get("cartype"));
					String historyUrl = cloudCoreURLConfig.getAddOrderModifyRecord();
					ObjectResponse historyResponse = HttpRequestTools.requestCloudCore(historyMap, historyUrl);
					if (!"200".equals(historyResponse.getCode())) {
						logger.info("车型改动储存失败;请求参数:" + historyMap);
					}
				}
				// 车辆类型改动
				if (!checkoutExistingResponseMap.get("type").equals(parmasMap.get("type"))) {
					historyMap.put("modifyType", "3");
					historyMap.put("beforeModify", checkoutExistingResponseMap.get("type"));
					historyMap.put("afterModify", parmasMap.get("type"));
					String historyUrl = cloudCoreURLConfig.getAddOrderModifyRecord();
					ObjectResponse historyResponse = HttpRequestTools.requestCloudCore(historyMap, historyUrl);
					if (!"200".equals(historyResponse.getCode())) {
						logger.info("车辆类型改动储存失败;请求参数:" + historyMap);
					}
				}
				Map<String, Object> responseMap = new HashMap<String, Object>();
				responseMap.put("orderid", parmasMap.get("orderid"));
				return ResultTools.setObjectResponse(LocalCodeConstants.SUCCESS,
						LocalCodeConstants.getName(LocalCodeConstants.SUCCESS), responseMap);
			} else if (parmasMap.get("action").equals("2")) {// 删除事件
				// 修改订单为取消
				Map<String, String> updateInfoMap = new HashMap<String, String>();
				updateInfoMap.put("orderNum", parmasMap.get("orderid"));
				updateInfoMap.put("serviceStatus", StatusConstants.SERVICE_CONCEL);
				String updateInfoUrl = cloudCoreURLConfig.getModBaseOrderInfo();
				ObjectResponse updateInfoResponse = HttpRequestTools.requestCloudCore(updateInfoMap, updateInfoUrl);
				if (!"200".equals(updateInfoResponse.getCode())) {
					logger.info("修改订单状态失败;请求参数:" + updateInfoMap);
				}
				// 储存 删除记录
				Map<String, String> historyMap = new HashMap<String, String>();
				historyMap.put("action", "2");
				historyMap.put("parkId", parmasMap.get("parkid"));
				historyMap.put("orderNum", parmasMap.get("orderid"));
				historyMap.put("modifyType", "1");
				historyMap.put("beforeModify", checkoutExistingResponseMap.get("plateNumber"));
				historyMap.put("afterModify", parmasMap.get("plate"));
				historyMap.put("oldVehicledesc", checkoutExistingResponseMap.get("car_desc"));
				historyMap.put("newVehicledesc", parmasMap.get("vehicledesc"));
				historyMap.put("modifyTime", "" + DateTools.phpnowDate());
				historyMap.put("useraccount", parmasMap.get("useraccount"));
				String historyUrl = cloudCoreURLConfig.getAddOrderModifyRecord();
				ObjectResponse historyResponse = HttpRequestTools.requestCloudCore(historyMap, historyUrl);
				if (!"200".equals(historyResponse.getCode())) {
					logger.info("修改订单记录失败;请求参数:" + historyMap);
				}
				Map<String, Object> responseMap = new HashMap<String, Object>();
				responseMap.put("orderid", parmasMap.get("orderid"));
				return ResultTools.setObjectResponse(LocalCodeConstants.SUCCESS,
						LocalCodeConstants.getName(LocalCodeConstants.SUCCESS), responseMap);
			} else {
				return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
						LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultTools.setResponse(LocalCodeConstants.ERROR_409,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_409));
		}
	}

	@Override
	public void setMustParams() {
		// 必传参数
		this.mustParams = new String[] { "parkid", "orderid", "plate", "type", "cartype", "action", "time" };

	}

}
