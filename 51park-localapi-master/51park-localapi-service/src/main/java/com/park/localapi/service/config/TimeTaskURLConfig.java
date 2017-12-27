package com.park.localapi.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * 定时任务项目URL
 * @author fangct 
 * created on 2017年8月29日
 */
@Component("timeTaskURLConfig")
public class TimeTaskURLConfig {
	
	@Value("${TimeTaskURL.sendTaskResult}")
	private String sendTaskResult;

	public String getSendTaskResult() {
		return sendTaskResult;
	}

	public void setSendTaskResult(String sendTaskResult) {
		this.sendTaskResult = sendTaskResult;
	}
}
