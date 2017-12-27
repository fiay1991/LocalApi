package com.park.localapi.domain;

import java.io.Serializable;

import com.park.localapi.domain.mybatis.Column;
import com.park.localapi.domain.mybatis.Index;
import com.park.localapi.domain.mybatis.Table;

/**
 * 月卡操作记录数据表实体
 * @author fangct
 *
 */
@Table("cp_month_operation_record")
public class MonthOperRecord  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Index("id")
	private int id;//主键
	@Column("month_info_id")
	private int month_info_id;//月卡信息ID
	@Column("park_id")
	private int park_id;//车场ID
	@Column("property")
	private int property;//申办性质 1 个人  2 公司
	@Column("card_owner")
	private String card_owner;//月卡所属人名称/公司名称
	@Column("plate_num")
	private String plate_num;//车牌号 多个中间用英文顿号隔开
	@Column("phone")
	private String phone;//	手机号码
	@Column("month_product_id")
	private int month_product_id;//产品id
	@Column("operation_type")
	private int operation_type;//操作类型  1 开卡 2 续费 3 退费 4 编辑
	@Column("is_overdue")
	private int is_overdue;//是否逾期  仅当操作类型为2时有效 1 是 0 否 
	@Column("examine_status")
	private int examine_status;//审核状态 仅当操作类型为2时有效 1 未审核 2 审核通过 3 审核未通过 
	@Column("carnum")
	private int carnum;//车位数
	@Column("count")
	private int count;//购买数量
	@Column("payamount")
	private double payamount;//支付金额
	@Column("pay_method")
	private int pay_method;//支付方式
	@Column("refund")
	private double	 refund;//退卡金额
	@Column("startdate")
	private String 	startdate;//月卡有效开始日期
	@Column("enddate")
	private String enddate;//月卡有效结束日期
	@Column("operator")
	private String operator;//操作人
	@Column("create_time")
	private String create_time;//	创建时间
	@Column("remark")
	private String remark;//备注
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMonth_info_id() {
		return month_info_id;
	}
	public void setMonth_info_id(int month_info_id) {
		this.month_info_id = month_info_id;
	}
	public int getPark_id() {
		return park_id;
	}
	public void setPark_id(int park_id) {
		this.park_id = park_id;
	}
	public String getCard_owner() {
		return card_owner;
	}
	public void setCard_owner(String card_owner) {
		this.card_owner = card_owner;
	}
	public String getPlate_num() {
		return plate_num;
	}
	public void setPlate_num(String plate_num) {
		this.plate_num = plate_num;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getMonth_product_id() {
		return month_product_id;
	}
	public void setMonth_product_id(int month_product_id) {
		this.month_product_id = month_product_id;
	}
	public int getOperation_type() {
		return operation_type;
	}
	public void setOperation_type(int operation_type) {
		this.operation_type = operation_type;
	}
	public int getIs_overdue() {
		return is_overdue;
	}
	public void setIs_overdue(int is_overdue) {
		this.is_overdue = is_overdue;
	}
	public int getExamine_status() {
		return examine_status;
	}
	public void setExamine_status(int examine_status) {
		this.examine_status = examine_status;
	}
	public int getCarnum() {
		return carnum;
	}
	public void setCarnum(int carnum) {
		this.carnum = carnum;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getPayamount() {
		return payamount;
	}
	public void setPayamount(double payamount) {
		this.payamount = payamount;
	}
	public double getRefund() {
		return refund;
	}
	public int getPay_method() {
		return pay_method;
	}
	public void setPay_method(int pay_method) {
		this.pay_method = pay_method;
	}
	public void setRefund(double refund) {
		this.refund = refund;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getProperty() {
		return property;
	}
	public void setProperty(int property) {
		this.property = property;
	}
	
}
