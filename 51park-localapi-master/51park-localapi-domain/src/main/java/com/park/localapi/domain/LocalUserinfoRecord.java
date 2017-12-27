package com.park.localapi.domain;

import java.io.Serializable;

import com.park.localapi.domain.mybatis.Column;
import com.park.localapi.domain.mybatis.Index;
import com.park.localapi.domain.mybatis.Table;

/**
 * 本地用户操作记录数据表实体
 * @author fangct
 *
 */
@Table("cp_park_localuser_info_record")
public class LocalUserinfoRecord  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Index("id")
	private int id;//主健自增长
	@Column("park_id")
	private int park_id;//停车场ID，对应cp_park(id)
	@Column("user_account")
	private String user_account;//用户帐号
	@Column("user_password")
	private String user_password;//用户密码
	@Column("user_name")
	private String user_name;//用户名称
	@Column("user_type")
	private int user_type;//用户类型 0：停车场收费人员 1：其它（供扩展使用）
	@Column("optype")
	private int optype;//操作类型 0：增加 1：删除 2：修改 3: 生成动态密码
	@Column("optime")
	private int optime;//操作时间 时间戳
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
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getUser_type() {
		return user_type;
	}
	public void setUser_type(int user_type) {
		this.user_type = user_type;
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
