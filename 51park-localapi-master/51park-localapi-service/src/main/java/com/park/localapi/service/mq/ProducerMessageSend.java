package com.park.localapi.service.mq;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.park.base.common.DataChangeTools;
import com.park.base.common.RSATools;
import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.common.constants.SendType;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.config.MQConfig;
import com.park.localapi.service.impl.SendStatusServiceImpl;

@Repository(value = "ProducerMessageSend")
public class ProducerMessageSend {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MQConfig mqConfig;
	@Autowired
	private SendStatusServiceImpl sendStatusServiceImpl;
	@Autowired
	private ParkDao parkDao;

	/**
	 * 发送至MQ
	 * 
	 * @param parkcode
	 * @param topic
	 * @param tag
	 * @param data
	 * @param businessCode
	 * @param businessKey
	 * @param dataMap_Json
	 * @param parkCode2
	 * @param key(business_key)
	 * @return
	 */
	public String sendMessage(String parkId, String tag, String data, String key, String businessKey,
			String businessCode, String parkCode, String dataMap_Json) {
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.ProducerId, mqConfig.getProducerId().replace("*", parkCode));
		properties.put(PropertyKeyConst.AccessKey, mqConfig.getAccessKey());
		properties.put(PropertyKeyConst.SecretKey, mqConfig.getSecretKey());
		properties.put(PropertyKeyConst.ONSAddr, mqConfig.getONSAddr());
		Producer producer = null;
		if (ProducerMapConstants.PRODUCER_MAP.get(parkCode) != null) {
			logger.info("***Parkcode:{}, 已创建过Producer对象，直接从Map中读取...", parkCode);
			producer = ProducerMapConstants.PRODUCER_MAP.get(parkCode);
			if (producer.isClosed()) {
				producer.start();
			}
		} else {
			logger.info("***Parkcode:{}, 第一次创建Producer对象...", parkCode);
			producer = ONSFactory.createProducer(properties);
			producer.start();
			ProducerMapConstants.PRODUCER_MAP.put(parkCode, producer);
		}
		// 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
		String topic = mqConfig.getProducer_Topic().replace("*", parkCode);
		Message msg = null;
		String msgId = "";
		try {
			msg = new Message(
					// Message Topic
					topic,
					// Message Tag,
					// 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在ONS服务器过滤
					tag,
					// Message Body
					// 任何二进制形式的数据，ONS不做任何干预，需要Producer与Consumer协商好一致的序列化和反序列化方式
					data.getBytes("utf-8"));
			// 设置代表消息的业务关键属性，请尽可能全局唯一。
			// 以方便您在无法正常收到消息情况下，可通过ONS Console查询消息并补发。
			// 注意：不设置也不会影响消息正常收发
			msg.setKey(key);
			// msg.setReconsumeTimes(0);
			// 发送消息，只要不抛异常就是成功
			logger.info("***id为{}的车场,开始发送消息:" + msg.getBody().toString(), parkId);
			SendResult sendResult = producer.send(msg);
			logger.info("***id为{}的车场,发送消息结束,结果为:" + sendResult, parkId);
			msgId = sendResult.getMessageId();
			// 存库
			sendStatusServiceImpl.addMqRecord(parkId, businessKey, businessCode, topic, key, dataMap_Json, msgId);
			logger.info("***id为{}的车场,发送本地的data数据：{}", parkId, dataMap_Json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("***id为{}的车场,发送消息失败,发生数据为:{},异常信息为:" + e, parkId, dataMap_Json);
		} // finally{
		// // 在应用退出前，销毁Producer对象
		// // 注意：如果不销毁也没有问题
		// //producer.shutdown();
		// }
		return msgId;
	}

	/**
	 * 发送MQ
	 * 
	 * @param dataMap
	 * @param parkId
	 * @param parkCode
	 * @param businessKey
	 * @param businessCode
	 * @param methodName
	 * @return
	 */
	public String issueMQLocal(Object requestObj, String parkId, String parkCode, String businessKey,
			String businessCode, String methodName) {
		try {
			String dataMap_Json = DataChangeTools.bean2gson(requestObj);
			Map<String, String> parkKeys = parkDao.getParkKey(parkId);
			String encrpytStr = RSATools.encrpyt(dataMap_Json, parkKeys.get("public_key"));
			Map<String, String> messageMap = new HashMap<String, String>();
			messageMap.put("ParkId", parkCode);
			messageMap.put("Type", businessCode);
			messageMap.put("data", encrpytStr);
			String messageJson = DataChangeTools.bean2gson(messageMap);
			UUID uuid = UUID.randomUUID();
			String key = methodName + "_" + uuid;
			String body = messageJson;
			String msgId = sendMessage(parkId, "", body, key, businessKey, businessCode, parkCode, dataMap_Json);

			if (businessCode.equals(SendType.缴费查询.getCode()) || businessCode.equals(SendType.远程控制开关闸.getCode())) {
				return msgId;
			}
			if (msgId != null) {
				return ResultTools.setResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
			} else {
				return ResultTools.setResponse(LocalCodeConstants.ERROR_406, LocalCodeConstants.getName(LocalCodeConstants.ERROR_406));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("下发本地失败 异常:" + e);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_406, LocalCodeConstants.getName(LocalCodeConstants.ERROR_406));
		}

	}

}
