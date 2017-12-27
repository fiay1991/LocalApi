package com.park.localapi.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.base.common.DataChangeTools;
import com.park.base.common.ResultTools;
import com.park.base.common.ToolsUtil;
import com.park.base.common.domain.ObjectResponse;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.common.tools.HttpRequestTools;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.SendService;
import com.park.localapi.service.config.CloudCoreURLConfig;
import com.park.localapi.service.mq.ProducerMessageSend;

@Service(value = "prepayService")
public class PrepayServiceImpl implements SendService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ParkDao parkDao;
	@Autowired
	private CloudCoreURLConfig cloudCoreURLConfig;
	@Autowired
	private ProducerMessageSend producerMessageSend;

	//优惠状态：已使用
	private final String DISCOUNT_STATUS = "1";
	
	@SuppressWarnings("unchecked")
	public String send(String params) throws Exception {
		Map<String, String> paramsMap = DataChangeTools.json2Map(params);

		/**
		 * 涉及到访问cloudcore的URL
		 */
		String payRecordUrl = cloudCoreURLConfig.getFindOrderPayRecord();// 查询交易记录
		String orderEnterUrl = cloudCoreURLConfig.getFindByOneOrderInfo();// 查询订单信息
		String discountsUrl = cloudCoreURLConfig.getFindsDiscount();// 优惠券信息

		String parkId = paramsMap.get("parkId");
		// 查询子订单相关信息
		Map<String, String> payRecordMap = new HashMap<String, String>();
		payRecordMap.put("parkId", parkId);
		payRecordMap.put("id", paramsMap.get("businessKey"));
		ObjectResponse payRecordResponse = HttpRequestTools.requestCloudCore(payRecordMap, payRecordUrl);
		if (payRecordResponse == null || payRecordResponse.getData() == null) {
			logger.info("***未找到支付记录，id:{}", paramsMap.get("businessKey"));
			return ResultTools.setResponse(LocalCodeConstants.ERROR_404, LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		}
		
		Map<String, String> payRecord = (Map<String, String>) payRecordResponse.getData();
		String orderNum = payRecord.get("orderNum");
		// 查询在场订单记录
		Map<String, String> orderEnterMap = new HashMap<String, String>();
		orderEnterMap.put("orderNum", orderNum);
		ObjectResponse orderEnterResponse = HttpRequestTools.requestCloudCore(orderEnterMap, orderEnterUrl);
		if (orderEnterResponse == null || orderEnterResponse.getData() == null) {
			logger.info("***未找到订单记录，订单号:{}", orderNum);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_404, LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		}
		Map<String, String> orderEnter = (Map<String, String>) orderEnterResponse.getData();

		// 查询本次交易使用的优惠券
		Map<String, String> discountsMap = new HashMap<String, String>();
		discountsMap.put("parkId", parkId);
		discountsMap.put("tradeNo", payRecord.get("tradeNo"));
		discountsMap.put("status", DISCOUNT_STATUS);
		ObjectResponse discountsResponse = HttpRequestTools.requestCloudCoreForList(discountsMap, discountsUrl);
		String discount_nos = new String();
		if (discountsResponse == null || discountsResponse.getData() == null) {
			logger.info("***没有使用优惠券，交易流水号:{}", payRecord.get("tradeNo"));
		} else {
			List<Map<String, String>> discounts = (List<Map<String, String>>) discountsResponse.getData();
			for (int i = 0; i < discounts.size(); i++) {
				Map<String, String> discount = discounts.get(i);
				if (discount != null && ToolsUtil.isNotNull(discount.get("discountNo"))) {
					discount_nos = discount_nos + (discount.get("discountNo") + ",");
				}
			}
			if (discount_nos.length() > 0) {
				discount_nos = discount_nos.substring(0, discount_nos.length() - 1);
			}
		}

		/**
		 * 预缴费下发
		 */
		String parkCode = parkDao.getParkCodeById(parkId);
		// 生成下发本地的参数对象
		Map<String, Object> prepayMap = new HashMap<String, Object>();
		prepayMap.put("parkid", parkCode);
		prepayMap.put("orderid", orderNum);
		prepayMap.put("discount_nos", discount_nos);
		buildPrepayParams(prepayMap, orderEnter, payRecord);

		String businessKey = paramsMap.get("businessKey");
		String businessCode = paramsMap.get("businessCode");
		return producerMessageSend.issueMQLocal(prepayMap, parkId, parkCode, businessKey, businessCode, "prepay");
	}

	/**
	 * 生成下发本地对象
	 * 
	 * @param prepayMap
	 * @param orderEnter
	 * @param payRecord
	 */
	public void buildPrepayParams(Map<String, Object> prepayMap, Map<String, String> orderEnter,
			Map<String, String> payRecord) {
		prepayMap.put("plate", orderEnter.get("plateNumber"));
		prepayMap.put("tradeno", payRecord.get("tradeNo"));
		prepayMap.put("total_price", Float.parseFloat(payRecord.get("costBefore")));
		prepayMap.put("paid_price", Float.parseFloat(payRecord.get("costAfter")));
		prepayMap.put("discount_price", Float.parseFloat(payRecord.get("discountAmount")));
		prepayMap.put("pay_way", Integer.parseInt(payRecord.get("payWay")));
		prepayMap.put("pay_time", Integer.parseInt(payRecord.get("payTime")));
		prepayMap.put("pay_channel", Integer.parseInt(payRecord.get("payChannel")));
		prepayMap.put("pay_terminal", payRecord.get("payTerminal"));
		prepayMap.put("useraccount", payRecord.get("userAccount"));
	}
}
