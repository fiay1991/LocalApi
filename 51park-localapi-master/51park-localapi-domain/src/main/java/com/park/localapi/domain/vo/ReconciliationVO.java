package com.park.localapi.domain.vo;

public class ReconciliationVO {
	
	private Double cost_after;
	private Double cost_before;
	private Double discount_amount;
	private Integer park_id;//'停车场ID，对应cp_park(id)',
	private String trade_no;//'交易流水号',
	private String order_num;//'云端订单号',
	private String local_order_id;//'本地订单号',
	private Double local_price_total;//'本地总应收',
	private Double local_price_paid;//'本地总实收',
	private Double local_price_discount;//'本地总优惠',
	private Integer local_discount_from;//'本地优惠来源',
	private Double platform_price_total;//'平台总应收',
	private Double platform_price_paid;//'[平台总实收',
	private Double platform_price_discount;//'平台总优惠',
	private Integer from_type;
	private Integer result;
	private Double amount;
	private String pf_trade_no;
	private String pf_local_order_id;
	private String pf_order_num;
	private Integer pf_pay_way;
	private Integer pf_pay_type;
	private Integer pf_pay_time;
	private String plate_number;
	private String report_date;
	
	
	public String getReport_date() {
		return report_date;
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public Integer getPf_pay_time() {
		return pf_pay_time;
	}
	public void setPf_pay_time(Integer pf_pay_time) {
		this.pf_pay_time = pf_pay_time;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public Double getCost_after() {
		return cost_after;
	}
	public void setCost_after(Double cost_after) {
		this.cost_after = cost_after;
	}
	public Double getCost_before() {
		return cost_before;
	}
	public void setCost_before(Double cost_before) {
		this.cost_before = cost_before;
	}
	public Double getDiscount_amount() {
		return discount_amount;
	}
	public void setDiscount_amount(Double discount_amount) {
		this.discount_amount = discount_amount;
	}
	public Integer getPark_id() {
		return park_id;
	}
	public void setPark_id(Integer park_id) {
		this.park_id = park_id;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public String getLocal_order_id() {
		return local_order_id;
	}
	public void setLocal_order_id(String local_order_id) {
		this.local_order_id = local_order_id;
	}
	public Double getLocal_price_total() {
		return local_price_total;
	}
	public void setLocal_price_total(Double local_price_total) {
		this.local_price_total = local_price_total;
	}
	public Double getLocal_price_paid() {
		return local_price_paid;
	}
	public void setLocal_price_paid(Double local_price_paid) {
		this.local_price_paid = local_price_paid;
	}
	public Double getLocal_price_discount() {
		return local_price_discount;
	}
	public void setLocal_price_discount(Double local_price_discount) {
		this.local_price_discount = local_price_discount;
	}
	public Integer getLocal_discount_from() {
		return local_discount_from;
	}
	public void setLocal_discount_from(Integer local_discount_from) {
		this.local_discount_from = local_discount_from;
	}
	public Double getPlatform_price_total() {
		return platform_price_total;
	}
	public void setPlatform_price_total(Double platform_price_total) {
		this.platform_price_total = platform_price_total;
	}
	public Double getPlatform_price_paid() {
		return platform_price_paid;
	}
	public void setPlatform_price_paid(Double platform_price_paid) {
		this.platform_price_paid = platform_price_paid;
	}
	public Double getPlatform_price_discount() {
		return platform_price_discount;
	}
	public void setPlatform_price_discount(Double platform_price_discount) {
		this.platform_price_discount = platform_price_discount;
	}
	public Integer getFrom_type() {
		return from_type;
	}
	public void setFrom_type(Integer from_type) {
		this.from_type = from_type;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getPf_trade_no() {
		return pf_trade_no;
	}
	public void setPf_trade_no(String pf_trade_no) {
		this.pf_trade_no = pf_trade_no;
	}
	public String getPf_local_order_id() {
		return pf_local_order_id;
	}
	public void setPf_local_order_id(String pf_local_order_id) {
		this.pf_local_order_id = pf_local_order_id;
	}
	public String getPf_order_num() {
		return pf_order_num;
	}
	public void setPf_order_num(String pf_order_num) {
		this.pf_order_num = pf_order_num;
	}
	public Integer getPf_pay_way() {
		return pf_pay_way;
	}
	public void setPf_pay_way(Integer pf_pay_way) {
		this.pf_pay_way = pf_pay_way;
	}
	public Integer getPf_pay_type() {
		return pf_pay_type;
	}
	public void setPf_pay_type(Integer pf_pay_type) {
		this.pf_pay_type = pf_pay_type;
	}
	public String getPlate_number() {
		return plate_number;
	}
	public void setPlate_number(String plate_number) {
		this.plate_number = plate_number;
	}
	
	
	
	
}
