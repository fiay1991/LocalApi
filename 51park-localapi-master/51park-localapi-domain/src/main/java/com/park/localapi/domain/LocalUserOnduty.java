package com.park.localapi.domain;

import java.io.Serializable;

import com.park.localapi.domain.mybatis.Column;
import com.park.localapi.domain.mybatis.Index;
import com.park.localapi.domain.mybatis.Table;

/**
 * 收费员当班表
 * @author fangct
 *
 */
@Table("cp_park_localuser_onduty")
public class LocalUserOnduty  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Index("id")
	private int id;//主健自增长
	@Column("park_id")
	private int park_id;//停车场ID，对应cp_park(id)
	@Column("user_account")
	private String user_account;//用户帐号，对应cp_park_account_info(user_account)
	@Column("gatnames")
	private String gatnames;//出入口名称，有一个或多个出入口名称组成中间用,(英文逗号) 隔开
	@Column("optime")
	private int optime;//操作时间 时间戳
	@Column("create_time")
	private int create_time;//创建时间
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
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	public int getOptime() {
		return optime;
	}
	public void setOptime(int optime) {
		this.optime = optime;
	}
	public String getGatnames() {
		return gatnames;
	}
	public void setGatnames(String gatnames) {
		this.gatnames = gatnames;
	}
	public int getCreate_time() {
		return create_time;
	}
	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}

}
