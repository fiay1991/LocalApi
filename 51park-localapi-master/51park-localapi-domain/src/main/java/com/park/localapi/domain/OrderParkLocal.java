package com.park.localapi.domain;

import java.math.BigDecimal;

public class OrderParkLocal {
	
	private int id;//主键
	private String order_num; //订单号
	private String local_order_id;// '本地订单号'
	private int cp_park_id;//'停车场ID'
	private String car_number;//'车牌号本地设备识别(白牌为“未识别”)'
	private String local_card_id;//'本地读卡设备读取/无忧卡卡号'
	private int card_type;//'卡类型：1:无忧月卡 2:无忧次卡 3:无忧临时卡 4：无忧错时卡 10:本地临时卡 11:本地月卡 99：特殊车辆'
	private String entrance_id;//'入口'
	private String exit_id;//'出口'
	private int entrance_time;//'进场时间'
	private int exit_time;//'出场时间'
	private BigDecimal amount;// '应收金额'
	private BigDecimal realamount;//'实收金额'
	private int pay_flag;//'支付标志0：现金支付1：无忧在线支付3：第三方收取'
	private String local_cashier_id;//'本地收费员ID'
	private String local_cashier_name;//'本地收费员名称'
	private int created_time;//'创建时间'
	private int car_type;//'车型，(1.小型车;2.大型车)，默认为1'
	private int car_stop_type;//'车辆类型，0：临时车；1：固定车; 2 : 错时车; 3 : 预定车; 4 : 特殊车辆'
	private String car_desc;//'若车辆类型是特殊车辆，此是对此车辆的此是对特殊车辆的描述 例如：垃圾车、军警车、医务车；若是VIP车辆，此是对Vip车辆的描述例如员工车、贵宾卡、红钻卡等。'
	private int updated_time;//'更新时间'
	private BigDecimal discount_amount;//'本地折扣金额'
	private String cardid;//'纸票id'
	
	public OrderParkLocal(int id, String order_num, String local_order_id, int cp_park_id, String car_number,
			String local_card_id, int card_type, String entrance_id, String exit_id, int entrance_time, int exit_time,
			BigDecimal amount, BigDecimal realamount, int pay_flag, String local_cashier_id, String local_cashier_name,
			int created_time, int car_type, int car_stop_type, String car_desc, int updated_time,
			BigDecimal discount_amount, String cardid) {
		super();
		this.id = id;
		this.order_num = order_num;
		this.local_order_id = local_order_id;
		this.cp_park_id = cp_park_id;
		this.car_number = car_number;
		this.local_card_id = local_card_id;
		this.card_type = card_type;
		this.entrance_id = entrance_id;
		this.exit_id = exit_id;
		this.entrance_time = entrance_time;
		this.exit_time = exit_time;
		this.amount = amount;
		this.realamount = realamount;
		this.pay_flag = pay_flag;
		this.local_cashier_id = local_cashier_id;
		this.local_cashier_name = local_cashier_name;
		this.created_time = created_time;
		this.car_type = car_type;
		this.car_stop_type = car_stop_type;
		this.car_desc = car_desc;
		this.updated_time = updated_time;
		this.discount_amount = discount_amount;
		this.cardid = cardid;
	}
	
	
	
	public OrderParkLocal() {
		super();
	}



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
	public String getLocal_order_id() {
		return local_order_id;
	}
	public void setLocal_order_id(String local_order_id) {
		this.local_order_id = local_order_id;
	}
	public int getCp_park_id() {
		return cp_park_id;
	}
	public void setCp_park_id(int cp_park_id) {
		this.cp_park_id = cp_park_id;
	}
	public String getCar_number() {
		return car_number;
	}
	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}
	public String getLocal_card_id() {
		return local_card_id;
	}
	public void setLocal_card_id(String local_card_id) {
		this.local_card_id = local_card_id;
	}
	public int getCard_type() {
		return card_type;
	}
	public void setCard_type(int card_type) {
		this.card_type = card_type;
	}
	public String getEntrance_id() {
		return entrance_id;
	}
	public void setEntrance_id(String entrance_id) {
		this.entrance_id = entrance_id;
	}
	public String getExit_id() {
		return exit_id;
	}
	public void setExit_id(String exit_id) {
		this.exit_id = exit_id;
	}
	public int getEntrance_time() {
		return entrance_time;
	}
	public void setEntrance_time(int entrance_time) {
		this.entrance_time = entrance_time;
	}
	public int getExit_time() {
		return exit_time;
	}
	public void setExit_time(int exit_time) {
		this.exit_time = exit_time;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getRealamount() {
		return realamount;
	}
	public void setRealamount(BigDecimal realamount) {
		this.realamount = realamount;
	}
	public int getPay_flag() {
		return pay_flag;
	}
	public void setPay_flag(int pay_flag) {
		this.pay_flag = pay_flag;
	}
	public String getLocal_cashier_id() {
		return local_cashier_id;
	}
	public void setLocal_cashier_id(String local_cashier_id) {
		this.local_cashier_id = local_cashier_id;
	}
	public String getLocal_cashier_name() {
		return local_cashier_name;
	}
	public void setLocal_cashier_name(String local_cashier_name) {
		this.local_cashier_name = local_cashier_name;
	}
	public int getCreated_time() {
		return created_time;
	}
	public void setCreated_time(int created_time) {
		this.created_time = created_time;
	}
	public int getCar_type() {
		return car_type;
	}
	public void setCar_type(int car_type) {
		this.car_type = car_type;
	}
	public int getCar_stop_type() {
		return car_stop_type;
	}
	public void setCar_stop_type(int car_stop_type) {
		this.car_stop_type = car_stop_type;
	}
	public String getCar_desc() {
		return car_desc;
	}
	public void setCar_desc(String car_desc) {
		this.car_desc = car_desc;
	}
	public int getUpdated_time() {
		return updated_time;
	}
	public void setUpdated_time(int updated_time) {
		this.updated_time = updated_time;
	}
	public BigDecimal getDiscount_amount() {
		return discount_amount;
	}
	public void setDiscount_amount(BigDecimal discount_amount) {
		this.discount_amount = discount_amount;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	
	@Override
	public String toString() {
		return "OrderParkLocal [id=" + id + ", order_num=" + order_num + ", local_order_id=" + local_order_id
				+ ", cp_park_id=" + cp_park_id + ", car_number=" + car_number + ", local_card_id=" + local_card_id
				+ ", card_type=" + card_type + ", entrance_id=" + entrance_id + ", exit_id=" + exit_id
				+ ", entrance_time=" + entrance_time + ", exit_time=" + exit_time + ", amount=" + amount
				+ ", realamount=" + realamount + ", pay_flag=" + pay_flag + ", local_cashier_id=" + local_cashier_id
				+ ", local_cashier_name=" + local_cashier_name + ", created_time=" + created_time + ", car_type="
				+ car_type + ", car_stop_type=" + car_stop_type + ", car_desc=" + car_desc + ", updated_time="
				+ updated_time + ", discount_amount=" + discount_amount + ", cardid=" + cardid + "]";
	}
	
	

}
