package com.park.localapi.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.park.base.common.DataChangeTools;
import com.park.base.common.DateTools;
import com.park.base.common.ResultTools;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.dao.LocalUserOndutyDao;
import com.park.localapi.dao.LocalUserinfoDao;
import com.park.localapi.dao.LoginOutDao;
import com.park.localapi.domain.LocalUserOnduty;
import com.park.localapi.domain.LoginOutRecord;
import com.park.localapi.service.LoginOutService;
import com.park.localapi.service.PublicService;

@Service(value = "loginOutService")
public class LoginOutServiceImpl implements LoginOutService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "loginOutDao")
	private LoginOutDao loginOutDao;

	@Resource(name = "localUserOndutyDao")
	private LocalUserOndutyDao localUserOndutyDao;

	@Resource(name = "localUserinfoDao")
	private LocalUserinfoDao localUserinfoDao;

	@Resource(name = "publicService")
	private PublicService publicService;

	public String loginOutRecord(String params) {
		Map<String, String> paramsMap = DataChangeTools.json2Map(params);
		// 验证入参有效性
		if (paramsMap.get("parkid") == null || paramsMap.get("useraccount") == null
				|| paramsMap.get("gatenames") == null || paramsMap.get("optype") == null
				|| paramsMap.get("optime") == null) {
			logger.info("***必填参数未传, {}", params);
			return ResultTools.setResponse(LocalCodeConstants.ERROR_400, LocalCodeConstants.getName(LocalCodeConstants.ERROR_400));
		} else {
			// 将parkcode转换成parkid
			paramsMap = publicService.initParamsMap(paramsMap);

			/*
			 * 验证账号有效性
			 */
			int count = localUserinfoDao.existUserAccount(paramsMap.get("useraccount"), paramsMap.get("parkid"));
			if (count == 0) {
				logger.info("***不存在的账号, {}", params);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_410, LocalCodeConstants.getName(LocalCodeConstants.ERROR_410));
			}
			/*
			 * 封装并插入登入登出对象
			 */
			LoginOutRecord record = new LoginOutRecord();
			record.setPark_id(Integer.parseInt(paramsMap.get("parkid")));
			record.setUser_account(paramsMap.get("useraccount"));
			record.setGatnames(paramsMap.get("gatenames"));
			int optype = Integer.parseInt(paramsMap.get("optype"));
			record.setOptype(optype);
			record.setOptime(Integer.parseInt(paramsMap.get("optime")));
			record.setCreate_time(DateTools.phpnowDate());
			int n = loginOutDao.insert(record);
			if (n < 1) {
				logger.info("***登入登出记录插入数据库表失败, 参数：{} ", params);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_407, LocalCodeConstants.getName(LocalCodeConstants.ERROR_407));
			}
			/*
			 * 操作当班人员记录
			 */
			LocalUserOnduty onduty = new LocalUserOnduty();
			if (optype == 0) {//插入当班记录
				BeanUtils.copyProperties(record, onduty);
				onduty.setCreate_time(DateTools.phpnowDate());
				n = localUserOndutyDao.insert(onduty);
			} else if (optype == 1) {//删除当班记录
				n = localUserOndutyDao.deleteOnduty(""+record.getPark_id(), record.getUser_account());
			}
			if (n < 1) {
				logger.info("***操作当班人员信息表失败, 参数：{} ", params);
			}
			return ResultTools.setResponse(LocalCodeConstants.SUCCESS, LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
		}
	}

}
