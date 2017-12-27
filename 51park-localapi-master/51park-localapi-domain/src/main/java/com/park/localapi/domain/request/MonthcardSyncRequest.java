package com.park.localapi.domain.request;

import java.util.List;

import com.park.localapi.domain.vo.MonthcardVO;

/**
 * 月卡同步下发请求实体
 * @author fangct
 *
 */
public class MonthcardSyncRequest {
	
	private String parkid;//车场code
	private List<MonthcardVO> carddata;//下发多个月卡的集合
	public String getParkid() {
		return parkid;
	}
	public void setParkid(String parkid) {
		this.parkid = parkid;
	}
	public List<MonthcardVO> getCarddata() {
		return carddata;
	}
	public void setCarddata(List<MonthcardVO> carddata) {
		this.carddata = carddata;
	}
}
