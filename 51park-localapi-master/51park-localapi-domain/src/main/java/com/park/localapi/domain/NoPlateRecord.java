/**
 * 
 */
package com.park.localapi.domain;

/**
 * @author fangct 
 * created on 2017年12月15日
 */
public class NoPlateRecord {

	private int id;
	private int park_id;
	private String park_name;
	private String en_channel_id;
	private String ex_channel_id;
	private String temp_plate;
	private String enter_time;
	private String exit_time;
	private String openid;
	private int status;//0: 待进场，1: 已进场，2: 待支付，3: 待离场，4: 已离场
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPark_id() {
		return park_id;
	}
	public void setPark_id(int park_id) {
		this.park_id = park_id;
	}
	public String getTemp_plate() {
		return temp_plate;
	}
	public void setTemp_plate(String temp_plate) {
		this.temp_plate = temp_plate;
	}
	public String getEnter_time() {
		return enter_time;
	}
	public void setEnter_time(String enter_time) {
		this.enter_time = enter_time;
	}
	public String getExit_time() {
		return exit_time;
	}
	public void setExit_time(String exit_time) {
		this.exit_time = exit_time;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPark_name() {
		return park_name;
	}
	public void setPark_name(String park_name) {
		this.park_name = park_name;
	}
	
	public String getEn_channel_id() {
		return en_channel_id;
	}
	public void setEn_channel_id(String en_channel_id) {
		this.en_channel_id = en_channel_id;
	}
	public String getEx_channel_id() {
		return ex_channel_id;
	}
	public void setEx_channel_id(String ex_channel_id) {
		this.ex_channel_id = ex_channel_id;
	}
	@Override
	public String toString() {
		return "NoPlateRecord [id=" + id + ", park_id=" + park_id + ", park_name=" + park_name + ", en_channel_id="
				+ en_channel_id + ", ex_channel_id=" + ex_channel_id + ", temp_plate=" + temp_plate + ", enter_time="
				+ enter_time + ", exit_time=" + exit_time + ", openid=" + openid + ", status=" + status + "]";
	}
	
}
