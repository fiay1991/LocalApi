package com.park.localapi.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.base.common.DateTools;
import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.common.constants.TimeOutConstants;
import com.park.localapi.dao.ParkStatusDao;
import com.park.localapi.domain.response.ParkStatusResponse;
import com.park.localapi.service.AbstractService;
import com.park.localapi.service.PublicService;

@Service(value = "parkStatusService")
public class ParkStatusServiceImpl extends AbstractService {

	@Autowired
	private ParkStatusDao parkStatusDao;
	@Autowired
	private PublicService publicService;

	@Override
	public String executeService(Map<String, String> parmasMap) {
		String parkcode = parmasMap.get("parkid");
		parmasMap = publicService.initParamsMap(parmasMap);
		int time = DateTools.phpnowDate();
		// 判断心跳记录是否存在
		Map<String, Object> checkoutExistingMap = parkStatusDao.checkoutExisting(parmasMap.get("parkid"));
		if (checkoutExistingMap == null) {
			// 新增心跳表 cp_park_status
			Map<String, Object> parkStatusMap = new HashMap<String, Object>();
			parkStatusMap.put("park_id", parmasMap.get("parkid"));
			parkStatusMap.put("platform_time", time);
			parkStatusMap.put("local_time", parmasMap.get("time"));
			parkStatusDao.addStatusInfo(parkStatusMap);
		} else {
			// 更新心跳表
			Map<String, Object> parkStatusMap = new HashMap<String, Object>();
			parkStatusMap.put("park_id", parmasMap.get("parkid"));
			parkStatusMap.put("platform_time", time);
			parkStatusMap.put("local_time", parmasMap.get("time"));
			parkStatusDao.updateStatusInfo(parkStatusMap);
			// 判断上次心跳同步时间与本次间隔
			Integer platform_time = (Integer) checkoutExistingMap.get("platform_time");
			if (platform_time + TimeOutConstants.OFF_LINE_TIME < time) {
				// 添加车场连接断开记录表
				Map<String, Object> offLineMap = new HashMap<String, Object>();
				offLineMap.put("park_id", parmasMap.get("parkid"));
				offLineMap.put("last_connection_time", platform_time);
				offLineMap.put("reconnect_time", time);
				offLineMap.put("off_time", time - platform_time);
				parkStatusDao.addOffLine(offLineMap);
			}
		}
		// 新增车场车位记录表 cp_park_carnum
		Map<String, Object> parkCarnumMap = new HashMap<String, Object>();
		parkCarnumMap.put("park_id", parmasMap.get("parkid"));
		parkCarnumMap.put("empty_car_num", parmasMap.get("emptynumber"));
		parkCarnumMap.put("update_time", DateTools.getFormat(new Date()));
		parkStatusDao.updateParkCarnum(parkCarnumMap);
		// 返回结果
		ParkStatusResponse parkStatusResponse = new ParkStatusResponse();
		parkStatusResponse.setParkid(parkcode);
		parkStatusResponse.setTime(time);
		return ResultTools.setObjectResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS), parkStatusResponse);
	}

	@Override
	public void setMustParams() {
		// 必传参数
		this.mustParams = new String[] { "parkid", "emptynumber", "time" };
	}

}
