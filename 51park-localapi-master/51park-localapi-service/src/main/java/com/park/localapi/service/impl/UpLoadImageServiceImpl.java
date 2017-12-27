package com.park.localapi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.base.common.Base64Tools;
import com.park.base.common.DataChangeTools;
import com.park.base.common.DateTools;
import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.ImageService;
import com.park.localapi.service.PublicService;

@Service(value="upLoadImageServiceImpl")
public class UpLoadImageServiceImpl {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ImageService imageService;
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private PublicService publicService;

	public String upLoadImage(String parmas) {
		// TODO Auto-generated method stub
		Map<String, String> parmasMap = DataChangeTools.json2Map(parmas);
		if (parmasMap.get("parkid") == null || parmasMap.get("filetype") == null || parmasMap.get("year") == null
				|| parmasMap.get("month") == null || parmasMap.get("day") == null || parmasMap.get("filename") == null
				|| parmasMap.get("imagebase64") == null || parmasMap.get("time") == null) {
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}else{
			byte[] imgFile = Base64Tools.decode2Byte(parmasMap.get("imagebase64"));
			String parkIdByCode = parkDao.getParkIdByCode(parmasMap.get("parkid"));
			if(parmasMap.get("month").length()==1)
				parmasMap.put("month", "0"+parmasMap.get("month"));
			if(parmasMap.get("day").length()==1)
				parmasMap.put("day", "0"+parmasMap.get("day"));
			String imgFileName = parmasMap.get("parkid")+"/"+
			(parmasMap.get("filetype").equals("0")?"image":"bill")+
					"/"+parmasMap.get("year")+parmasMap.get("month")+
					"/"+parmasMap.get("day")+"/"+parmasMap.get("filename");
			imageService.uploadFileStream(imgFile, imgFileName);
			if(parmasMap.get("filetype").equals("1")){
				Map<String, String> checkReconciliationMap = new HashMap<String, String>();
		         checkReconciliationMap.put("park_id", parkIdByCode);
		         checkReconciliationMap.put("report_date", parmasMap.get("year")+"-"+parmasMap.get("month")+"-"+parmasMap.get("day"));
		         logger.info("***park_id="+parkIdByCode);
		         logger.info("***report_date="+checkReconciliationMap.get("report_date"));
		         int checkReconciliationCount = publicService.checkBillPathExist(checkReconciliationMap);
		         logger.info("***checkReconciliationCount="+checkReconciliationCount);
		         if(checkReconciliationCount == 0){//新增URL
		        	Map<String, String> billUrlMap = new HashMap<String, String>();
					billUrlMap.put("park_id", parkIdByCode);
					billUrlMap.put("time", parmasMap.get("year")+"-"+parmasMap.get("month")+"-"+parmasMap.get("day"));
					billUrlMap.put("bill_path", imgFileName);
					billUrlMap.put("create_time", ""+DateTools.phpnowDate());
					parkDao.addBillUrl(billUrlMap);
		         }else {//修改URL
		        	 Map<String, String> billUrlMap = new HashMap<String, String>();
					 billUrlMap.put("park_id", parkIdByCode);
					 billUrlMap.put("time", parmasMap.get("year")+"-"+parmasMap.get("month")+"-"+parmasMap.get("day"));
					 billUrlMap.put("bill_path", imgFileName);
		        	 parkDao.updateBillUrl(billUrlMap);
				}
			}
			return ResultTools.setResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
		}
		
	}

}
