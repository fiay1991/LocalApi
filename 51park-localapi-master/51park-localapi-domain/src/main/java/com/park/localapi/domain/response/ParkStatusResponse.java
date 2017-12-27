package com.park.localapi.domain.response;

public class ParkStatusResponse {
	
	private String parkid;//停车场ID, 由平台统一分配，在本地系统上配置
	private int time;//平台服务器系统时间，采用时间戳（秒）
	
	public String getParkid() {
		return parkid;
	}
	public void setParkid(String parkid) {
		this.parkid = parkid;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "ParkStatusResponse [parkid=" + parkid + ", time=" + time + "]";
	}
	
	

}
