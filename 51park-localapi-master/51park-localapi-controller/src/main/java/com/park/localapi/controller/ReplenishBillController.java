package com.park.localapi.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.service.impl.ReplenishBillServiceImpl;

@Controller
public class ReplenishBillController {

	@Resource(name = "replenishBillService")
	private ReplenishBillServiceImpl replenishBillService;

	/**
	 * 补报账单
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/completebill", produces = "text/html;charset=UTF-8")
	public String replenishBill(HttpServletRequest request, HttpServletResponse response) {
		String params = (String) request.getAttribute("params");
		try {
			String result = replenishBillService.service(params);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultTools.setResponse(LocalCodeConstants.ERROR,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
	}

}
