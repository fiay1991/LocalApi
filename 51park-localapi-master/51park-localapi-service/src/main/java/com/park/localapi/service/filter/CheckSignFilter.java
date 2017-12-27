package com.park.localapi.service.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.park.base.common.RSATools;
import com.park.base.common.ResultTools;
//import com.park.base.common.SignTools;
import com.park.base.common.ToolsUtil;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.common.constants.PrivateKeyConstants;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.Spring;

/**
 * 签名校验
 * 
 * @author wupanjun
 *
 */
public class CheckSignFilter implements Filter {
	/**
	 * 需要排除的页面
	 */

	private String excludedPages;
	private String[] excludedPageArray;

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		Logger logger = LoggerFactory.getLogger(this.getClass());

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		response.setContentType("text/html; charset=UTF-8");
		boolean isExcludedPage = false;
		for (String page : excludedPageArray) {// 判断是否在过滤url之外
			if (request.getRequestURI().contains(page)) {
				isExcludedPage = true;
				break;
			}
		}
		if (isExcludedPage) {// 在过滤url之外
			chain.doFilter(request, response);
			return;
		}
		String url = request.getRequestURI();
		/** 欢迎页 */
		if (url.endsWith("/LocalApi/") || url.endsWith("/LocalApi/testThread")) {
			chain.doFilter(request, response);
			return;
		}

		boolean verify = false;
		/**
		 * 获取请求来源
		 */
		String parkCode = request.getHeader("ParkId");
		String pid = request.getHeader("PID");
		String currentFrom = "";// 当前请求来源

		String params = request.getParameter("params");
		logger.info("***ParkCode={}, PID={}, 请求的URL={}", parkCode, pid, url);

		if (StringUtils.isBlank(params)) {
			logger.info("***未获取到params参数");
			response.getWriter().print(ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400)));
			return;
		}
		String privateKey = null;

		if (ToolsUtil.isNotNull(pid)) {// 获取此系统私钥
			currentFrom = pid;
			privateKey = PrivateKeyConstants.THIS.getPrivateKey();
		} else if (ToolsUtil.isNotNull(parkCode)) {// 获取车场对应私钥
			currentFrom = parkCode;
			try {
				ParkDao parkDao = Spring.getBean("parkDao");
				String id = parkDao.getParkIdByCode(parkCode);
				Map<String, String> parkKeys = parkDao.getParkKey("" + id);
				if (null == parkKeys || parkKeys.isEmpty()) {
					logger.info("验证失败，parkcode={},没有对应车场的KEY！", parkCode);
				} else {
					privateKey = parkKeys.get("private_key");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("查询车场对应私钥错误，parkCode: {}, error:{}", parkCode, e.toString());
			}
		}
		// 解密，获取参数
		if (privateKey != null) {
//			try {
//				String newParams = RSATools.decrypt(params, privateKey);
//				request.setAttribute("params", newParams);
				request.setAttribute("params", params);
				verify = true;
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error("解密环节出现错误：URL：" + url + "错误信息" + e.toString());
//			}
		}

		if (verify) {
			chain.doFilter(request, response);
			return;
		} else {
			logger.info("来自" + currentFrom + "的访问,认证失败，请求url:" + url + "***参数：" + params);
			response.getWriter().print(ResultTools.setResponse(LocalCodeConstants.ERROR_401, LocalCodeConstants.getName(LocalCodeConstants.ERROR_401)));
			return;
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		excludedPages = fConfig.getInitParameter("excludedUrl");
		if (StringUtils.isNotEmpty(excludedPages)) {
			excludedPageArray = excludedPages.split(",");
		}
		return;
	}
}
