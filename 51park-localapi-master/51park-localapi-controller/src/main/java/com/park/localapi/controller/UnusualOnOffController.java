package com.park.localapi.controller;

import java.util.Map;

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
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.impl.UnusualOnOffServiceImpl;

@Controller
public class UnusualOnOffController {
	
	@Resource(name="unusualOnOffService")
	private UnusualOnOffServiceImpl unusualOnOffService;
	@Autowired
	private ParkDao parkDao;
	/**
	 * 上报异常开关闸记录
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/unusualonoff",produces="text/html;charset=UTF-8")
	public String unusualOnOff(HttpServletRequest request, HttpServletResponse response){
		String params = request.getParameter("params");
		String parmasMap;
		try {
			String parkid = parkDao.getParkIdByCode(request.getHeader("ParkId"));
			Map<String, String> parkKeys = parkDao.getParkKey(parkid);
			parmasMap = RSATools.decrypt(params, parkKeys.get("private_key"));
			String result = unusualOnOffService.unusualOnOff(parmasMap);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultTools.setResponse(LocalCodeConstants.ERROR, LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
	}

}
