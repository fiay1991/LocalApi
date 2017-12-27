package com.park.localapi.service.impl;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csvreader.CsvReader;
import com.park.base.common.DataChangeTools;
import com.park.base.common.DateTools;
import com.park.base.common.ResultTools;
import com.park.base.common.ToolsUtil;
import com.park.base.common.domain.ObjectResponse;
import com.park.base.common.domain.Response;
import com.park.localapi.common.constants.LocalCodeConstants;
import com.park.localapi.common.tools.HttpRequestTools;
import com.park.localapi.dao.ParkDao;
import com.park.localapi.domain.vo.ReconciliationVO;
import com.park.localapi.service.AbstractService;
import com.park.localapi.service.ImageService;
import com.park.localapi.service.PublicService;
import com.park.localapi.service.config.CloudCoreURLConfig;

@Service(value = "reconciliationService")
public class ReconciliationServiceImpl extends AbstractService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PublicService publicService;
	@Autowired
	private CloudCoreURLConfig cloudCoreURLConfig;
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private ImageService imageService;

	@Override
	public void setMustParams() {
		// 必传参数
		this.mustParams = new String[] { "starttime", "endtime" };
	}

	@Override
	public String executeService(Map<String, String> paramsMap) {
		try {
			logger.info("***对账车场 : " + paramsMap.get("parkid") + "对账时间 : starttime:" + paramsMap.get("starttime")
					+ "***endtime:" + paramsMap.get("endtime"));
			// 判断是否有parkId
			if (paramsMap.get("parkid") == null) {
				// 查询所有parkId
				ArrayList<Integer> parkIdList = parkDao.findAllParkId();
				// 遍历parkIdList
				for (int parkId : parkIdList) {
					String timeCheck = timeCheck(paramsMap.get("starttime"), paramsMap.get("endtime"), "" + parkId);
					Response gson2bean = DataChangeTools.gson2bean(timeCheck, Response.class);
					if ("200".equals(gson2bean.getCode())) {
						logger.info("***对账成功 对账日期:" + paramsMap.get("starttime") + "至" + paramsMap.get("endtime")
								+ "成功车场:" + parkId);
					} else {
						logger.info("***对账失败 对账日期:" + paramsMap.get("starttime") + "至" + paramsMap.get("endtime")
								+ "失败车场:" + parkId);
					}
				}
			} else {
				String timeCheck = timeCheck(paramsMap.get("starttime"), paramsMap.get("endtime"),
						paramsMap.get("parkid"));
				Response gson2bean = DataChangeTools.gson2bean(timeCheck, Response.class);
				if ("200".equals(gson2bean.getCode())) {
					return ResultTools.setResponse(LocalCodeConstants.SUCCESS,
							LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
				} else {
					logger.info("对账失败 请求参数:" + paramsMap.get("parkid") + paramsMap.get("starttime")
							+ paramsMap.get("endtime"));
					return ResultTools.setResponse(LocalCodeConstants.ERROR,
							LocalCodeConstants.getName(LocalCodeConstants.ERROR));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("对账时异常" + e);
			return ResultTools.setResponse(LocalCodeConstants.ERROR,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}
		return ResultTools.setResponse(LocalCodeConstants.SUCCESS,
				LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));

	}

	/**
	 * 日期对账
	 * 
	 * @param startTime
	 * @param endTime
	 * @param parkId
	 * @return
	 */
	public String timeCheck(String startTime, String endTime, String parkId) {
		int startTimeNew = DateTools.timeStr2seconds(startTime);
		int endTimeNew = DateTools.timeStr2seconds(endTime);
		int times = (endTimeNew - startTimeNew) / 60 / 60 / 24 + 1;
		String checkBill = "";
		for (int i = 0; i < times; i++) {
			checkBill = checkBill(startTimeNew, parkId);
			startTimeNew = startTimeNew + 60 * 60 * 24;
			Response gson2bean = DataChangeTools.gson2bean(checkBill, Response.class);
			if ("200".equals(gson2bean.getCode())) {

			} else {
				logger.info("对账失败startTimeNew=" + startTimeNew + "parkId=" + parkId);
			}
		}
		return checkBill;
	}

	/**
	 * 对账
	 * 
	 * @param time
	 * @param parkId
	 * @return
	 */
	public String checkBill(int time, String parkId) {
		try {
			int endTime = time + 60 * 60 * 24;
			String startTime = DateTools.secondTostringDate3(time);
			// 查询是否重复对账
			Map<String, String> checkReconciliationMap = new HashMap<String, String>();
			checkReconciliationMap.put("park_id", parkId);
			checkReconciliationMap.put("report_date", startTime);
			int checkReconciliationCount = publicService.checkReconciliation(checkReconciliationMap);
			// 获取平台各项总金额
			Map<String, String> getPlatformMap = new HashMap<String, String>();
			getPlatformMap.put("parkId", parkId);
			getPlatformMap.put("payTimeStart", "" + time);
			getPlatformMap.put("payTimeEnd", "" + endTime);
			getPlatformMap.put("payStatus", "2");
			String getPlatformUrl = cloudCoreURLConfig.getSumTradeOrder();
			ObjectResponse getPlatformResponse = HttpRequestTools.requestCloudCore(getPlatformMap, getPlatformUrl);
			if (!"200".equals(getPlatformResponse.getCode())) {
				logger.info("获取平台各项总金额失败;请求参数:" + getPlatformMap);
				return ResultTools.setResponse(LocalCodeConstants.ERROR_404,
						LocalCodeConstants.getName(LocalCodeConstants.ERROR_404));
			}
			String bean2gson = DataChangeTools.bean2gson(getPlatformResponse.getData());
			Map<String, String> getPlatform = DataChangeTools.json2Map(bean2gson);
			if (checkReconciliationCount == 0) {
				// 添加平台总对账数据
				Map<String, String> addPlatformMap = new HashMap<String, String>();
				addPlatformMap.put("park_id", parkId);
				addPlatformMap.put("report_date", startTime);
				addPlatformMap.put("local_count", "0");
				addPlatformMap.put("local_price_total", "0");
				addPlatformMap.put("local_price_paid", "0");
				addPlatformMap.put("local_price_discount", "0");
				addPlatformMap.put("platform_count",
						getPlatform.get("totalCount") == null ? "0" : getPlatform.get("totalCount"));
				addPlatformMap.put("platform_price_total",
						getPlatform.get("totalCostBefore") == null ? "0" : getPlatform.get("totalCostBefore"));
				addPlatformMap.put("platform_price_paid",
						getPlatform.get("totalCostAfter") == null ? "0" : getPlatform.get("totalCostAfter"));
				addPlatformMap.put("platform_price_discount",
						getPlatform.get("totalDiscount") == null ? "0" : getPlatform.get("totalDiscount"));
				parkDao.addFinalReconciliation(addPlatformMap);
			}
			ArrayList<String[]> csvList = new ArrayList<String[]>(); // 用来保存数据
			Map<String, String> csvFilePathMap = new HashMap<String, String>();
			csvFilePathMap.put("park_id", parkId);
			csvFilePathMap.put("time", startTime);
			// 查账对账文件路径
			String csvFilePath = parkDao.findBillPath(csvFilePathMap);
			if (csvFilePath == null) {
				Map<String, String> updateFinalReconciliationResultMap = new HashMap<String, String>();
				updateFinalReconciliationResultMap.put("park_id", parkId);
				updateFinalReconciliationResultMap.put("report_date", startTime);
				updateFinalReconciliationResultMap.put("result", "2");// 对账结果标示:0:对账一致1:对账不一致'2:对账文件不存在
				parkDao.updateFinalReconciliationResult(updateFinalReconciliationResultMap);
				logger.info("查询对账文件路径为空,对账文件不存在" + updateFinalReconciliationResultMap);
				return ResultTools.setResponse(LocalCodeConstants.SUCCESS,
						LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
			}
			logger.info("***下载OSS上文件的路径 csvFilePath:" + csvFilePath);
			File oss2File = imageService.getOSS2File(csvFilePath);
			String path = oss2File.getPath();
			CsvReader reader = new CsvReader(path, ',', Charset.forName("UTF-8"));// 一般用这编码读就可以了
			while (reader.readRecord()) { // 逐行读入除表头的数据
				csvList.add(reader.getValues());
			}
			reader.close();
			oss2File.delete();

			// 获取文件头
			String[] strings = csvList.get(0);
			logger.info("***获取csv文件头:" + strings.toString());
			String report_date = strings[0];
			// 去除BOM标记
			if (report_date.startsWith("\uFEFF"))
				report_date = report_date.replace("\uFEFF", "");
			String stringToDate = DateTools.stringToDate(report_date);

			double local_price_total = Float.parseFloat(strings[2] == null ? "0" : strings[2]) / 100D;
			double local_price_paid = Float.parseFloat(strings[3] == null ? "0" : strings[3]) / 100D;
			double local_price_discount = Float.parseFloat(strings[4] == null ? "0" : strings[4]) / 100D;

			Map<String, String> headMap = new HashMap<String, String>();
			headMap.put("report_date", stringToDate);
			headMap.put("park_id", parkId);
			headMap.put("local_count", "" + strings[1] == null ? "0" : strings[1]);
			headMap.put("local_price_total", "" + local_price_total);
			headMap.put("local_price_paid", "" + local_price_paid);
			headMap.put("local_price_discount", "" + local_price_discount);
			logger.info("***csv文件头Map:" + strings);
			// 比较各项总金额
			if (local_price_total != ToolsUtil.parseDouble((getPlatform.get("totalCostBefore")))
					|| local_price_paid != ToolsUtil.parseDouble((getPlatform.get("totalCostAfter")))
					|| local_price_discount != ToolsUtil.parseDouble((getPlatform.get("totalDiscount")))) {
				headMap.put("result", "1");
				headMap.put("platform_count",
						getPlatform.get("totalCount") == null ? "0" : getPlatform.get("totalCount"));
				headMap.put("platform_price_total",
						getPlatform.get("totalCostBefore") == null ? "0" : getPlatform.get("totalCostBefore"));
				headMap.put("platform_price_paid",
						getPlatform.get("totalCostAfter") == null ? "0" : getPlatform.get("totalCostAfter"));
				headMap.put("platform_price_discount",
						getPlatform.get("totalDiscount") == null ? "0" : getPlatform.get("totalDiscount"));
				// update总对账
				parkDao.updateFinalReconciliation(headMap);
			} else {
				headMap.put("result", "0");
				headMap.put("platform_count",
						getPlatform.get("totalCount") == null ? "0" : getPlatform.get("totalCount"));
				headMap.put("platform_price_total",
						getPlatform.get("totalCostBefore") == null ? "0" : getPlatform.get("totalCostBefore"));
				headMap.put("platform_price_paid",
						getPlatform.get("totalCostAfter") == null ? "0" : getPlatform.get("totalCostAfter"));
				headMap.put("platform_price_discount",
						getPlatform.get("totalDiscount") == null ? "0" : getPlatform.get("totalDiscount"));
				// update总对账
				parkDao.updateFinalReconciliation(headMap);
			}
			// 删除重复的对账明细
			parkDao.delReconciliationInfo(headMap);
			// 循环获取文件体
			for (int row = 1; row < csvList.size(); row++) {
				String[] oneRow = csvList.get(row);
				Map<String, String> rowMap = new HashMap<String, String>();
				rowMap.put("park_id", parkId);
				rowMap.put("trade_no", oneRow[0]);
				rowMap.put("order_num", oneRow[1]);
				rowMap.put("local_order_id", oneRow[2]);
				rowMap.put("pay_type", oneRow[3]);
				rowMap.put("pay_way", oneRow[4]);
				rowMap.put("local_price_total", oneRow[5] == null ? "0" : "" + Double.parseDouble(oneRow[5]) / 100D);
				rowMap.put("local_price_paid", oneRow[6] == null ? "0" : "" + Double.parseDouble(oneRow[6]) / 100D);
				rowMap.put("local_price_discount", oneRow[7] == null ? "0" : "" + Double.parseDouble(oneRow[7]) / 100D);
				rowMap.put("local_discount_from", oneRow[8]);
				rowMap.put("pay_time", oneRow[9] == null ? "0" : oneRow[9]);
				rowMap.put("platform_price_total", "0");
				rowMap.put("platform_price_paid", "0");
				rowMap.put("platform_price_discount", "0");
				rowMap.put("report_date", stringToDate);
				String plate = parkDao.findPlate(rowMap.get("order_num"));
				rowMap.put("plate_number", plate);
				parkDao.addReconciliation(rowMap);
			}
			Map<String, String> findBillMap = new HashMap<String, String>();
			findBillMap.put("park_id", parkId);
			findBillMap.put("time", "" + time);
			findBillMap.put("endTime", "" + endTime);
			List<ReconciliationVO> reconciliationVOList = parkDao.findBillList(findBillMap);
			logger.info("对账详情查询 请求参数:" + findBillMap + "返回结果条数" + reconciliationVOList.size());
			for (int i = 0; i < reconciliationVOList.size(); i++) {
				ReconciliationVO reconciliationVO = reconciliationVOList.get(i);
				reconciliationVO.setPlatform_price_total(reconciliationVO.getCost_before());
				reconciliationVO.setPlatform_price_paid(reconciliationVO.getCost_after());
				reconciliationVO.setPlatform_price_discount(reconciliationVO.getDiscount_amount());
				// String plate = parkDao.findPlate(reconciliationVO.getOrder_num());
				// reconciliationVO.setPlate_number(plate);
				// 加ToolsUtil.parseDouble()是为了防止空指针
				if (!ToolsUtil.parseDouble(reconciliationVO.getLocal_price_total())
						.equals(reconciliationVO.getCost_before())
						|| !ToolsUtil.parseDouble(reconciliationVO.getLocal_price_paid())
								.equals(reconciliationVO.getCost_after())
						|| !ToolsUtil.parseDouble(reconciliationVO.getDiscount_amount())
								.equals(reconciliationVO.getLocal_price_discount())) {
					if (reconciliationVO.getCost_after() == null && reconciliationVO.getCost_before() == null
							&& reconciliationVO.getDiscount_amount() == null) {
						reconciliationVO.setPlatform_price_total(0.00);
						reconciliationVO.setPlatform_price_paid(0.00);
						reconciliationVO.setPlatform_price_discount(0.00);
						reconciliationVO.setResult(4);// 本地有平台无
					} else if (reconciliationVO.getLocal_price_total() == null
							&& reconciliationVO.getLocal_price_paid() == null
							&& reconciliationVO.getLocal_price_discount() == null) {
						reconciliationVO.setResult(3);// 平台有本地无
					} else {
						reconciliationVO.setResult(2);// 不一致
					}
				} else {
					reconciliationVO.setResult(1);// 一致
				}
				if (reconciliationVO.getResult() == 3) {
					reconciliationVO.setLocal_price_total(0.0);
					reconciliationVO.setLocal_price_paid(0.0);
					reconciliationVO.setLocal_price_discount(0.0);
					reconciliationVO.setPark_id(Integer.parseInt(parkId));
					reconciliationVO.setOrder_num(reconciliationVO.getPf_order_num());
					reconciliationVO.setLocal_order_id(reconciliationVO.getPf_local_order_id());
					reconciliationVO.setReport_date(stringToDate);
					parkDao.addReconciliationInfo(reconciliationVO);
				} else {
					parkDao.updateReconciliationInfo(reconciliationVO);
				}
			}
		} catch (Exception ex) {
			logger.info("解析csv时异常:" + ex);
			return ResultTools.setResponse(LocalCodeConstants.ERROR,
					LocalCodeConstants.getName(LocalCodeConstants.ERROR));
		}

		return ResultTools.setResponse(LocalCodeConstants.SUCCESS,
				LocalCodeConstants.getName(LocalCodeConstants.SUCCESS));
	}
}
