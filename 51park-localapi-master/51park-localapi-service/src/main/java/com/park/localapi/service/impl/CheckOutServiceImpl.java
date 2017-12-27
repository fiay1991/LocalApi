package com.park.localapi.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.base.common.ResultTools;
import com.park.base.common.ToolsUtil;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.AbstractService;
import com.park.localapi.service.PublicService;

@Service(value = "checkOutService")
public class CheckOutServiceImpl extends AbstractService {

	@Autowired
	private PublicService publicService;
	@Autowired
	private ParkDao parkDao;

	public String executeService(Map<String, String> parmasMap) {
		parmasMap = publicService.initParamsMap(parmasMap);
		if (ToolsUtil.isNull(parmasMap.get("discount_amount"))) {
			parmasMap.put("discount_amount", "0");
		}
		if (ToolsUtil.isNull(parmasMap.get("discount_number"))) {
			parmasMap.put("discount_number", "0");
		}
		logger.info("addCheckOut 请求参数:" + parmasMap);

		parkDao.addCheckOut(parmasMap);
		return ResultTools.setResponse(LocalCodeConstants.SUCCESS,
				LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
	}

	public void setMustParams() {
		// 必传参数
		this.mustParams = new String[] { "parkid", "type", "useraccount", "username", "position", "begintime",
				"endtime", "total_number", "total_amount", "time" };
	}
}
