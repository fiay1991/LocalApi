package com.park.localapi.domain;

import java.io.Serializable;

import com.park.localapi.domain.mybatis.Column;
import com.park.localapi.domain.mybatis.Index;
import com.park.localapi.domain.mybatis.Table;

/**
 * 车场信息表
 * @author fangct
 *
 */
@Table("cp_park")
public class Park  implements Serializable {
	private static final long serialVersionUID = 1L;
	@Index("cp_park_id")
	private int cp_park_id;
	@Column("name")
	private String name;
	@Column("mall_id")
	private int mall_id;
	@Column("mall_name")
	private String mall_name;
	@Column("park_count")
	private int park_count;
	@Column("free_count")
	private int free_count;
	
	public int getCp_park_id() {
		return cp_park_id;
	}
	public void setCp_park_id(int cp_park_id) {
		this.cp_park_id = cp_park_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMall_id() {
		return mall_id;
	}
	public void setMall_id(int mall_id) {
		this.mall_id = mall_id;
	}
	public String getMall_name() {
		return mall_name;
	}
	public void setMall_name(String mall_name) {
		this.mall_name = mall_name;
	}
	public int getPark_count() {
		return park_count;
	}
	public void setPark_count(int park_count) {
		this.park_count = park_count;
	}
	public int getFree_count() {
		return free_count;
	}
	public void setFree_count(int free_count) {
		this.free_count = free_count;
	}
}
