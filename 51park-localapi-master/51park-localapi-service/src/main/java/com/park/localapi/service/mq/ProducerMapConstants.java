/**
 * 
 */
package com.park.localapi.service.mq;

import java.util.HashMap;
import java.util.Map;
import com.aliyun.openservices.ons.api.Producer;

/**
 * @author fangct 
 * created on 2017年11月7日
 */
public class ProducerMapConstants {
	/**
	 * MQ下发的车场Producer对象集合
	 */
	public static final Map<String, Producer> PRODUCER_MAP = new HashMap<String, Producer>();

}
