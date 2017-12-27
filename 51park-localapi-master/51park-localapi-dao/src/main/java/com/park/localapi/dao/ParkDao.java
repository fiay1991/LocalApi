package com.park.localapi.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.park.localapi.dao.mybatis.BaseDao;
import com.park.localapi.domain.Park;
import com.park.localapi.domain.vo.ReconciliationVO;

@Repository(value="parkDao")
public interface ParkDao extends BaseDao<Park, Serializable>{

	/**
	 * 根据车场code查询车场的key
	 * @param parkcode
	 * @return
	 */
	public Map<String, String> getParkKey(String parkid);
	public String getParkIdByCode(String parkcode);
	public String getParkCodeById(String parkid);
	public ArrayList<Integer> findAllParkId();
	public void addReconciliation(Map<String, String> rowMap);
	public List<ReconciliationVO> findBillList(Map<String, String> findBillMap);
	public void updateReconciliationInfo(ReconciliationVO reconciliationVO);
	public void addFinalReconciliation(Map<String, String> headMap);
	public void addBillUrl(Map<String, String> billUrlMap);
	public String findBillPath(Map<String, String> csvFilePathMap);
	public void updateFinalReconciliation(Map<String, String> headMap);
	public void delReconciliationByTradeno(String trade_no);
	public void addUnusualOnOff(Map<String, String> parmasMap);
	public void addCheckOut(Map<String, String> parmasMap);
	public int checkEquipmentInfo(Map<String, String> checkEquipmentInfoMap);
	public void addEquipmentInfo(Map<String, String> addEquipmentInfoMap);
	public void delEquipmentInfo(Map<String, String> delEquipmentInfoMap);
	public void updateEquipmentInfo(Map<String, String> updateEquipmentInfoMap);
	public void updateEquipmentStatus(Map<String, String> updateEquipmentStatusMap);
	public Map<String, Object> findAuthorizeById(String id);
	public Map<String, Object> findChargingRoleById(String id);
	public void updateFinalReconciliationResult(Map<String, String> updateFinalReconciliationResultMap);
	public void updateBillUrl(Map<String, String> billUrlMap);
	public void addReconciliationInfo(ReconciliationVO reconciliationVO);
	public Map<String, String> findEquipmentInfo(Map<String, String> checkEquipmentInfoMap);
	public void addEquipmentAlert(Map<String, String> addEquipmentAlertMap);
	public String findPlate(String order_num);
	public void delReconciliationInfo(Map<String, String> headMap);
}
