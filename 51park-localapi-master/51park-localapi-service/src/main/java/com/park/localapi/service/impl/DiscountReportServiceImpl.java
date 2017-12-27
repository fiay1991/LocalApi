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

@Service(value = "discountReportService")
public class DiscountReportServiceImpl extends AbstractService {

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
		this.mustParams = new String[] { "parkid", "orderid", "plate", "discount_no", "discount_type",
				"discount_amount", "discount_time" };
	}

	@Override
	public String executeService(Map<String, String> paramsMap) {
		if (parkDao.getParkIdByCode(paramsMap.get("parkid")) == null) {
			logger.info("请求参数 车场ID不合法 parkcode=" + paramsMap.get("parkid"));
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}
		paramsMap = publicService.initParamsMap(paramsMap);
		// 判断discountNo唯一
		Map<String, String> findDiscountMap = new HashMap<String, String>();
		findDiscountMap.put("parkId", paramsMap.get("parkid"));
		findDiscountMap.put("discountNo", paramsMap.get("discount_no"));
		String findDiscountUrl = cloudCoreURLConfig.getFindDiscount();
		ObjectResponse findDiscountResponse = HttpRequestTools.requestCloudCore(findDiscountMap, findDiscountUrl);
		if (!"404".equals(findDiscountResponse.getCode())) {
			logger.info("重复上报优惠;请求参数:" + findDiscountMap);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_405,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_405));
		}
		// 新增优惠
		Map<String, String> addDiscountMap = new HashMap<String, String>();
		addDiscountMap.put("parkId", paramsMap.get("parkid"));
		addDiscountMap.put("orderNum", paramsMap.get("orderid"));
		// addDiscountMap.put("tradeNo", paramsMap.get(""));
		addDiscountMap.put("discountNo", paramsMap.get("discount_no"));
		addDiscountMap.put("type", paramsMap.get("discount_type"));
		addDiscountMap.put("amount", paramsMap.get("discount_amount"));
		addDiscountMap.put("fromType", "0");// 优惠来源 0:本地 1:商户端 2:第三方
		// addDiscountMap.put("userId", paramsMap.get(""));
		// addDiscountMap.put("status", "0");// 默认为0
		// addDiscountMap.put("getAmount", paramsMap.get(""));
		addDiscountMap.put("discountTime", paramsMap.get("discount_time"));
		String addDiscountUrl = cloudCoreURLConfig.getAddDiscount();
		ObjectResponse addDiscountResponse = HttpRequestTools.requestCloudCore(addDiscountMap, addDiscountUrl);
		if (!"200".equals(addDiscountResponse.getCode())) {
			logger.info("新增优惠失败;请求参数:" + addDiscountMap);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_404,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		}
		return ResultTools.setResponse(LocalCodeConstants.SUCCESS,
				LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
	}
}
