package com.park.localapi.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.park.base.common.DataChangeTools;
import com.park.base.common.DateTools;
import com.park.base.common.HttpTools;
import com.park.base.common.RSATools;
import com.park.base.common.ResultTools;
import com.park.base.common.domain.ObjectResponse;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.dao.PublicDao;
import com.park.localapi.service.config.KeysConfig;
import com.park.localapi.service.redis.RedisUtils;
@Component
public class PublicService {

	private Logger logger =LoggerFactory.getLogger(this.getClass());
	@Resource(name="publicDao")
	private  PublicDao publicDao;
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private KeysConfig keysConfig;
	@Resource(name = "redisUtils")
	private RedisUtils redisUtils;
	
	 /**
     * 生成本地规则的header对象
     */
    public Map<String, String> getHeaders(String params,String parkcode, String parkid){  
		Map<String, String> headers = new HashMap<String, String>(); 
		Map<String, String> json2Map = DataChangeTools.json2Map(params);
    	String createLinkString = DataChangeTools.createLinkString(json2Map);
    	Map<String, String> parkKeys = parkDao.getParkKey(parkid);
		headers.put("Authorization", RSATools.sign(createLinkString, parkKeys.get("private_key")));
		headers.put("ParkId", parkcode);
		return headers;  
	}
    /**
     * 生成子系统规则的header对象
     * @param params
     * @return
     */
    public Map<String, String> getHeaders(String params){  
    	Map<String, String> headers = new HashMap<String, String>(); 
    	Map<String, String> json2Map = DataChangeTools.json2Map(params);
    	String createLinkString = DataChangeTools.createLinkString(json2Map);
    	headers.put("Authorization", RSATools.sign(createLinkString, keysConfig.getPrivate_key()));
    	return headers;  
    }
	
	/**
	 * 生成云订单号 order_num
	 * @return
	 */
	public static String generateOrderNum(){
		String now = DateTools.getFormat(DateTools.DF_TWOBIT,new Date());
		int rand =(int)((Math.random()*9+1)*10000);
		return now + "" + rand;
	}
	
	/**
	 * 根据车牌查询uid
	 * @param license_plate
	 * @return
	 */
/*	public static int findUidByPlateNum(String license_plate) {
		// TODO Auto-generated method stub
		return publicDao.findUidByPlateNum(license_plate);
	}*/

	/**
	 * 根据uid查询card_id
	 * @param uid
	 * @return
	 */
	/*public static String findCardidByUid(int uid) {
		// TODO Auto-generated method stub
		return publicDao.findCardidByUid(uid);
	}*/
	
	public Map<String, String> initParamsMap(Map<String, String> paramsMap){
		if(paramsMap.get("parkid")!=null){
			String parkid = parkDao.getParkIdByCode(paramsMap.get("parkid"));
			if(parkid==null){
				return paramsMap;
			}else {
				paramsMap.put("parkid", parkid);
				return paramsMap;
			}
		}else{
			return paramsMap;
		}
	}
	public Map<String, String> restoreParamsMap(Map<String, String> paramsMap){
		if(paramsMap.get("parkId")!=null){
			String parkId = parkDao.getParkCodeById(paramsMap.get("parkId"));
			if(parkId==null){
				return paramsMap;
			}else {
				paramsMap.put("parkId", parkId);
				return paramsMap;
			}
		}else{
			return paramsMap;
		}
	}
	
