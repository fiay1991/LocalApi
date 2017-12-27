package com.park.localapi.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.park.localapi.domain.mybatis.Column;
import com.park.localapi.domain.mybatis.Index;

public class OrderInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@Index("id")
	private int id;
	@Column("order_num")
	private String order_num;
	@Column("cp_park_id")
	private int cp_park_id;
	@Column("plate_number")
	private String plate_number;
	@Column("card_id")
	private String card_id;
	@Column("ticket_id")
	private String ticket_id;
	@Column("uid")
	private int uid;
	@Column("car_type")
	private int car_type;
	@Column("enter_time")
	private int enter_time;
	@Column("exit_time")
	private int exit_time;
	@Column("cost_before")
	private BigDecimal cost_before;
	@Column("cost_after")
	private BigDecimal cost_after;
	@Column("discount_amount")
	private BigDecimal discount_amount;
	@Column("prepay_amount")
	private BigDecimal prepay_amount;
	@Column("prepay_time")
	private int prepay_time;
	@Column("ordertype")
	private int ordertype;
	@Column("card_type")
	private int card_type;
	@Column("pay_method")
	private int pay_method;
	@Column("service_status")
	private int service_status;
	@Column("pay_status")
	private int pay_status;
	@Column("create_time")
	private int create_time;
	@Column("update_time")
	private int update_time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public int getCp_park_id() {
		return cp_park_id;
	}
	public void setCp_park_id(int cp_park_id) {
		this.cp_park_id = cp_park_id;
	}
	public String getPlate_number() {
		return plate_number;
	}
	public void setPlate_number(String plate_number) {
		this.plate_number = plate_number;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(String ticket_id) {
		this.ticket_id = ticket_id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getCar_type() {
		return car_type;
	}
	public void setCar_type(int car_type) {
		this.car_type = car_type;
	}
	public int getEnter_time() {
		return enter_time;
	}
	public void setEnter_time(int enter_time) {
		this.enter_time = enter_time;
	}
	public int getExit_time() {
		return exit_time;
	}
	public void setExit_time(int exit_time) {
		this.exit_time = exit_time;
	}
	public BigDecimal getCost_before() {
		return cost_before;
	}
	public void setCost_before(BigDecimal cost_before) {
		this.cost_before = cost_before;
	}
	public BigDecimal getCost_after() {
		return cost_after;
	}
	public void setCost_after(BigDecimal cost_after) {
		this.cost_after = cost_after;
	}
	public BigDecimal getDiscount_amount() {
		return discount_amount;
	}
	public void setDiscount_amount(BigDecimal discount_amount) {
		this.discount_amount = discount_amount;
	}
	public BigDecimal getPrepay_amount() {
		return prepay_amount;
	}
	public void setPrepay_amount(BigDecimal prepay_amount) {
		this.prepay_amount = prepay_amount;
	}
	public int getPrepay_time() {
		return prepay_time;
	}
	public void setPrepay_time(int prepay_time) {
		this.prepay_time = prepay_time;
	}
	public int getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(int ordertype) {
		this.ordertype = ordertype;
	}
	public int getCard_type() {
		return card_type;
	}
	public void setCard_type(int card_type) {
		this.card_type = card_type;
	}
	public int getPay_method() {
		return pay_method;
	}
	public void setPay_method(int pay_method) {
		this.pay_method = pay_method;
	}
	public int getService_status() {
		return service_status;
	}
	public void setService_status(int service_status) {
		this.service_status = service_status;
	}
	public int getPay_status() {
		return pay_status;
	}
	public void setPay_status(int pay_status) {
		this.pay_status = pay_status;
	}
	public int getCreate_time() {
		return create_time;
	}
	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}
	public int getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}
	

}
