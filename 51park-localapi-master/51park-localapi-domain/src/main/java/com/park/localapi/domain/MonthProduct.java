package com.park.localapi.domain;

/**
 * 月卡产品数据表实体
 * @author fangct
 *
 */
public class MonthProduct  {
	
	private int id;//主键
	private int park_id;//车场ID
	private String name;//产品名称
	private String duration;//产品时长
	private int type;//产品类型 0 全天卡 1 分时段卡
	private String starttime;//开始时间 格式为 hh:mm:ss
	private String endtime;//结束时间  格式为 hh:mm:ss 注：若结束时间小于开始时间，则表示该时间为次日时间
	private String region;//所属场库
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
}