	/**
	 * 查询本地URL
	 * @param park_id
	 * @param interface_name
	 * @return
	 */
	public  String getLocalUrl(String park_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", park_id);
		return publicDao.getLocalUrl(map);
	}
	public int checkReconciliation(Map<String, String> checkReconciliationMap) {
		// TODO Auto-generated method stub
		return publicDao.checkReconciliation(checkReconciliationMap);
	}
	public int checkPayRecordReconciliation(String trade_no) {
		// TODO Auto-generated method stub
		return publicDao.checkPayRecordReconciliation(trade_no);
	}
	/**
	 * 请求本地，下发数据
	 * @param requestObj
	 * @param parkCode
	 * @param parkId
	 * @param businessKey
	 * @return
	 */
	public String sendLocalRequest(String request_uri, Object requestObj, String parkCode, String parkId, String businessKey, String businessCode){
		try {
			String request_json = DataChangeTools.bean2gson(requestObj);
			String request_url = getLocalUrl(parkId)+request_uri;
			Map<String, String> request_headers = getHeaders(request_json, parkCode, parkId);
			Map<String, String> parkKeys = parkDao.getParkKey(parkId);
			String discount_Encrpyt = RSATools.encrpyt(request_json, parkKeys.get("public_key"));
			String responseResult = HttpTools.HttpClientPost(request_url, discount_Encrpyt, request_headers);
			if(responseResult==null)
				return ResultTools.setResponse(LocalCodeConstants.ERROR_406, LocalCodeConstants.getName(LocalCodeConstants.ERROR_406));
			ObjectResponse response = DataChangeTools.gson2bean(responseResult, ObjectResponse.class);
			if(response.getCode().equals("200")){
				return ResultTools.setResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
			}else {
				logger.info("***businessCode为{}, businessKey为 {} 的对象下发失败: 返回Msg为{} " , businessCode, businessKey, response.getMsg());
				return ResultTools.setResponse(LocalCodeConstants.ERROR_406, LocalCodeConstants.getName(LocalCodeConstants.ERROR_406));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("***businessCode为{}, businessKey为 {} 的对象下发失败: {} ", businessCode, businessKey, e);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_406, LocalCodeConstants.getName(LocalCodeConstants.ERROR_406));
		}
	}
	/**
	 * 从redis中读取msgId的结果
	 * @param msgId
	 * @param timeOut
	 * @return
	 */
	public Map<String, String> getValueFromRedis(String msgId, int timeOut){
		Map<String, String> responseMap = new HashMap<String, String>();
		long currentTime = System.currentTimeMillis();
		long endTime = currentTime + timeOut * 1000;

		/**
		 * 从redis中读取msgId的值, 超过2秒未读取到数据, 则直接返回
		 */
		int n = 0;
		while (currentTime < endTime) {
			n++;
			if(redisUtils.exists(msgId)){
				logger.info("***开始从redis中读取msgid:{}...", msgId);
				Object obj = redisUtils.get(msgId);
				if (obj != null) {
					responseMap = DataChangeTools.json2Map((String) obj);
					logger.info("***第{}次从redis中读取msgid:{}成功.", n, msgId);
				}
				break;
			}
			currentTime = System.currentTimeMillis();
		}
		return responseMap;
	}
	public String findOrderNum(String local_orderid) {
		// TODO Auto-generated method stub
		return publicDao.findOrderNum(local_orderid);
	}
	public int checkBillPathExist(Map<String, String> checkReconciliationMap) {
		// TODO Auto-generated method stub
		return publicDao.checkBillPathExist(checkReconciliationMap);
	}
	
	/**
	 * 请求订单操作子系统CloudCore
	 * @param requestMap
	 * @param url
	 * @return
	 */
	public ObjectResponse requestCloudCore(Map<String, String> requestMap, String url) {
		try {
			String requestJson = DataChangeTools.bean2gson(requestMap);
			Map<String, String> headers = getHeaders(requestJson);
			String responseJson = HttpTools.HttpClientPost(url,
					RSATools.encrpyt(requestJson, keysConfig.getPublic_key()), headers);
			logger.info("请求CloudCore;请求参数为:" + requestJson + ";请求地址为:" + url + ";返回结果为:" + responseJson);
			ObjectResponse cloudCoreResponse = DataChangeTools.gson2bean(responseJson, ObjectResponse.class);
			return cloudCoreResponse;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("请求CloudCore异常;请求参数为:" + requestMap + ";请求地址为:" + url);
			ObjectResponse cloudCoreResponse = new ObjectResponse();
			cloudCoreResponse.setCode(LocalCodeConstants.ERROR_408);
			cloudCoreResponse.setMsg("请求CloudCore时异常;请求参数为:" + requestMap + ";请求地址为:" + url);
			return cloudCoreResponse;
		}
	}
	
}
