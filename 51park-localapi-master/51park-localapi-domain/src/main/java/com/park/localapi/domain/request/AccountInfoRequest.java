package com.park.localapi.domain.request;

/**
 * 本地用户操作记录下发请求参数
 * @author fangct
 *
 */
public class AccountInfoRequest {
	
	private String parkid;//停车场code
	private String useraccount;//用户帐号
	private String userpassword;//用户密码
	private String username;//用户名称
	private int usertype;//用户类型 0：停车场收费人员 1：其它（供扩展使用）
	private int optype;//操作类型 0：增加 1：删除 2：修改 
	private int optime;//操作时间 时间戳
	public String getParkid() {
		return parkid;
	}
	public void setParkid(String parkid) {
		this.parkid = parkid;
	}
	public String getUseraccount() {
		return useraccount;
	}
	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}
	public String getUserpassword() {
		return userpassword;
	}
	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	public int getOptype() {
		return optype;
	}
	public void setOptype(int optype) {
		this.optype = optype;
	}
	public int getOptime() {
		return optime;
	}
	public void setOptime(int optime) {
		this.optime = optime;
	}

}
