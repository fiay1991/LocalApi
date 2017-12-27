package com.park.localapi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.base.common.DataChangeTools;
import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.mq.ProducerMessageSend;
@Service(value="authorizeService")
public class AuthorizeServiceImpl {
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private ProducerMessageSend producerMessageSend;

	public String authorize(String params) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> paramsMap = DataChangeTools.json2Map(params);
			//查询授权记录
		    String id = paramsMap.get("businessKey");
		    Map<String, Object> authorizeMap = parkDao.findAuthorizeById(id);
		    logger.info("查询授权记录 请求参数:"+id+"返回结果"+authorizeMap);
		    if(authorizeMap == null || authorizeMap.get("id")==null){
		    	logger.info("无授权记录 请求参数:"+paramsMap);
		    	return ResultTools.setResponse(LocalCodeConstants.ERROR_404, LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		    }else {
		    	Map<String, Object> authorize = new HashMap<String, Object>();
		    	String parkCode = parkDao.getParkCodeById(""+authorizeMap.get("park_id"));
		    	authorize.put("parkid", parkCode);
		    	authorize.put("plate", ""+authorizeMap.get("plate_number"));
		    	authorize.put("name", ""+authorizeMap.get("name"));
		    	authorize.put("begintime", authorizeMap.get("begin_time"));
		    	authorize.put("endtime", authorizeMap.get("end_time"));
		    	authorize.put("authtype", authorizeMap.get("auth_type"));
		    	authorize.put("time", authorizeMap.get("auth_time"));
		    	String businessKey = paramsMap.get("businessKey");
				String businessCode = paramsMap.get("businessCode");
				String parkId = ""+authorizeMap.get("park_id");
				return producerMessageSend.issueMQLocal(authorize, parkId, parkCode, businessKey, businessCode, "chargingRole");
				
			}
	}
}
