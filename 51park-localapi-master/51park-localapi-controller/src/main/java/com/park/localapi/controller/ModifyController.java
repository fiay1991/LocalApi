package com.park.localapi.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.service.impl.ModifyServiceImpl;

@Controller
public class ModifyController {
	
	@Resource(name="modifyService")
	private ModifyServiceImpl modifyService;
	
	/**
	 * 车辆矫正
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/modify",produces="text/html;charset=UTF-8")
	public String modify(HttpServletRequest request, HttpServletResponse response){
		String params = (String)request.getAttribute("params");
		try {
			String result = modifyService.service(params);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultTools.setResponse(LocalCodeConstants.ERROR, LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
	}

}
