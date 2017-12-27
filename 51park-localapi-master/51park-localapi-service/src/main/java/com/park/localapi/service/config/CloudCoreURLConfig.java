package com.park.localapi.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("cloudCoreURLConfig")
public class CloudCoreURLConfig {
	
	@Value("${CloudCoreURL.addOrderInfo}")
	private String addOrderInfo;
	
	@Value("${CloudCoreURL.modBaseOrderInfo}")
	private String modBaseOrderInfo;
	
	@Value("${CloudCoreURL.modCostOrderInfo}")
	private String modCostOrderInfo;
	
	@Value("${CloudCoreURL.countOrderInfo}")
	private String countOrderInfo;
	
	@Value("${CloudCoreURL.findByOneOrderInfo}")
	private String findByOneOrderInfo;
	
	@Value("${CloudCoreURL.findsByCarinfoOrderInfo}")
	private String findsByCarinfoOrderInfo;
	
	@Value("${CloudCoreURL.findsOrderInfo}")
	private String findsOrderInfo;
	
	@Value("${CloudCoreURL.addOrderPayRecord}")
	private String addOrderPayRecord;
	
	@Value("${CloudCoreURL.modBaseOrderPayRecord}")
	private String modBaseOrderPayRecord;
	
	@Value("${CloudCoreURL.modStatusOrderPayRecord}")
	private String modStatusOrderPayRecord;
	
	@Value("${CloudCoreURL.findOrderPayRecord}")
	private String findOrderPayRecord;
	
	@Value("${CloudCoreURL.sumOrderPayRecord}")
	private String sumOrderPayRecord;
	
	@Value("${CloudCoreURL.addOrderModifyRecord}")
	private String addOrderModifyRecord;
	
	@Value("${CloudCoreURL.findsOrderModifyRecord}")
	private String findsOrderModifyRecord;
	
	@Value("${CloudCoreURL.addOrderEnExRecord}")
	private String addOrderEnExRecord;
	
	@Value("${CloudCoreURL.modOrderEnExRecord}")
	private String modOrderEnExRecord;
	
	@Value("${CloudCoreURL.findsOrderEnExRecord}")
	private String findsOrderEnExRecord;
	
	@Value("${CloudCoreURL.addDiscount}")
	private String addDiscount;
	
	@Value("${CloudCoreURL.modByOrdernumDiscount}")
	private String modByOrdernumDiscount;
	
	@Value("${CloudCoreURL.modByNoDiscount}")
	private String modByNoDiscount;
	
	@Value("${CloudCoreURL.findDiscount}")
	private String findDiscount;
	
	@Value("${CloudCoreURL.findsDiscount}")
	private String findsDiscount;
	
	@Value("${CloudCoreURL.sumTradeOrder}")
	private String sumTradeOrder;
	
	@Value("${CloudCoreURL.queryPayment}")
	private String queryPayment;

	/**
	 * 新增订单
	 * 必填参数:
	 * parkId;
	 * plateNumber/cardId/ticketId;
	 * enterTime;
	 * serviceStatus;
	 * @return
	 */
	public String getAddOrderInfo() {
		return addOrderInfo;
	}

	public void setAddOrderInfo(String addOrderInfo) {
		this.addOrderInfo = addOrderInfo;
	}
	/**
	 * 修改订单基本信息
	 * 必填参数:orderNum;
	 * @return
	 */
	public String getModBaseOrderInfo() {
		return modBaseOrderInfo;
	}

	public void setModBaseOrderInfo(String modBaseOrderInfo) {
		this.modBaseOrderInfo = modBaseOrderInfo;
	}
	/**
	 * 修改订单支付金额
	 * 必填参数:
	 * orderNum;
	 * costBefore;
	 * costAfter/prepayAmount/prepayTime(orderNum为空必填);
	 * @return
	 */
	public String getModCostOrderInfo() {
		return modCostOrderInfo;
	}

