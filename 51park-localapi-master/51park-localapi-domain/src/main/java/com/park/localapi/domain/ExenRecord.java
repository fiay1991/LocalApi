package com.park.localapi.domain;

public class ExenRecord {
	   private int id;//'自增长ID',
	   private int record_type;//'记录类型（1:进场;2:出场）',
	   private String order_num;//'订单编号 账单ID(命名规则：年月日两位-时分秒-随机数（5位） 13041116580912345) 与在场订单信息表信息一致',
	   private String card_id;//'会员卡ID号',
	   private int park_id; //'停车场ID，对应cp_park(id)',
	   private int device_type;//'车场设备类型0:刷卡1:车牌识别2：车牌识别和刷卡组合',
	   private int enter_time;//'车辆入场时间',
	   private int exit_time;//'车辆出场时间',
	   private String plate_number;//'车牌号',
	   private String carbrand;//'车辆品牌 0:其它 1:大众 2:别克 3:宝马 4:本田 5:标致 6:丰田 7:福特 8:日产 9:奥迪 10:马自达 11:雪佛兰 12:雪铁龙 13:现代 14:奇瑞',
	   private String carcolor; //'车辆颜色 0：未知、1：蓝色、2：黄色、3：白色、4：黑色',
	   private String car_desc;//'若车辆类型是特殊车辆，此是对此车辆的描述信息，例如：垃圾车、军警车、医务车',
	   private String enter_no;//'入场设备号',
	   private String exit_no;//'出场设备号',
	   private String image;//'进出场图片地址',
	   private String user_account;//'本地收费员账号',
	   private int create_time;//'创建时间',
	   private int update_time;//'更新时间',
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRecord_type() {
		return record_type;
	}
	public void setRecord_type(int record_type) {
		this.record_type = record_type;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public int getPark_id() {
		return park_id;
	}
	public void setPark_id(int park_id) {
		this.park_id = park_id;
	}
	public int getDevice_type() {
		return device_type;
	}
	public void setDevice_type(int device_type) {
		this.device_type = device_type;
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
	public String getPlate_number() {
		return plate_number;
	}
	public void setPlate_number(String plate_number) {
		this.plate_number = plate_number;
	}
	public String getCarbrand() {
		return carbrand;
	}
	public void setCarbrand(String carbrand) {
		this.carbrand = carbrand;
	}
	public String getCarcolor() {
		return carcolor;
	}
	public void setCarcolor(String carcolor) {
		this.carcolor = carcolor;
	}
	public String getCar_desc() {
		return car_desc;
	}
	public void setCar_desc(String car_desc) {
		this.car_desc = car_desc;
	}
	public String getEnter_no() {
		return enter_no;
	}
	public void setEnter_no(String enter_no) {
		this.enter_no = enter_no;
	}
	public String getExit_no() {
		return exit_no;
	}
	public void setExit_no(String exit_no) {
		this.exit_no = exit_no;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String user_account) {
		this.user_account = user_account;
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
	@Override
	public String toString() {
		return "ExenRecord [id=" + id + ", record_type=" + record_type + ", order_num=" + order_num + ", card_id="
				+ card_id + ", park_id=" + park_id + ", device_type=" + device_type + ", enter_time=" + enter_time
				+ ", exit_time=" + exit_time + ", plate_number=" + plate_number + ", carbrand=" + carbrand
				+ ", carcolor=" + carcolor + ", car_desc=" + car_desc + ", enter_no=" + enter_no + ", exit_no="
				+ exit_no + ", image=" + image + ", user_account=" + user_account + ", create_time=" + create_time
				+ ", update_time=" + update_time + "]";
	}
	   
	   

}
