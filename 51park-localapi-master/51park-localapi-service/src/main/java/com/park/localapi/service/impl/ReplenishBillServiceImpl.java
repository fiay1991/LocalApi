package com.park.localapi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.park.base.common.DataChangeTools;
import com.park.base.common.ResultTools;
import com.park.base.common.ToolsUtil;
import com.park.base.common.domain.ObjectResponse;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.common.tools.HttpRequestTools;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.AbstractService;
import com.park.localapi.service.ProduceFactory;
import com.park.localapi.service.PublicService;
import com.park.localapi.service.config.CloudCoreURLConfig;

@Service(value = "replenishBillService")
public class ReplenishBillServiceImpl extends AbstractService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PublicService publicService;
	@Autowired
	private CloudCoreURLConfig cloudCoreURLConfig;
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private ProduceFactory produceFactory;
	@Autowired
	private ExitServiceImpl exitService;
	

	@Override
	public void setMustParams() {
		// 必传参数
		this.mustParams = new String[] { "parkid", "local_orderid", "plate", "exittime", "total_amount", "payed_amount",
				"discount_amount", "useraccount", "cartype", "type", "entertime", "devicetype", "exittime" };
	}

	@SuppressWarnings("unchecked")
	@Override
	public String executeService(Map<String, String> paramsMap) {
		JSONArray paidinfoArray = new JSONArray();
		JSONArray discountInfoArray = new JSONArray();
		// 补报时是否限制时间待定
		// if (Integer.parseInt(paramsMap.get("exittime")) < 1483200000) {// 2017/01/01
		// 00:00:00
		// logger.info("请求参数 出场时间不合法 exittime=" + paramsMap.get("exittime"));
		// return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
		// LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		// }
		// if (Integer.parseInt(paramsMap.get("entertime")) < 1483200000) {// 2017/01/01
		// 00:00:00
		// logger.info("请求参数 进场时间不合法 entertime=" + paramsMap.get("entertime"));
		// return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
		// LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		// }

		if (parkDao.getParkIdByCode(paramsMap.get("parkid")) == null) {
			logger.info("请求参数 车场ID不合法 parkcode=" + paramsMap.get("parkid"));
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}
		if (!"0".equals(paramsMap.get("total_amount"))) {
			if (paramsMap.get("paidinfo") == null) {
				logger.info("请求参数 无支付明细;请求参数为:" + paramsMap);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
						LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
			} else {
				// 解析并循环支付明细
				String paidinfo = paramsMap.get("paidinfo");
				paidinfoArray = (JSONArray) JSONArray.parse(paidinfo);
				for (int i = 0; i < paidinfoArray.size(); i++) {
					JSONObject jbo = (JSONObject) paidinfoArray.get(i);
					if (jbo.get("tradeno") == null || jbo.get("total_price") == null || jbo.get("paid_price") == null
							|| jbo.get("discount_price") == null || jbo.get("pay_way") == null
							|| jbo.get("pay_channel") == null || jbo.get("pay_time") == null) {
						logger.info("二级请求参数有误;请求参数为:" + paidinfo);
						return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
								LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
					} else {
						if (!"0".equals(jbo.get("discount_price"))) {
							if (jbo.get("discountInfo") == null) {
								logger.info("请求参数 无优惠明细;");
								return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
										LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
							} else {
								Object discountInfoString = jbo.get("discountInfo");
								String bean2gson = DataChangeTools.bean2gson(discountInfoString);
								JSONArray discountArray = (JSONArray) JSONArray.parse(bean2gson);
								for (int j = 0; j < discountArray.size(); j++) {
									JSONObject discountjbo = (JSONObject) discountArray.get(j);
									if (ToolsUtil.isNull(discountjbo.get("discount_no"))
											|| ToolsUtil.isNull(discountjbo.get("discount_amount"))
											|| ToolsUtil.isNull(discountjbo.get("discount_time"))) {
										logger.info("三级请求参数有误;请求参数为:" + discountArray);
										return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
												LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
									} else {
										discountInfoArray.add(jbo.get("discountInfo"));
									}
								}
							}
						}
					}
				}
			}
		}
		
		paramsMap = publicService.initParamsMap(paramsMap);
		String orderNum = PublicService.generateOrderNum();
		// 查询出场记录
		Map<String, String> checkoutExistingMap = new HashMap<String, String>();
		checkoutExistingMap.put("localOrderId", paramsMap.get("local_orderid"));
		String checkoutExistingUrl = cloudCoreURLConfig.getFindByOneOrderInfo();
		ObjectResponse checkoutExistingResponse = HttpRequestTools.requestCloudCore(checkoutExistingMap,
				checkoutExistingUrl);
		if (!"404".equals(checkoutExistingResponse.getCode())) {
			logger.info("重复出场,参数为:" + paramsMap);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_405,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_405));
		}
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
		// (补进场)
		paramsMap.put("recordtype", "1");
		paramsMap.put("orderid", dataMap.get("orderNum"));
		paramsMap.put("image", paramsMap.get("enterimage"));
		Map<String, String> produceOrderExEnEnterMap = produceFactory.produceOrderExEnMap(paramsMap);
		String exenEnterRecordUrl = cloudCoreURLConfig.getAddOrderEnExRecord();
		ObjectResponse exenRecordResponse = HttpRequestTools.requestCloudCore(produceOrderExEnEnterMap, exenEnterRecordUrl);
		if (!"200".equals(exenRecordResponse.getCode())) {
			logger.info("新增车辆进出场记录失败;请求参数:" + produceOrderExEnEnterMap);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_404,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		}
		// (补出场)
		exitService.toExit(paramsMap, paidinfoArray, discountInfoArray);
		// // 新增第三方平台进场信息
		// Map<String, Object> thirdRecordMap = new HashMap<String, Object>();
		// thirdRecordMap.put("order_num", orderNum);
		// thirdRecordMap.put("record_type", 1);
		// thirdRecordMap.put("park_id", paramsMap.get("parkid"));
		// thirdRecordMap.put("plate_number", paramsMap.get("plate"));
		// thirdRecordMap.put("enter_name", paramsMap.get("entername"));
		// thirdRecordMap.put("is_send", 1);
		// thirdRecordMap.put("create_time", DateTools.phpnowDate());
		// enterDao.addEnterThirdRecord(thirdRecordMap);
		// // 新增第三方平台出场信息
		// Map<String, Object> exThirdRecordMap = new HashMap<String, Object>();
		// exThirdRecordMap.put("order_num", orderNum);
		// exThirdRecordMap.put("record_type", "2");
		// exThirdRecordMap.put("park_id", paramsMap.get("parkid"));
		// exThirdRecordMap.put("plate_number", paramsMap.get("plate"));
		// exThirdRecordMap.put("exit_name", paramsMap.get("exitname"));
		// exThirdRecordMap.put("amount", paramsMap.get("total_amount"));
		// exThirdRecordMap.put("discount_time", paramsMap.get("discount_amount"));
		// exThirdRecordMap.put("pay_method", "2");
		// exThirdRecordMap.put("is_send", "1");
		// exThirdRecordMap.put("create_time", DateTools.phpnowDate());
		// exitDao.addThirdRecord(exThirdRecordMap);
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("orderid", orderNum);
		responseMap.put("local_orderid", paramsMap.get("local_orderid"));
		return ResultTools.setObjectResponse(LocalCodeConstants.SUCCESS,
				LocalCodeConstants.getName(LocalCodeConstants.SUCCESS), responseMap);
	}

}
