package com.park.localapi.domain.request;
/**
 * 本地进场请求参数对象
 * EnterRequest
 */
public class EnterRequest {
	
	private int parkid;//停车场ID
	private String license_plate;//车牌号,车牌号为空或无牌车的按无牌车编码规则上报。如停车场ID+进场年月日时间秒，如：1234520170118153021
	private String entername;//入口名称
	private int time;//进场时间	单位为秒
	private int type;//车辆类型，0：临时车；1：固定车 ；2：错时车 ； 3：预定车 ； 4：特殊车辆
	private int cartype;//车型，(1.小型车;2.大型车)，默认为1
	private String local_orderid;//本地订单号
	private String ticket_id;//纸票ID编号
	private String enterimagename;//车辆入场图片文件路径 格式为：parkid\年月\日\164041_In_0_京A00003_5447.jpg
	private String vehicledesc;//若车辆类型是特殊车辆，此是对此车辆的描述例如：垃圾车、军警车、医务车；若是VIP车辆，此是对Vip车辆的描述例如员工车、贵宾卡、红钻卡等
	
	public EnterRequest(int parkid, String license_plate, String entername, int time, int type, int cartype,
			String local_orderid, String ticket_id, String enterimagename, String vehicledesc) {
		super();
		this.parkid = parkid;
		this.license_plate = license_plate;
		this.entername = entername;
		this.time = time;
		this.type = type;
		this.cartype = cartype;
		this.local_orderid = local_orderid;
		this.ticket_id = ticket_id;
		this.enterimagename = enterimagename;
		this.vehicledesc = vehicledesc;
	}
	
	public int getParkid() {
		return parkid;
	}
	public void setParkid(int parkid) {
		this.parkid = parkid;
	}
	public String getLicense_plate() {
		return license_plate;
	}
	public void setLicense_plate(String license_plate) {
		this.license_plate = license_plate;
	}
	public String getEntername() {
		return entername;
	}
	public void setEntername(String entername) {
		this.entername = entername;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCartype() {
		return cartype;
	}
	public void setCartype(int cartype) {
		this.cartype = cartype;
	}
	public String getLocal_orderid() {
		return local_orderid;
	}
	public void setLocal_orderid(String local_orderid) {
		this.local_orderid = local_orderid;
	}
	public String getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(String ticket_id) {
		this.ticket_id = ticket_id;
	}
	public String getEnterimagename() {
		return enterimagename;
	}
	public void setEnterimagename(String enterimagename) {
		this.enterimagename = enterimagename;
	}
	public String getVehicledesc() {
		return vehicledesc;
	}
	public void setVehicledesc(String vehicledesc) {
		this.vehicledesc = vehicledesc;
	}
	
	@Override
	public String toString() {
		return "EnterRequest [parkid=" + parkid + ", license_plate=" + license_plate + ", entername=" + entername
				+ ", time=" + time + ", type=" + type + ", cartype=" + cartype + ", local_orderid=" + local_orderid
				+ ", ticket_id=" + ticket_id + ", enterimagename=" + enterimagename + ", vehicledesc=" + vehicledesc
				+ "]";
	}
	
	

}
