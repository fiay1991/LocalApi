package com.park.localapi.dao;

import org.springframework.stereotype.Repository;
import com.park.localapi.domain.NoPlateRecord;

@Repository(value="noPlateRecordDao")
public interface NoPlateRecordDao {
	
	/**
	 * 查询
	 * @param id
	 * @return
	 */
	public NoPlateRecord select(int id);

}
