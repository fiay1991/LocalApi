package com.park.localapi.domain.request;

/**
 * 密码下发请求参数
 * @author fangct
 *
 */
public class PasswordissueRequest {
	
	private String parkid;//停车场code
	private String useraccount;//用户帐号
	private String userpassword;//用户密码
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
	public int getOptime() {
		return optime;
	}
	public void setOptime(int optime) {
		this.optime = optime;
	}

}