	public void setModCostOrderInfo(String modCostOrderInfo) {
		this.modCostOrderInfo = modCostOrderInfo;
	}
	/**
	 * 查询订单数量
	 * 必填参数:parkId;
	 * @return
	 */
	public String getCountOrderInfo() {
		return countOrderInfo;
	}

	public void setCountOrderInfo(String countOrderInfo) {
		this.countOrderInfo = countOrderInfo;
	}
	/**
	 * 根据唯一条件查询订单详情
	 * 必填参数:id/orderNum/localOrderId;
	 * @return
	 */
	public String getFindByOneOrderInfo() {
		return findByOneOrderInfo;
	}

	public void setFindByOneOrderInfo(String findByOneOrderInfo) {
		this.findByOneOrderInfo = findByOneOrderInfo;
	}
	/**
	 * 根据车辆信息查询订单详情列表
	 * 必填参数:
	 * parkId;
	 * plateNumber/cardId/ticketId;
	 * @return
	 */
	public String getFindsByCarinfoOrderInfo() {
		return findsByCarinfoOrderInfo;
	}

	public void setFindsByCarinfoOrderInfo(String findsByCarinfoOrderInfo) {
		this.findsByCarinfoOrderInfo = findsByCarinfoOrderInfo;
	}
	/**
	 * 查询订单详情列表
	 * 必填参数:parkId;
	 * @return
	 */
	public String getFindsOrderInfo() {
		return findsOrderInfo;
	}

	public void setFindsOrderInfo(String findsOrderInfo) {
		this.findsOrderInfo = findsOrderInfo;
	}
	/**
	 * 新增交易流水
	 * 必填参数:
	 * parkId;
	 * orderNum;
	 * costBefore;
	 * costAfter;
	 * payStatus;
	 * @return
	 */
	public String getAddOrderPayRecord() {
		return addOrderPayRecord;
	}

	public void setAddOrderPayRecord(String addOrderPayRecord) {
		this.addOrderPayRecord = addOrderPayRecord;
	}
	/**
	 * 修改交易流水的基本信息
	 * 必填参数:
	 * tradeNo;
	 * payTime;
	 * @return
	 */
	public String getModBaseOrderPayRecord() {
		return modBaseOrderPayRecord;
	}

	public void setModBaseOrderPayRecord(String modBaseOrderPayRecord) {
		this.modBaseOrderPayRecord = modBaseOrderPayRecord;
	}
	/**
	 * 修改交易流水的状态
	 * 必填参数:
	 * orderNum/tradeNo;
	 * newPayStatus;
	 * @return
	 */
	public String getModStatusOrderPayRecord() {
		return modStatusOrderPayRecord;
	}

	public void setModStatusOrderPayRecord(String modStatusOrderPayRecord) {
		this.modStatusOrderPayRecord = modStatusOrderPayRecord;
	}
	/**
	 * 查询交易流水详情
	 * 必填参数:id/tradeNo;
	 * @return
	 */
	public String getFindOrderPayRecord() {
		return findOrderPayRecord;
	}

	public void setFindOrderPayRecord(String findOrderPayRecord) {
		this.findOrderPayRecord = findOrderPayRecord;
	}
	/**
	 * 查询统计交易流水
	 * 必填参数:
	 * @return
	 */
	public String getSumOrderPayRecord() {
		return sumOrderPayRecord;
	}

	public void setSumOrderPayRecord(String sumOrderPayRecord) {
		this.sumOrderPayRecord = sumOrderPayRecord;
	}
	/**
	 * 新增车牌校正记录
	 * 必填参数:
	 * parkId;
	 * orderNum;
	 * action;
	 * modifyType;
	 * @return
	 */
	public String getAddOrderModifyRecord() {
		return addOrderModifyRecord;
	}

