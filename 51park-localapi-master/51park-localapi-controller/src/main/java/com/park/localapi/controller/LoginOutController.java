package com.park.localapi.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.base.common.RSATools;
import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.LoginOutService;

@Controller
public class LoginOutController {
	
	@Resource(name="loginOutService")
	private LoginOutService loginOutService;
	@Resource(name="parkDao")
	private ParkDao parkDao;
	/**
	 * 收费员登入登出
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/loginout",produces="text/html;charset=UTF-8")
	public String loginout(HttpServletRequest request, HttpServletResponse response){
		String params = request.getParameter("params");
		try {
			String parkid = parkDao.getParkIdByCode(request.getHeader("ParkId"));
			Map<String, String> parkKeys = parkDao.getParkKey(parkid);
			String newParams = RSATools.decrypt(params, parkKeys.get("private_key"));
			String result = loginOutService.loginOutRecord(newParams);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultTools.setResponse(LocalCodeConstants.ERROR, LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
		
	}

}
