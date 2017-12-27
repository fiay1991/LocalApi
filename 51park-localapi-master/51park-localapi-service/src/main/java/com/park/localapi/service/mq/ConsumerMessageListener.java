package com.park.localapi.service.mq;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.park.base.common.DataChangeTools;
import com.park.base.common.ToolsUtil;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.common.constants.SendType;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.service.SendStatusService;
import com.park.localapi.service.redis.RedisUtils;

public class ConsumerMessageListener implements MessageListener {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource(name = "redisUtils")
	private RedisUtils redisUtils;

	@Resource(name = "sendStatusServiceImpl")
	private SendStatusService sendStatusService;
	
	@Resource(name = "parkDao")
	private ParkDao parkDao;

	public Action consume(Message message, ConsumeContext context) {
		try {	
			// 解析mq内容
			if (null != message && message.getBody().length > 0) {
				logger.info("Receive msgbody:" + new String(message.getBody(), "utf-8"));
				String body = new String(message.getBody(), "utf-8");
				Map<String, String> paramsMap = DataChangeTools.json2Map(body);
				
				String messageID = paramsMap.get("MessageID");
				String parkCode = paramsMap.get("ParkId");
				String code = paramsMap.get("code");
				String type = paramsMap.get("Type");
				String data = paramsMap.get("data");
				
				int flag = 0;//1：普通回写（需要通知TimeTask）2：不需要通知TimeTask
				//验证入参有效性
				if (ToolsUtil.isNull(messageID) || ToolsUtil.isNull(parkCode) || ToolsUtil.isNull(code) || ToolsUtil.isNull(type)) {
					logger.info("***本地响应参数不合法, msgid:{}", messageID);
					// 获取业务类型 缴费和开关闸 写入redis
					if (type.equals(SendType.缴费查询.getCode()) || type.equals(SendType.远程控制开关闸.getCode())) {
						// 写入redis空值
						redisUtils.set(messageID, data);
					}
				}else{
					// 获取业务类型 缴费和开关闸 写入redis
					if (type.equals(SendType.缴费查询.getCode()) || type.equals(SendType.远程控制开关闸.getCode())) {
						flag = 2;
						if(code.equals(LocalCodeConstants.SUCCESS)){//如果本地处理成功
							// 获取下发信息的msgid
							logger.info("***开始写入redis的键{}值{}...", messageID, data);
							// 写入redis
							redisUtils.set(messageID, data);
							logger.info("***写入redis成功.");
						} else {// 其他下发修改数据库状态
							// 写入redis, 置空，为了可以尽快根据msgid获取到结果
							redisUtils.set(messageID, null);
							logger.info("***本地处理失败, msgid:{}, 返回提示信息msg:{}", messageID, paramsMap.get("msg"));
						}
					}else{
						flag = 1;
					}
					/*
					 ****回写本地的返回结果，或通知TimeTask下发结果
					 */
					String parkId = parkDao.getParkIdByCode(parkCode);
					if(flag == 1){
						sendStatusService.updateStatus(paramsMap, code, parkId);
					}else if(flag == 2){
						sendStatusService.backUpdate(body, messageID, parkId, type);
					}
				}
			}
			return Action.CommitMessage;
		} catch (Exception e) {
			// 消费失败
			try {
				logger.error("body:{}, 作为消费者处理返回结果异常, {}", new String(message.getBody(), "utf-8"), e);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return Action.ReconsumeLater;
		}
	}
}
