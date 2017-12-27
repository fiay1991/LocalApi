package com.park.localapi.domain.vo;

/**
 *  月卡信息实体
 * @author fangct
 *
 */
public class MonthcardVO {

	private String cardid;//云端月卡ID号
	private String plate;//车牌号或卡号
	private String phone;//手机号
	private String name;//用户姓名/公司名称
	private int property;//申办性质 1 个人  2 公司
	private String startdate;//月卡开始日期
	private String enddate;//月卡结束日期
	private int type;//月卡类型
	private String starttime;//开始时间
	private String endtime;//结束时间
	private int action;//操作类型
	private int plotcount;//车位数量
	private double paymoney;//金额
	private String useraccount;//管理员用户账号
	private int updatetime;//办理时间
	private String region;//所属场库
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public int getPlotcount() {
		return plotcount;
	}
	public void setPlotcount(int plotcount) {
		this.plotcount = plotcount;
	}
	public double getPaymoney() {
		return paymoney;
	}
	public void setPaymoney(double paymoney) {
		this.paymoney = paymoney;
	}
	public String getUseraccount() {
		return useraccount;
	}
	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}
	public int getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(int updatetime) {
		this.updatetime = updatetime;
	}
	public int getProperty() {
		return property;
	}
	public void setProperty(int property) {
		this.property = property;
	}
}
