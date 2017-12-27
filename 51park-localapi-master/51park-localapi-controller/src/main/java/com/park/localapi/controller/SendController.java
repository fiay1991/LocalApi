package com.park.localapi.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.base.common.DataChangeTools;
import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.service.SendService;
import com.park.localapi.service.factory.SendFactory;

@Controller
public class SendController {
	
	/**
	 * 下发通知
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/send",produces="text/html;charset=UTF-8")
	public String send(HttpServletRequest request, HttpServletResponse response){
		String params = (String)request.getAttribute("params");
		try {
			Map<String, String> paramsMap = DataChangeTools.json2Map(params);
			//验证入参有效性
			if (paramsMap.get("businessKey") == null ||
					paramsMap.get("parkId") == null ||
					paramsMap.get("businessCode") == null) {
				return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
			}else{
				SendService service = SendFactory.createSendImpl(paramsMap.get("businessCode"));
				if(service != null){
					return service.send(params);
				}else {
					return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultTools.setResponse(LocalCodeConstants.ERROR, LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
		
	}

}
