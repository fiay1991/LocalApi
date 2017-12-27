package com.park.localapi.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.base.common.DataChangeTools;
import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.PublicService;
@Service(value="unusualOnOffService")
public class UnusualOnOffServiceImpl{
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private PublicService publicService;
	
	public String unusualOnOff(String parmas) {
		// TODO Auto-generated method stub
		Map<String, String> parmasMap = DataChangeTools.json2Map(parmas);
		logger.info("unusualOnOff 请求参数:"+parmasMap);
		if (parmasMap.get("parkid") == null ||  parmasMap.get("plate") == null || 
			 parmasMap.get("devicetype") == null ||  parmasMap.get("gatetype") == null || 
			 parmasMap.get("source") == null || parmasMap.get("type") == null || 
			 parmasMap.get("useraccount") == null ||  parmasMap.get("optime") == null) {
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}else{
			parmasMap = publicService.initParamsMap(parmasMap);
			parkDao.addUnusualOnOff(parmasMap);
			return ResultTools.setResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
		}
	}
	
}
