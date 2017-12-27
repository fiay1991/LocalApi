/**
 * 
 */
package com.park.localapi.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.park.base.common.DataChangeTools;
import com.park.base.common.ResultTools;
import com.park.base.common.ToolsUtil;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.domain.vo.ValidateResultVO;

/**
 * @author fangct created on 2017年12月1日
 */
public abstract class AbstractService {
	// 日志管理
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	// 必传参数
	protected String[] mustParams;

	/**
	 * 设置必传参数
	 * 
	 * @return
	 */
	public abstract void setMustParams();

	/**
	 * 获取必传参数
	 * 
	 * @return
	 */
	public String[] getMustParams() {
		return mustParams;
	}

	/**
	 * 业务处理标准流程
	 * 
	 * @param params
	 * @return
	 */
	public String service(String params) {
		setMustParams();
		// 验证参数必传
		ValidateResultVO result = vaildateParams(params, mustParams);
		if (!result.isSuccess()) {
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		} else {
			// 执行service逻辑
			Map<String, String> parmasMap = (Map<String, String>) result.getParams();
			return executeService(parmasMap);
		}
	}

	/**
	 * 执行具体的业务逻辑
	 * 
	 * @param parmasMap
	 * @return
	 */
	protected abstract String executeService(Map<String, String> paramsMap);

	/**
	 * 验证参数必传
	 * 
	 * @param params
	 *            JSON格式参数
	 * @param paramsArr
	 *            必传参数数组
	 * @return
	 */
	protected ValidateResultVO vaildateParams(String params, String[] mustParams) {
		Map<String, String> parmasMap = DataChangeTools.json2Map(params);

		StringBuffer logContent = new StringBuffer();
		boolean success = true;
		for (String param : mustParams) {
			String p = parmasMap.get(param);
			if (ToolsUtil.isNull(p)) {
				logContent.append(param).append(",");
				success = false;
			}
		}
		if (!success) {
			logger.info("***必传参数:{} 为空！", logContent.toString());
		}
		ValidateResultVO result = new ValidateResultVO();
		result.setSuccess(success);
		result.setParams(parmasMap);
		return result;
	}
}
