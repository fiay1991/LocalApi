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
import com.park.localapi.dao.ExitDao;
import com.park.localapi.service.PublicService;
@Service(value="unusualOrderService")
public class UnusualOrderServiceImpl{
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ExitDao exitDao;
	@Autowired
	private PublicService publicService;

	public String unusualOrder(String parmas) {
		// TODO Auto-generated method stub
		Map<String, String> parmasMap = DataChangeTools.json2Map(parmas);
		logger.info("unusualOrder 请求参数:"+parmasMap);
		if (parmasMap.get("parkid") == null ||  parmasMap.get("plate") == null || 
			 parmasMap.get("devicetype") == null ||  parmasMap.get("exitname") == null || 
			 parmasMap.get("exittime") == null || parmasMap.get("cartype") == null || 
			 parmasMap.get("total_price") == null || parmasMap.get("payed_price") == null || 
			 parmasMap.get("discount_price") == null || parmasMap.get("unpay_price") == null || 
			 parmasMap.get("discount_amount") == null || 
			 parmasMap.get("useraccount") == null ||  parmasMap.get("source") == null) {
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		}else{
			//判断重复上报
			Map<String, String> ckMap = new HashMap<String, String>();
			logger.info("unusualOrder请求参数:"+parmasMap);
			//将parkCode转化为parkId
			parmasMap = publicService.initParamsMap(parmasMap);
			ckMap.put("park_id", parmasMap.get("parkid"));
			ckMap.put("plate_number", parmasMap.get("plate"));
			int ckUnusual = exitDao.checkOutUnusualOrder(ckMap);
			// 储存\修改数据
			Map<String, String> exceptionMap = new HashMap<String, String>();
			exceptionMap.put("park_id", parmasMap.get("parkid"));
			exceptionMap.put("order_num", parmasMap.get("orderid"));
			exceptionMap.put("local_order_id", parmasMap.get("local_orderid"));
			exceptionMap.put("plate_number", parmasMap.get("plate"));
			exceptionMap.put("card_id", parmasMap.get("cardid"));
			exceptionMap.put("record_type", parmasMap.get("source").equals("0")?"1":"3");
			exceptionMap.put("device_type", parmasMap.get("devicetype"));
			exceptionMap.put("enter_name", parmasMap.get("entername"));
			exceptionMap.put("exit_name", parmasMap.get("exitname"));
			exceptionMap.put("enter_time", parmasMap.get("entertime"));
			exceptionMap.put("exit_time", parmasMap.get("exittime"));
			exceptionMap.put("enter_image", parmasMap.get("enterimage"));
			exceptionMap.put("exit_image", parmasMap.get("exitimage"));
			exceptionMap.put("car_type", parmasMap.get("cartype"));
			exceptionMap.put("total_price", parmasMap.get("total_price"));
			exceptionMap.put("payed_price", parmasMap.get("payed_price"));
			exceptionMap.put("discount_amount", parmasMap.get("discount_amount"));
			exceptionMap.put("discount_price", parmasMap.get("discount_price"));
			exceptionMap.put("unpay_price", parmasMap.get("unpay_price"));
			exceptionMap.put("user_account", parmasMap.get("useraccount"));
			//exceptionMap.put("create_time", ""+DateTools.phpnowDate());
			//exceptionMap.put("repair_time", ""+DateTools.nowDate());
			if(ckUnusual == 0){
				logger.info("新增异常订单 请求参数:"+exceptionMap);
				exitDao.addUnusualOrder(exceptionMap);
			}else {
				logger.info("修改异常订单 请求参数:"+exceptionMap);
				exitDao.updateUnusualOrder(exceptionMap);
			}
			return ResultTools.setResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
		}
	}
	
	
}
