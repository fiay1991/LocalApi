package com.park.localapi.service.factory;

import com.park.localapi.common.constants.SendType;
import com.park.localapi.service.SendService;
import com.park.localapi.service.Spring;
import com.park.localapi.service.impl.DiscountServiceImpl;
import com.park.localapi.service.impl.NoPlateEnterServiceImpl;
import com.park.localapi.service.impl.NoPlateExitServiceImpl;
import com.park.localapi.service.impl.PrepayServiceImpl;

/**
 * 
 * @author fangct
 *
 */
public class SendFactory {

	public static SendService createSendImpl(String businessCode){
		SendService service = null;
		if(businessCode.equals(SendType.优惠记录.getCode())){
			service = Spring.getBean(DiscountServiceImpl.class);
		}else if(businessCode.equals(SendType.预缴费.getCode())){
			service = Spring.getBean(PrepayServiceImpl.class);
		}else if(businessCode.equals(SendType.无牌车入场.getCode())){
			service = Spring.getBean(NoPlateEnterServiceImpl.class);
		}else if(businessCode.equals(SendType.无牌车出场.getCode())){
			service = Spring.getBean(NoPlateExitServiceImpl.class);
		}/*else if(businessCode.equals(SendType.授权下发.getCode())){
			service = Spring.getBean(clz);
		}else if(businessCode.equals(SendType.人员信息.getCode())){
			service = Spring.getBean(clz);
		}else if(businessCode.equals(SendType.密码下发.getCode())){
			service = Spring.getBean(clz);
		}else if(businessCode.equals(SendType.计费规则.getCode())){
			service = Spring.getBean(clz);
		}else if(businessCode.equals(SendType.月卡.getCode())){
			service = Spring.getBean(clz);
		}*/
		return service;
	}
}
