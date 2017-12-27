package com.park.localapi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.base.common.DataChangeTools;
import com.park.base.common.ResultTools;
import com.park.base.common.domain.ObjectResponse;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.common.tools.HttpRequestTools;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.SendService;
import com.park.localapi.service.config.CloudCoreURLConfig;
import com.park.localapi.service.mq.ProducerMessageSend;

@Service(value = "discountService")
public class DiscountServiceImpl implements SendService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ParkDao parkDao;
	@Autowired
	private CloudCoreURLConfig cloudCoreURLConfig;
	@Autowired
	private ProducerMessageSend producerMessageSend;
	
	//优惠类型：免金额
	private final String DISCOUNT_TYPE = "5";

	@SuppressWarnings("unchecked")
	public String send(String params) throws Exception {
		Map<String, String> paramsMap = DataChangeTools.json2Map(params);

		/**
		 * 涉及到访问cloudcore的URL
		 */
		String discountUrl = cloudCoreURLConfig.getFindDiscount();// 查询优惠信息
		String orderFindUrl = cloudCoreURLConfig.getFindByOneOrderInfo();// 查询订单信息

		String parkId = paramsMap.get("parkId");
		// 查询优惠记录
		Map<String, String> findDiscountMap = new HashMap<String, String>();
		findDiscountMap.put("id", paramsMap.get("businessKey"));
		ObjectResponse discountRecordResponse = HttpRequestTools.requestCloudCore(findDiscountMap, discountUrl);
		if (discountRecordResponse == null || discountRecordResponse.getData() == null) {
			logger.info("***未找到优惠记录，id:{}", paramsMap.get("businessKey"));
			return ResultTools.setResponse(LocalCodeConstants.ERROR_404, LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		}
		Map<String, String> discountRecord = (Map<String, String>) discountRecordResponse.getData();
		String orderNum = discountRecord.get("orderNum");
		// 查询在场订单记录
		Map<String, String> orderInfoMap = new HashMap<String, String>();
		orderInfoMap.put("orderNum", orderNum);
		ObjectResponse orderInfoResponse = HttpRequestTools.requestCloudCore(orderInfoMap, orderFindUrl);
		if (orderInfoResponse == null || orderInfoResponse.getData() == null) {
			logger.info("***未找到订单记录，id:{}", orderNum);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_404, LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		}
		Map<String, Object> orderInfo = (Map<String, Object>) orderInfoResponse.getData();
		// 优惠通知
		Map<String, Object> discount = new HashMap<String, Object>();
		String parkCode = parkDao.getParkCodeById(parkId);
		discount.put("parkid", parkCode);
		discount.put("orderid", orderNum);
		discount.put("plate", orderInfo.get("plateNumber"));
		discount.put("discount_no", discountRecord.get("discountNo"));
		discount.put("discount_type", Integer.parseInt((String) discountRecord.get("type")));
		if (DISCOUNT_TYPE.equals((String) discountRecord.get("type"))) {
			//元转分
			discount.put("discount_amount", Float.parseFloat((String) discountRecord.get("amount")) * 100);
		} else {
			discount.put("discount_amount", Float.parseFloat((String) discountRecord.get("amount")));
		}
		discount.put("discount_time", Integer.parseInt((String) discountRecord.get("discountTime")));
		String businessKey = paramsMap.get("businessKey");
		String businessCode = paramsMap.get("businessCode");
		return producerMessageSend.issueMQLocal(discount, parkId, parkCode, businessKey, businessCode, "discount");
	}

}
