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
import com.park.localapi.service.PublicService;
@Service(value="equipmentStatusService")
public class EquipmentStatusServiceImpl{
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private PublicService publicService;
	@Autowired
	private ParkDao parkDao;

	public String equipmentStatus(String parmas) {
		// TODO Auto-generated method stub
		Map<String, String> parmasMap = DataChangeTools.json2Map(parmas);
		logger.info("equipmentInfo 请求参数:"+parmasMap);
		if (parmasMap.get("parkid") == null ||  parmasMap.get("deviceno") == null || 
			 parmasMap.get("devicestatus") == null || parmasMap.get("time") == null ) {
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}else{
			parmasMap = publicService.initParamsMap(parmasMap);
			Map<String, String> checkEquipmentInfoMap = new HashMap<String, String>();
			checkEquipmentInfoMap.put("park_id", parmasMap.get("parkid"));
			checkEquipmentInfoMap.put("code", parmasMap.get("deviceno"));
//			int checkEquipmentInfo = parkDao.checkEquipmentInfo(checkEquipmentInfoMap);
			Map<String, String> EquipmentInfoMap = parkDao.findEquipmentInfo(checkEquipmentInfoMap);
			if(EquipmentInfoMap!=null && EquipmentInfoMap.size()>0){
				Map<String, String> updateEquipmentStatusMap = new HashMap<String, String>();
				updateEquipmentStatusMap.put("park_id", parmasMap.get("parkid"));
				updateEquipmentStatusMap.put("code", parmasMap.get("deviceno"));
				updateEquipmentStatusMap.put("status", parmasMap.get("devicestatus"));
				updateEquipmentStatusMap.put("update_time", parmasMap.get("time"));
				updateEquipmentStatusMap.put("fault_reason", parmasMap.get("failurcause"));
				logger.info("修改设备状态请求参数"+updateEquipmentStatusMap);
				parkDao.updateEquipmentStatus(updateEquipmentStatusMap);
				if(!parmasMap.get("devicestatus").equals("1")){
					Map<String, String> addEquipmentAlertMap = new HashMap<String, String>();
					addEquipmentAlertMap.put("equip_id", EquipmentInfoMap.get("id"));
					if(parmasMap.get("devicestatus").equals("0")){
						addEquipmentAlertMap.put("alarm_event", "离线");
					}else {
						addEquipmentAlertMap.put("alarm_event", "故障");
					}
					addEquipmentAlertMap.put("alaram_desc", parmasMap.get("failurcause"));
					addEquipmentAlertMap.put("time", parmasMap.get("time"));
//					addEquipmentAlertMap.put("operator", "");//暂无操作人,如需展示请更改参数
					logger.info("新增cp_park_equipment_alter表 参数为:"+addEquipmentAlertMap);
					parkDao.addEquipmentAlert(addEquipmentAlertMap);
				}
				return ResultTools.setResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
			}else {
				logger.info("请求设备不存在 请求参数:"+parmasMap);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_404, LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
			}
			
		}
	}
}