	public void setAddOrderModifyRecord(String addOrderModifyRecord) {
		this.addOrderModifyRecord = addOrderModifyRecord;
	}
	/**
	 * 查询车牌校正记录列表
	 * 必填参数:
	 * @return
	 */
	public String getFindsOrderModifyRecord() {
		return findsOrderModifyRecord;
	}

	public void setFindsOrderModifyRecord(String findsOrderModifyRecord) {
		this.findsOrderModifyRecord = findsOrderModifyRecord;
	}
	/**
	 * 新增车辆进出场记录
	 * 必填参数:
	 * recordType;
	 * orderNum;
	 * parkId;
	 * deviceType;
	 * @return
	 */
	public String getAddOrderEnExRecord() {
		return addOrderEnExRecord;
	}

	public void setAddOrderEnExRecord(String addOrderEnExRecord) {
		this.addOrderEnExRecord = addOrderEnExRecord;
	}
	/**
	 * 修改车辆进出场记录
	 * 必填参数:
	 * orderNum;
	 * parkId;
	 * plateNumber;
	 * @return
	 */
	public String getModOrderEnExRecord() {
		return modOrderEnExRecord;
	}

	public void setModOrderEnExRecord(String modOrderEnExRecord) {
		this.modOrderEnExRecord = modOrderEnExRecord;
	}
	/**
	 * 查询车辆进出场记录列表
	 * 必填参数:
	 * @return
	 */
	public String getFindsOrderEnExRecord() {
		return findsOrderEnExRecord;
	}

	public void setFindsOrderEnExRecord(String findsOrderEnExRecord) {
		this.findsOrderEnExRecord = findsOrderEnExRecord;
	}
	/**
	 * 新增优惠
	 * 必填参数:
	 * parkId;
	 * orderNum;
	 * type;amount;
	 * fromType;
	 * discountTime;
	 * @return
	 */
	public String getAddDiscount() {
		return addDiscount;
	}

	public void setAddDiscount(String addDiscount) {
		this.addDiscount = addDiscount;
	}
	/**
	 * 根据订单号修改优惠
	 * 必填参数:
	 * orderNum;
	 * newStatus;
	 * @return
	 */
	public String getModByOrdernumDiscount() {
		return modByOrdernumDiscount;
	}

	public void setModByOrdernumDiscount(String modByOrdernumDiscount) {
		this.modByOrdernumDiscount = modByOrdernumDiscount;
	}
	/**
	 * 根据优惠编号修改优惠
	 * 必填参数:discountNo;
	 * @return
	 */
	public String getModByNoDiscount() {
		return modByNoDiscount;
	}

	public void setModByNoDiscount(String modByNoDiscount) {
		this.modByNoDiscount = modByNoDiscount;
	}
	/**
	 * 查询优惠
	 * 必填参数:id
	 * @return
	 */
	public String getFindDiscount() {
		return findDiscount;
	}

	public void setFindDiscount(String findDiscount) {
		this.findDiscount = findDiscount;
	}
	/**
	 * 查询优惠列表
	 * 必填参数:
	 * @return
	 */
	public String getFindsDiscount() {
		return findsDiscount;
	}

	public void setFindsDiscount(String findsDiscount) {
		this.findsDiscount = findsDiscount;
	}
	/**
	 * 查询统计某日交易流水
	 * 必填参数:
	 * parkId;
	 * payTimeStart;
	 * payTimeEnd;
	 * @return
	 */
	public String getSumTradeOrder() {
		return sumTradeOrder;
	}

	public void setSumTradeOrder(String sumTradeOrder) {
		this.sumTradeOrder = sumTradeOrder;
	}
	/**
	 * 缴费查询接口
	 * 必填参数:
	 * parkId;
	 * orderNum/plateNumber;
	 * @return
	 */
	public String getQueryPayment() {
		return queryPayment;
	}

	public void setQueryPayment(String queryPayment) {
		this.queryPayment = queryPayment;
	}
	
	

}
