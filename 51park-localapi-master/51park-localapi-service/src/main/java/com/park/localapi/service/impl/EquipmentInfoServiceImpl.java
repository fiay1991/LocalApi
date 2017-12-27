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
@Service(value="equipmentInfoService")
public class EquipmentInfoServiceImpl{
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private PublicService publicService;
	@Autowired
	private ParkDao parkDao;

	public String equipmentInfo(String parmas) {
		// TODO Auto-generated method stub
		Map<String, String> parmasMap = DataChangeTools.json2Map(parmas);
		logger.info("equipmentInfo 请求参数:"+parmasMap);
		if (parmasMap.get("parkid") == null ||  parmasMap.get("deviceno") == null || 
			 parmasMap.get("deviceip") == null ||  parmasMap.get("devicetype") == null || 
			 parmasMap.get("optype") == null || parmasMap.get("optime") == null ) {
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}else{
			parmasMap = publicService.initParamsMap(parmasMap);
			if(parmasMap.get("optype").equals("0")){
				//验重复添加
				Map<String, String> checkEquipmentInfoMap = new HashMap<String, String>();
				checkEquipmentInfoMap.put("park_id", parmasMap.get("parkid"));
				checkEquipmentInfoMap.put("code", parmasMap.get("deviceno"));
				int checkEquipmentInfo = parkDao.checkEquipmentInfo(checkEquipmentInfoMap);
				if(checkEquipmentInfo==0){
					//新增设备信息
					Map<String, String> addEquipmentInfoMap = new HashMap<String, String>();
					addEquipmentInfoMap.put("park_id", parmasMap.get("parkid"));
					addEquipmentInfoMap.put("code", parmasMap.get("deviceno"));
					addEquipmentInfoMap.put("gate_name", parmasMap.get("gatename"));
					addEquipmentInfoMap.put("ip", parmasMap.get("deviceip"));
					addEquipmentInfoMap.put("port", parmasMap.get("deviceport"));
					addEquipmentInfoMap.put("type", parmasMap.get("devicetype"));
					addEquipmentInfoMap.put("optime", parmasMap.get("optime"));
					addEquipmentInfoMap.put("status", "1");//'设备状态 0:离线 1:在线 2:故障'(新增默认为在线)
					logger.info("新增设备信息的参数:"+addEquipmentInfoMap);
					parkDao.addEquipmentInfo(addEquipmentInfoMap);
					return ResultTools.setResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
				}else {
					logger.info("添加设备请求重复 请求参数:"+parmasMap);
					return ResultTools.setResponse(LocalCodeConstants.ERROR_405, LocalCodeConstants.getName(LocalCodeConstants.ERROR_405));
				}
			}else if(parmasMap.get("optype").equals("1")){
				//验设备存在
				Map<String, String> checkEquipmentInfoMap = new HashMap<String, String>();
				checkEquipmentInfoMap.put("park_id", parmasMap.get("parkid"));
				checkEquipmentInfoMap.put("code", parmasMap.get("deviceno"));
				int checkEquipmentInfo = parkDao.checkEquipmentInfo(checkEquipmentInfoMap);
				if(checkEquipmentInfo!=0){
					//逻辑删除
					Map<String, String> delEquipmentInfoMap = new HashMap<String, String>();
					delEquipmentInfoMap.put("park_id", parmasMap.get("parkid"));
					delEquipmentInfoMap.put("code", parmasMap.get("deviceno"));
					delEquipmentInfoMap.put("delflag", "1");//删除标识（0：正常，1：删除）
					logger.info("删除设备 请求参数:"+delEquipmentInfoMap);
					parkDao.delEquipmentInfo(delEquipmentInfoMap);
					return ResultTools.setResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
				}else {
					logger.info("请求删除设备不存在 请求参数:"+parmasMap);
					return ResultTools.setResponse(LocalCodeConstants.ERROR_404, LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
				}
			}else if(parmasMap.get("optype").equals("2")){
				//验设备存在
				Map<String, String> checkEquipmentInfoMap = new HashMap<String, String>();
				checkEquipmentInfoMap.put("park_id", parmasMap.get("parkid"));
				checkEquipmentInfoMap.put("code", parmasMap.get("deviceno"));
				int checkEquipmentInfo = parkDao.checkEquipmentInfo(checkEquipmentInfoMap);
				if(checkEquipmentInfo !=0){
					//修改设备信息
					Map<String, String> updateEquipmentInfoMap = new HashMap<String, String>();
					updateEquipmentInfoMap.put("park_id", parmasMap.get("parkid"));
					updateEquipmentInfoMap.put("code", parmasMap.get("deviceno"));
					updateEquipmentInfoMap.put("gate_name", parmasMap.get("gatename"));
					updateEquipmentInfoMap.put("ip", parmasMap.get("deviceip"));
					updateEquipmentInfoMap.put("port", parmasMap.get("deviceport"));
					updateEquipmentInfoMap.put("type", parmasMap.get("devicetype"));
					updateEquipmentInfoMap.put("optime", parmasMap.get("optime"));
					updateEquipmentInfoMap.put("status", "1");//'设备状态 0:离线 1:在线 2:故障'(新增默认为在线)
					logger.info("修改设备信息的参数:"+updateEquipmentInfoMap);
					parkDao.updateEquipmentInfo(updateEquipmentInfoMap);
					return ResultTools.setResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
				}else {
					logger.info("请求修改设备不存在 请求参数:"+parmasMap);
					return ResultTools.setResponse(LocalCodeConstants.ERROR_404, LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
				}
			}else {
				logger.info("请求参数 optype 无效:"+parmasMap.get("optype"));
				return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
			}
		}
	}
	
	
}
