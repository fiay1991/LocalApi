package com.park.localapi.common.tools;

import java.util.Map;

import com.park.base.common.HttpTools;
import com.park.base.common.ResultTools;
import com.park.base.common.constants.PublicKeyConstants;
import com.park.base.common.domain.ObjectResponse;

public class HttpRequestTools {

	/**
	 * 请求订单操作子系统CloudCore, 针对data是单个对象的情况
	 * @param requestMap
	 * @param url
	 * @return
	 */
	public static ObjectResponse requestCloudCore(Map<String, String> requestMap, String url) {
		String responseBody = HttpTools.pidPost(requestMap, url, PublicKeyConstants.CloudCore, PublicKeyConstants.CloudCore.toString());
		return ResultTools.getObjectResponse(responseBody, Map.class);
	}
	/**
	 * 请求订单操作子系统CloudCore, 针对data是对象集合的情况
	 * @param requestMap
	 * @param url
	 * @return
	 */
	public static ObjectResponse requestCloudCoreForList(Map<String, String> requestMap, String url) {
		String discountResponse = HttpTools.pidPost(requestMap, url, PublicKeyConstants.CloudCore, PublicKeyConstants.CloudCore.toString());
		return ResultTools.getObjectListResponse(discountResponse, Map.class);
	}
}
