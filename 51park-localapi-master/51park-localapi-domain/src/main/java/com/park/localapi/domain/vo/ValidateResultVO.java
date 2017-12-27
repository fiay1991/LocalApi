/**
 * 
 */
package com.park.localapi.domain.vo;

import java.util.Map;

/**
 * @author fangct 
 * created on 2017年12月1日
 */
public class ValidateResultVO {

	private boolean success;//成功标识
	private Map<String, String> params;//参数对象
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
