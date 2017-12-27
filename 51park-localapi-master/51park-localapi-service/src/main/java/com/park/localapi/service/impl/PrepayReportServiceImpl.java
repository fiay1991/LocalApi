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
import com.park.localapi.service.PublicService;
import com.park.localapi.service.config.CloudCoreURLConfig;

@Service(value = "prepayReportService")
public class PrepayReportServiceImpl extends AbstractService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PublicService publicService;
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private CloudCoreURLConfig cloudCoreURLConfig;

	@Override
	public void setMustParams() {
		// 必传参数
		this.mustParams = new String[] { "parkid", "orderid", "plate", "tradeno", "total_price", "paid_price",
				"discount_price", "pay_way", "pay_channel", "pay_time" };
	}

	@Override
	public String executeService(Map<String, String> paramsMap) {
		if (parkDao.getParkIdByCode(paramsMap.get("parkid")) == null) {
			logger.info("请求参数 车场ID不合法 parkcode=" + paramsMap.get("parkid"));
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}
		if (Float.parseFloat(paramsMap.get("discount_price")) != 0) {
			if (paramsMap.get("discount_nos") == null) {
				logger.info("请求参数 discount_nos为空 参数为" + paramsMap);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
						LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
			}
		}
		paramsMap = publicService.initParamsMap(paramsMap);
		// 新增子订单
		Map<String, String> addPrepayMap = new HashMap<String, String>();
		addPrepayMap.put("parkId", paramsMap.get("parkid"));
		addPrepayMap.put("orderNum", paramsMap.get("orderid"));
		addPrepayMap.put("tradeNo", paramsMap.get("tradeno"));
		addPrepayMap.put("costBefore", paramsMap.get("total_price"));
		addPrepayMap.put("costAfter", paramsMap.get("paid_price"));
		addPrepayMap.put("discountAmount", paramsMap.get("discount_price"));
		addPrepayMap.put("payStatus", "2");
		addPrepayMap.put("payWay", paramsMap.get("pay_way"));
		addPrepayMap.put("payChannel", paramsMap.get("pay_channel"));
		// addPrepayMap.put("describe", paramsMap.get(""));
		addPrepayMap.put("payTerminal", paramsMap.get("pay_terminal"));
		addPrepayMap.put("userAccount", paramsMap.get("useraccount"));
		addPrepayMap.put("payTime", paramsMap.get("pay_time"));
		String addPrepayUrl = cloudCoreURLConfig.getAddOrderPayRecord();
		ObjectResponse addPrepayResponse = HttpRequestTools.requestCloudCore(addPrepayMap, addPrepayUrl);
		if (!"200".equals(addPrepayResponse.getCode())) {
			logger.info("新增子订单失败;请求参数:" + addPrepayMap);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_404,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		}
		// 修改优惠
		if (paramsMap.get("discount_nos") != null) {
			String discount_nos = paramsMap.get("discount_nos");
			String[] discount_nosSplit = discount_nos.split(",");
			for (int i = 0; i < discount_nosSplit.length; i++) {
				String discount_no = discount_nosSplit[i];
				Map<String, String> updateDiscountMap = new HashMap<String, String>();
				updateDiscountMap.put("discountNo", discount_no);
				updateDiscountMap.put("newStatus", "1");
				updateDiscountMap.put("toTradeNo", paramsMap.get("tradeno"));
				String updateDiscountUrl = cloudCoreURLConfig.getModByNoDiscount();
				ObjectResponse updateDiscountResponse = HttpRequestTools.requestCloudCore(updateDiscountMap,
						updateDiscountUrl);
				if (!"200".equals(updateDiscountResponse.getCode())) {
					logger.info("修改失败;请求参数:" + updateDiscountMap);
					return ResultTools.setResponse(LocalCodeConstants.ERROR_404,
							LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
				}
			}
		}
		return ResultTools.setResponse(LocalCodeConstants.SUCCESS,
				LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
	}
}
