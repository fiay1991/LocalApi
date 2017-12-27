package com.park.localapi.domain;

public class MemLogin {

	private int uid;//'用户ID'
	private String username;//'用户名'
	private String password;//'用户的临时密码hash（加密方式：md5("uc_members的uid|uc_member的regdate")）例：$password=md5("$uid|$regdate");'
	private int type;//'用户类型(1,个人；2，企业;   3:冻结; 4:洗车店用户)'
	private int source;//'来源：0:未知，1: 安卓，2: iOS，3:网站，4：扫卡注册，5：百度'
	private int activate;//'1:未激活，2:已激活（用户登录后为激活状态）'
	private int status;//'状态：1:有效，2:已禁用'
	private int cp_park_id;//'首单使用的停车场ID'
	private String channel;//'0:非第三方渠道注册 1:百度注册'
	private String subchannel;//'子商户id'
	
	public MemLogin(int uid, String username, String password, int type, int source, int activate, int status,
			int cp_park_id, String channel, String subchannel) {
		super();
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.type = type;
		this.source = source;
		this.activate = activate;
		this.status = status;
		this.cp_park_id = cp_park_id;
		this.channel = channel;
		this.subchannel = subchannel;
	}
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getActivate() {
		return activate;
	}
	public void setActivate(int activate) {
		this.activate = activate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCp_park_id() {
		return cp_park_id;
	}
	public void setCp_park_id(int cp_park_id) {
		this.cp_park_id = cp_park_id;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getSubchannel() {
		return subchannel;
	}
	public void setSubchannel(String subchannel) {
		this.subchannel = subchannel;
	}
	
	@Override
	public String toString() {
		return "MemLogin [uid=" + uid + ", username=" + username + ", password=" + password + ", type=" + type
				+ ", source=" + source + ", activate=" + activate + ", status=" + status + ", cp_park_id=" + cp_park_id
				+ ", channel=" + channel + ", subchannel=" + subchannel + "]";
	}
	
}
