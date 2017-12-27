package com.park.localapi.service.mq;
import java.util.Properties;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
public class ProducerClient {
	 public static void main(String[] args) {
	       Properties properties = new Properties();
	       properties.put(PropertyKeyConst.ProducerId, "PID_Parking56001_Producer");
	       properties.put(PropertyKeyConst.AccessKey, "LTAIJNwsQD8VXAsF");
	       properties.put(PropertyKeyConst.SecretKey, "BRVwC5VDZoE13I1dXJEBInORGU4Cxt");
	       //公有云生产环境：http://onsaddr-internal.aliyun.com:8080/rocketmq/nsaddr4client-internal
	       //公有云公测环境：http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet
	       //杭州金融云环境：http://jbponsaddr-internal.aliyun.com:8080/rocketmq/nsaddr4client-internal
	       //杭州深圳云环境：http://mq4finance-sz.addr.aliyun.com:8080/rocketmq/nsaddr4client-internal
	       properties.put(PropertyKeyConst.ONSAddr,
	          "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");//此处以公有云生产环境为例
	       Producer producer = ONSFactory.createProducer(properties);
	            
	       //在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
	       producer.start();
	       for (int i = 0; i < 1; i++) {
	    	   	Message msg = new Message(
	   	            //Message Topic
	   	            "Parking_56001_0826",
	   	            //Message Tag,
	   	            //可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在ONS服务器过滤       
	   	            "TagA",
	   	            //Message Body
	   	            //任何二进制形式的数据，ONS不做任何干预，需要Producer与Consumer协商好一致的序列化和反序列化方式
	   	            (i+"Hello MQqqqssss").getBytes()
	   	        );
	   	        // 设置代表消息的业务关键属性，请尽可能全局唯一。
	   	        // 以方便您在无法正常收到消息情况下，可通过ONS Console查询消息并补发。
	   	        // 注意：不设置也不会影响消息正常收发
	   	        msg.setKey("ORDERID_200");
	   	        //msg.setReconsumeTimes(0);
	   	        //发送消息，只要不抛异常就是成功
	   	        SendResult sendResult = producer.send(msg);
	   	        System.out.println(sendResult);
	   	        try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	       }
	        // 在应用退出前，销毁Producer对象
	        // 注意：如果不销毁也没有问题
	        producer.shutdown();
//		 String data ="拉取本地费用";
//		 ProducerMessageSend producerMessageSend =new ProducerMessageSend();
//		 producerMessageSend.SendMessage("PID_Parking56001_Producer","Parking_56001_0826","TagA",data,"billinfo");
//		 RedisUtils redisUtils =new RedisUtils();
//		 long startTime = System.currentTimeMillis();    //获取开始时间
//		 for (int i = 0; i < 100; i++) {
//			if(redisUtils.exists("bill")){
//				System.out.println("第"+i+"次循环获取到redis值："+redisUtils.get("bill"));
//				break;
//			}else {
//				System.out.println("第"+i+"次循环获");
//			}
//		}
//		 long endTime = System.currentTimeMillis();    //获取结束时间
//		 System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
	 }
}
