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
import com.park.localapi.service.ChargingRoleService;
import com.park.localapi.service.mq.ProducerMessageSend;

import net.sf.json.JSONObject;
@Service(value="chargingRoleService")
public class ChargingRoleServiceImpl implements ChargingRoleService{
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private ProducerMessageSend  producerMessageSend;

	@SuppressWarnings("static-access")
	public String chargingRole(String params) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> paramsMap = DataChangeTools.json2Map(params);
			//查询临停规则记录
		    String id = paramsMap.get("businessKey");
		    Map<String, Object> chargingRoleMap = parkDao.findChargingRoleById(id);
		    logger.info("查询临停规则记录 请求参数:"+id+"返回结果"+chargingRoleMap);
		    if(chargingRoleMap.get("id")==null){
		    	logger.info("无临停规则记录 请求参数:"+paramsMap);
		    	return ResultTools.setResponse(LocalCodeConstants.ERROR_404, LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
		    }else {
		    	Map<String, Object> chargingRole = new HashMap<String, Object>();
		    	String parkCode = parkDao.getParkCodeById(paramsMap.get("parkId"));
		    	chargingRole.put("parkid", parkCode);
		    	chargingRole.put("ruleid", ""+chargingRoleMap.get("rule_type"));
		    	chargingRole.put("name", ""+chargingRoleMap.get("name"));
		    	JSONObject json = JSONObject.fromObject(chargingRoleMap.get("fees_rule"));
//		    	JSONObject jsonObject = new JSONObject();
//		    	Object json = jsonObject.to(chargingRoleMap.get("fees_rule"));
		    	chargingRole.put("rule", json);
		    	String businessKey = paramsMap.get("businessKey");
				String businessCode = paramsMap.get("businessCode");
				String parkId = paramsMap.get("parkId");
				return producerMessageSend.issueMQLocal(chargingRole, parkId, parkCode, businessKey, businessCode,"chargingRole");
			}
	}
}
