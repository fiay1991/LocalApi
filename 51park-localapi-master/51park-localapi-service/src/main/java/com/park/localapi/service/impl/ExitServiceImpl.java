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

@Service(value = "exitService")
public class ExitServiceImpl extends AbstractService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PublicService publicService;
	@Autowired
	private CloudCoreURLConfig cloudCoreURLConfig;
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private ProduceFactory produceFactory;

	@Override
	public void setMustParams() {
		// 必传参数
		this.mustParams = new String[] { "parkid", "local_orderid", "plate", "orderid", "exittime", "total_amount",
				"paid_amount", "discount_amount", "useraccount" };
	}

	@SuppressWarnings("unchecked")
	@Override
	public String executeService(Map<String, String> params) {
		JSONArray paidinfoArray = new JSONArray();
		JSONArray discountInfoArray = new JSONArray();
		if (Integer.parseInt(params.get("exittime")) < 1483200000) {// 2017/01/01 00:00:00
			logger.info("请求参数 时间戳不合法 exittime=" + params.get("exittime"));
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}
		if (parkDao.getParkIdByCode(params.get("parkid")) == null) {
			logger.info("请求参数 车场ID不合法 parkcode=" + params.get("parkid"));
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}
		if (!"0".equals(params.get("total_amount"))) {
			if (params.get("paidinfo") == null) {
				logger.info("请求参数 无支付明细;请求参数为:" + params);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
						LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
			} else {
				// 解析并循环支付明细
				String paidinfo = params.get("paidinfo");
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
		params = publicService.initParamsMap(params);
		// 判断是否重复离场
		Map<String, String> checkoutExistingMap = new HashMap<String, String>();
		checkoutExistingMap.put("orderNum", params.get("orderid"));
		String checkoutExistingUrl = cloudCoreURLConfig.getFindByOneOrderInfo();
		ObjectResponse checkoutExistingResponse = HttpRequestTools.requestCloudCore(checkoutExistingMap,
				checkoutExistingUrl);
		if ("404".equals(checkoutExistingResponse.getCode())) {
			return ResultTools.setResponse(LocalCodeConstants.ERROR_404,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		}
		if ("200".equals(checkoutExistingResponse.getCode())) {
			Map<String, String> checkoutExistingData = (Map<String, String>) checkoutExistingResponse.getData();
			if ("2".equals(checkoutExistingData.get("serviceStatus"))) {
				logger.info("重复离场;重复请求参数:" + params);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_405,
						LocalCodeConstants.getName(LocalCodeConstants.ERROR_405));
			} else {
				return toExit(params, paidinfoArray, discountInfoArray);
			}
		} else {
			logger.info("请求订单子系统返回错误:" + checkoutExistingResponse);
			return ResultTools.setResponse(LocalCodeConstants.ERROR,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
	}

	/**
	 * 出场业务
	 * 
	 * @param params
	 * @param discountInfoArray
	 * @param paidinfoArray
	 * @param checkoutEnter
	 * @return
	 * @throws Exception
	 */
	public String toExit(Map<String, String> params, JSONArray paidinfoArray, JSONArray discountInfoArray) {
		// 新增车辆进出场记录
		params.put("recordtype", "2");
		params.put("image", params.get("exitimage"));
		Map<String, String> produceOrderExEnMap = produceFactory.produceOrderExEnMap(params);
		String exenRecordUrl = cloudCoreURLConfig.getAddOrderEnExRecord();
		ObjectResponse exenRecordResponse = HttpRequestTools.requestCloudCore(produceOrderExEnMap, exenRecordUrl);
		// 结果判断
		if (!"200".equals(exenRecordResponse.getCode())) {
			return ResultTools.setResponse(LocalCodeConstants.ERROR,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
		// 修改主订单cp_order_info
		Map<String, String> updateOrderInfoMap = produceFactory.produceOrderInfoMap(params);
		String orderInfoUrl = cloudCoreURLConfig.getModCostOrderInfo();
		ObjectResponse updateOrderInfoResponse = HttpRequestTools.requestCloudCore(updateOrderInfoMap, orderInfoUrl);
		// 结果判断
		if (!"200".equals(updateOrderInfoResponse.getCode())) {
			return ResultTools.setResponse(LocalCodeConstants.ERROR,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
		// 添加子订单
		if (paidinfoArray.size() != 0 || paidinfoArray != null) {
			for (int i = 0; i < paidinfoArray.size(); i++) {
				JSONObject jbo = (JSONObject) paidinfoArray.get(i);
				String tradeno = (String) jbo.get("tradeno");
				Map<String, String> paidInfoMap = new HashMap<String, String>();
				paidInfoMap.put("tradeNo", tradeno);
				String paidInfoUrl = cloudCoreURLConfig.getFindOrderPayRecord();
				ObjectResponse paidInfoResponse = HttpRequestTools.requestCloudCore(paidInfoMap, paidInfoUrl);
				if (!"404".equals(paidInfoResponse.getCode())) {

				} else {
					Map<String, String> payRecordMap = new HashMap<String, String>();
					payRecordMap.put("orderNum", params.get("orderid"));
					payRecordMap.put("parkId", params.get("parkid"));
					payRecordMap.put("tradeNo", "" + jbo.get("tradeno"));
					payRecordMap.put("costBefore", "" + jbo.get("total_price"));
					payRecordMap.put("costAfter", "" + jbo.get("paid_price"));
					payRecordMap.put("discountAmount", "" + jbo.get("discount_price"));
					payRecordMap.put("payStatus", "2");
					payRecordMap.put("payWay", "" + jbo.get("pay_way"));
					payRecordMap.put("payChannel", "" + jbo.get("pay_channel"));
					payRecordMap.put("payTime", "" + jbo.get("pay_time"));
					payRecordMap.put("payTerminal",
							jbo.get("pay_terminal") == null ? "" : "" + jbo.get("pay_terminal"));
					payRecordMap.put("userAccount", params.get("useraccount"));
					String payRecordUrl = cloudCoreURLConfig.getAddOrderPayRecord();
					ObjectResponse payRecordResponse = HttpRequestTools.requestCloudCore(payRecordMap, payRecordUrl);
					// 结果判断
					if (!"200".equals(payRecordResponse.getCode())) {
						return ResultTools.setResponse(LocalCodeConstants.ERROR,
								LocalCodeConstants.getName(LocalCodeConstants.ERROR));
					}
				}
				if (!"0".equals(jbo.get("discount_price"))) {
					Object discountInfoString = jbo.get("discountInfo");
					String bean2gson = DataChangeTools.bean2gson(discountInfoString);
					JSONArray discountArray = (JSONArray) JSONArray.parse(bean2gson);
					for (int j = 0; j < discountArray.size(); j++) {
						JSONObject discountjbo = (JSONObject) discountArray.get(j);
						Map<String, String> findDiscountMap = new HashMap<String, String>();
						findDiscountMap.put("discountNo", discountjbo.getString("discount_no"));
						findDiscountMap.put("parkId", params.get("parkid"));
						String findDiscountUrl = cloudCoreURLConfig.getFindDiscount();
						ObjectResponse findDiscountResponse = HttpRequestTools.requestCloudCore(findDiscountMap,
								findDiscountUrl);
						if ("404".equals(findDiscountResponse.getCode())) {
							// 添加优惠记录
							Map<String, String> discountMap = new HashMap<String, String>();
							discountMap.put("parkId", params.get("parkid"));
							discountMap.put("orderNum", params.get("orderid"));
							discountMap.put("tradeNo", "" + jbo.get("tradeno"));
							discountMap.put("type", "" + discountjbo.get("discount_type"));
							discountMap.put("amount", "" + discountjbo.get("discount_amount"));
							discountMap.put("fromType", "0");
							discountMap.put("status", "1");
							discountMap.put("discountTime", "" + discountjbo.get("discount_time"));
							String discountUrl = cloudCoreURLConfig.getAddDiscount();
							ObjectResponse discountResponse = HttpRequestTools.requestCloudCore(discountMap,
									discountUrl);
							// 结果判断
							if (!"200".equals(discountResponse.getCode())) {
								return ResultTools.setResponse(LocalCodeConstants.ERROR,
										LocalCodeConstants.getName(LocalCodeConstants.ERROR));
							}
						}
					}
				}
			}
		}
		// 修改无效子订单状态
		Map<String, String> updateTradeMap = new HashMap<String, String>();
		updateTradeMap.put("orderNum", params.get("orderid"));
		updateTradeMap.put("oldPayStatus", "1");
		updateTradeMap.put("newPayStatus", "3");
		String updateTradeUrl = cloudCoreURLConfig.getModStatusOrderPayRecord();
		ObjectResponse updateTradeResponse = HttpRequestTools.requestCloudCore(updateTradeMap, updateTradeUrl);
		// 结果判断
		if (!"200".equals(updateTradeResponse.getCode())) {
//			return ResultTools.setResponse(LocalCodeConstants.ERROR,
//					LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
		// 更新优惠记录
		Map<String, String> updateDiscountMap = new HashMap<String, String>();
		updateDiscountMap.put("orderNum", params.get("orderid"));
		updateDiscountMap.put("oldStatus", "0");
		updateDiscountMap.put("newStatus", "2");
		String updateDiscountUrl = cloudCoreURLConfig.getModByOrdernumDiscount();
		ObjectResponse updateDiscountResponse = HttpRequestTools.requestCloudCore(updateDiscountMap, updateDiscountUrl);
		// 结果判断
		if (!"200".equals(updateDiscountResponse.getCode())) {
//			return ResultTools.setResponse(LocalCodeConstants.ERROR,
//					LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
		//更新主订单状态
		String updateOrderInfoUrl = cloudCoreURLConfig.getModCostOrderInfo();
		updateOrderInfoMap.put("serviceStatus", "2");
		ObjectResponse orderInfoResponse = HttpRequestTools.requestCloudCore(updateOrderInfoMap, updateOrderInfoUrl);
		// 结果判断
		if (!"200".equals(orderInfoResponse.getCode())) {
			return ResultTools.setResponse(LocalCodeConstants.ERROR,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
		// // 新增第三方平台出场信息
		// Map<String, Object> thirdRecordMap = new HashMap<String, Object>();
		// thirdRecordMap.put("order_num", params.get("orderid"));
		// thirdRecordMap.put("record_type", "2");
		// thirdRecordMap.put("park_id", params.get("parkid"));
		// thirdRecordMap.put("plate_number", params.get("plate"));
		// thirdRecordMap.put("exit_name", params.get("exitname"));
		// thirdRecordMap.put("amount", params.get("total_amount"));
		// thirdRecordMap.put("discount_time", params.get("discount_amount"));
		// thirdRecordMap.put("pay_method", "2");
		// thirdRecordMap.put("is_send", "1");
		// thirdRecordMap.put("create_time", DateTools.phpnowDate());
		// thirdRecordMap.put("exit_time", params.get("exittime"));
		// exitDao.addThirdRecord(thirdRecordMap);
		// // 判断特殊车辆
		// if (params.get("type").equals("4")) {
		// Map<String, String> exceptionMap = new HashMap<String, String>();
		// exceptionMap.put("park_id", params.get("parkid"));
		// exceptionMap.put("order_num", params.get("orderid"));
		// exceptionMap.put("local_order_id", params.get("local_orderid"));
		// exceptionMap.put("plate_number", params.get("plate"));
		// exceptionMap.put("card_id", params.get("cardid"));
		// exceptionMap.put("record_type", "2");
		// // exceptionMap.put("device_type", deviceType);
		// exceptionMap.put("enter_name", checkoutEnter.get("enterName"));
		// exceptionMap.put("exit_name", params.get("exitname"));
		// exceptionMap.put("enter_time", checkoutEnter.get("enterTime"));
		// exceptionMap.put("exit_time", params.get("exittime"));
		// // exceptionMap.put("enter_image", (String) jsonObject.get("enterImage"));
		// exceptionMap.put("exit_image", params.get("exitimage"));
		// // exceptionMap.put("car_type", (String) jsonObject.get("carType"));
		// exceptionMap.put("total_price", params.get("total_amount"));
		// exceptionMap.put("payed_price", params.get("paid_amount"));
		// exceptionMap.put("discount_amout", params.get("discount_amount"));
		// exceptionMap.put("discount_price", params.get("discount_amount"));
		// exceptionMap.put("unpay_price", params.get("total_amount"));
		// exceptionMap.put("user_account", params.get("user_account"));
		// exceptionMap.put("audit_status", "1");
		// // exceptionMap.put("create_time", ""+DateTools.phpnowDate());
		// // exceptionMap.put("repair_time", ""+DateTools.nowDate());
		// exitDao.addExceptionOrder(exceptionMap);
		// logger.info("特殊车辆储存异常表" + exceptionMap);
		// }
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("orderid", params.get("orderid"));
		responseMap.put("local_orderid", params.get("local_orderid"));
		return ResultTools.setObjectResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.SUCCESS, responseMap);
	}

}
