package com.park.localapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.park.base.common.DataChangeTools;
import com.park.base.common.DateTools;
import com.park.base.common.ResultTools;
import com.park.base.common.ToolsUtil;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.dao.MonthcardSyncDao;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.domain.MonthOperRecord;
import com.park.localapi.domain.MonthProduct;
import com.park.localapi.domain.request.MonthcardSyncRequest;
import com.park.localapi.domain.vo.MonthcardVO;
import com.park.localapi.service.MonthcardSyncService;
import com.park.localapi.service.mq.ProducerMessageSend;

@Service(value = "monthcardSyncService")
public class MonthcardSyncServiceImpl implements MonthcardSyncService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "monthcardSyncDao")
	private MonthcardSyncDao monthcardSyncDao;
	@Resource(name = "parkDao")
	private ParkDao parkDao;
	@Resource(name = "ProducerMessageSend")
	private ProducerMessageSend producerMessageSend;

	public String syncMonthcard(String params) {
		Map<String, String> paramsMap = DataChangeTools.json2Map(params);
		String parkId = paramsMap.get("parkId");
		String businessKey = paramsMap.get("businessKey");
		String businessCode = paramsMap.get("businessCode");
		MonthOperRecord operRecord = monthcardSyncDao.get(MonthOperRecord.class, businessKey);

		if (operRecord == null) {
			logger.info("***根据ID: {}, 未查询到月卡操作记录 ", businessKey);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		} else {
			if (operRecord.getPark_id() != ToolsUtil.parseInt(parkId)) {
				logger.info("***数据库表中的车场ID {} 和参数中的车场ID {} 不相同 ", operRecord.getPark_id(), parkId);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_402, LocalCodeConstants.getName(LocalCodeConstants.ERROR_402));
			}
			/*
			 * 封装请求本地的对象
			 */
			int productID = operRecord.getMonth_product_id();
			MonthProduct monthProduct = monthcardSyncDao.getMonthProductById(productID);
			String parkCode = parkDao.getParkCodeById(parkId);
			MonthcardSyncRequest requestObj = buildMonthcardSyncRequest(operRecord, monthProduct, parkCode);

			// 将数据下发到MQ
			return producerMessageSend.issueMQLocal(requestObj, parkId, parkCode, businessKey, businessCode,
					"syncmonthcard");
		}
	}

	/**
	 * 构建请求对象
	 * 
	 * @param operRecord
	 * @param monthProduct
	 * @param parkCode
	 * @return
	 */
	private MonthcardSyncRequest buildMonthcardSyncRequest(MonthOperRecord operRecord, MonthProduct monthProduct,
			String parkCode) {
		MonthcardSyncRequest requestObj = new MonthcardSyncRequest();
		requestObj.setParkid(parkCode);

		MonthcardVO vo = new MonthcardVO();
		vo.setCardid("" + operRecord.getMonth_info_id());
		vo.setPlate(operRecord.getPlate_num().replace(",", "/"));
		vo.setPhone(operRecord.getPhone());
		vo.setName(operRecord.getCard_owner());
		vo.setProperty(operRecord.getProperty());
		vo.setStartdate(operRecord.getStartdate());
		vo.setEnddate(operRecord.getEnddate());

		int action = operRecord.getOperation_type();
		if (action == 3) {
			action = 9;
		} else if (action == 4) {
			action = 3;
		}

		int type = monthProduct.getType();
		vo.setType(type);
		if (type == 1) {// 产品类型 0 全天卡 1 分时段卡
			vo.setStarttime(monthProduct.getStarttime());
			vo.setEndtime(monthProduct.getEndtime());
		}
		vo.setAction(action);
		vo.setPlotcount(operRecord.getCarnum());
		vo.setPaymoney(operRecord.getPayamount());
		vo.setUseraccount(operRecord.getOperator());
		vo.setRegion(monthProduct.getRegion());

		String createtime = DateTools.formatDateTime(operRecord.getCreate_time());
		int updateTime = DateTools.timeStr2seconds(createtime);
		vo.setUpdatetime(updateTime);

		List<MonthcardVO> carddata = new ArrayList<MonthcardVO>();
		carddata.add(vo);
		requestObj.setCarddata(carddata);
		return requestObj;
	}

}
