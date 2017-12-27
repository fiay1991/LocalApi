package com.park.localapi.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository(value="sendStatusDao")
public interface SendStatusDao {

	/**
	 * 保存MQ请求记录
	 * @param map
	 */
	void addMqRecord(Map<String, String> map);

	/**
	 * 更新MQ请求的返回结果
	 * @param map
	 */
	int updateResult(Map<String, String> map);
	
	/**
	 * 获取业务信息
	 * @param message_id
	 * @param parkid
	 * @return
	 */
	Map<String, Object> getBusinessInfo(@Param("message_id")String message_id, 
			@Param("parkid")String parkid, @Param("business_code")String business_code);
}
