package com.park.localapi.service.mq;

import java.util.Properties;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
public class ConsumerTest {
	public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, "CID_ZHANGYI");
        properties.put(PropertyKeyConst.AccessKey, "LTAIJNwsQD8VXAsF");
        properties.put(PropertyKeyConst.SecretKey, "BRVwC5VDZoE13I1dXJEBInORGU4Cxt");
        Consumer consumer = (Consumer) ONSFactory.createConsumer(properties);
        consumer.subscribe("S00002", "*", new ConsumerMessageListener());
        consumer.start();
        System.out.println("Consumer Started");
    }
}
