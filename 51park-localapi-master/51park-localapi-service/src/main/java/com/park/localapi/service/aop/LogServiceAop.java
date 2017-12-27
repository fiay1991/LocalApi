package com.park.localapi.service.aop;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.park.base.common.RSATools;
import com.park.base.common.ToolsUtil;
import com.park.localapi.common.constants.PrivateKeyConstants;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.Spring;

/**
 * 记录所有接口被请求时的参数和返回结果
 * @author fangct 20170705
 *
 */
@Aspect
@Component
public class LogServiceAop {
	
	private static final Logger logger=LoggerFactory.getLogger(LogServiceAop.class);
	
	/**
	 * 切面：controller层的任意方法和参数
	 */
	//@Pointcut("execution(* com.park.localapi.service.impl.*.test(..))")
	@Pointcut("execution(* com.park.localapi.controller.*.*(..))")
	public void log() {}
	
	@AfterReturning(value="log()",returning="returnValue")
	public void log(JoinPoint joinPoint,Object returnValue) throws Throwable {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		try {
			
			//从request请求中获取参数
			String params = request.getParameter("params");
			if (params == null) {
				return;
			}
			// 方法名
			String req_interface = joinPoint.getSignature().getName();
			// 请求URL
			String url = request.getRequestURL().toString();

			String parkCode = request.getHeader("ParkId");
			String pid = request.getHeader("PID");
			String currentFrom = "";// 当前请求来源
			String privateKey = null;
			
			if (ToolsUtil.isNotNull(pid)) {// 获取此系统私钥
				currentFrom = pid;
				privateKey = PrivateKeyConstants.THIS.getPrivateKey();
			} else if (ToolsUtil.isNotNull(parkCode)) {// 获取车场对应私钥
				currentFrom = parkCode;
				ParkDao parkDao = Spring.getBean("parkDao");
				String id = parkDao.getParkIdByCode(parkCode);
				Map<String, String> parkkeys = parkDao.getParkKey("" + id);
				privateKey = parkkeys.get("private_key");
			}
			String newParams = RSATools.decrypt(params, privateKey);
			if(newParams.contains("imagebase64")){
				int pos1 = newParams.indexOf("imagebase64");
				int length = "imagebase64".length();
				String leftStr = newParams.substring(0, pos1 + length + 1);
				String rightStr = newParams.substring(pos1 + length + 1, newParams.length());
				
				int pos2 = rightStr.indexOf(",");
				String rightStr2 = rightStr.substring(pos2, rightStr.length());
				newParams = leftStr + ":\"(文件base64编码的替代内容)\"" + rightStr2;
			}
			logger.info("来自"+currentFrom+"的访问: 请求的URL:{}, 方法名:{}, 参数:{}, 返回结果:{}", url, req_interface, newParams, returnValue.toString());
			
		}catch(Exception e){
			//记录本地异常日志    
            logger.error("=======接口调用记录Log异常========"); 
            logger.error("请求的URL:{}", request.getRequestURL()); 
            logger.error("异常方法:{}异常代码:{}异常信息:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage());
		}
	}
}
