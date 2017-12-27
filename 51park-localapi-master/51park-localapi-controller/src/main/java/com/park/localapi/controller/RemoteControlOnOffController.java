package com.park.localapi.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.base.common.RSATools;
import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.service.config.KeysConfig;
import com.park.localapi.service.impl.RemoteControlOnOffServiceImpl;

@Controller
public class RemoteControlOnOffController {
	
	@Resource(name="remoteControlOnOffService")
	private RemoteControlOnOffServiceImpl remoteControlOnOffService;
	@Autowired
	private KeysConfig keysConfig;
	
	/**
	 * 远程开关闸
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/remotecontrolonoff",produces="text/html;charset=UTF-8")
	public String remoteControlOnOff(HttpServletRequest request, HttpServletResponse response){
		String params = request.getParameter("params");
		String parmasMap;
		try {
			parmasMap = RSATools.decrypt(params, keysConfig.getPrivate_key());
			String result = remoteControlOnOffService.remoteControlOnOff(parmasMap);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultTools.setResponse(LocalCodeConstants.ERROR, LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
	}

}
