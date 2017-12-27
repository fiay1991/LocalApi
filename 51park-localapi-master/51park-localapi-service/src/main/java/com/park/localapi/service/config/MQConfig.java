package com.park.localapi.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("MQConfig")
public class MQConfig {
	
	@Value("${MQ.ProducerId}")
	private String ProducerId;
	@Value("${MQ.AccessKey}")
	private String AccessKey;
	@Value("${MQ.SecretKey}")
	private String SecretKey;
	@Value("${MQ.Producer_Topic}")
	private String Producer_Topic;
	@Value("${MQ.Consumer_Topic}")
	private String Consumer_Topic;
	@Value("${MQ.ONSAddr}")
	private String ONSAddr;
	public String getAccessKey() {
		return AccessKey;
	}
	public void setAccessKey(String accessKey) {
		AccessKey = accessKey;
	}
	public String getSecretKey() {
		return SecretKey;
	}
	public void setSecretKey(String secretKey) {
		SecretKey = secretKey;
	}
	public String getProducer_Topic() {
		return Producer_Topic;
	}
	public void setProducer_Topic(String producer_Topic) {
		Producer_Topic = producer_Topic;
	}
	public String getConsumer_Topic() {
		return Consumer_Topic;
	}
	public void setConsumer_Topic(String consumer_Topic) {
		Consumer_Topic = consumer_Topic;
	}
	public String getONSAddr() {
		return ONSAddr;
	}
	public void setONSAddr(String oNSAddr) {
		ONSAddr = oNSAddr;
	}
	public String getProducerId() {
		return ProducerId;
	}
	public void setProducerId(String producerId) {
		ProducerId = producerId;
	}
}
