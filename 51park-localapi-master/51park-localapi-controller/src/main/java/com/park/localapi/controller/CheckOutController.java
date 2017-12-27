package com.park.localapi.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.service.impl.CheckOutServiceImpl;

@Controller
public class CheckOutController {

	@Resource(name = "checkOutService")
	private CheckOutServiceImpl checkOutService;

	/**
	 * 结账统计
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkout", produces = "text/html;charset=UTF-8")
	public String checkOut(HttpServletRequest request, HttpServletResponse response) {
		String params = (String) request.getAttribute("params");
		try {
			String result = checkOutService.service(params);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultTools.setResponse(LocalCodeConstants.ERROR,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
	}

}
