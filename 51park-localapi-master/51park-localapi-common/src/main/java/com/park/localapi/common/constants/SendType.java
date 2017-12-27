package com.park.localapi.common.constants;
/**
 * 下发业务对应的类型
 *
 */
public enum SendType {
	
	/**
     * 业务编码  
     * 1.预缴费 
     * 2.优惠记录 
     * 3.月卡 
     * 4.授权/黑名单下发 
     * 5.计费规则  
     * 6.人员信息 
     * 7. 密码下发
     * 8. 缴费查询
     * 9. 远程控制开关闸
     * 10.无牌车入场
     * 11.无牌车出场
     */
	预缴费("预缴费","1"), 
	优惠记录("优惠记录","2"),
	月卡("月卡", "3"),
	授权下发("授权/黑名单下发","4"),
	计费规则("计费规则","5"),
	人员信息("人员信息","6"),
	密码下发("密码下发","7"),
	缴费查询("缴费查询","8"),
	远程控制开关闸("远程控制开关闸","9"),
    无牌车入场("无牌车入场","10"),
    无牌车出场("无牌车出场","11");
	
	
	private final String content;
	private final String code;

	private SendType(String content, String code) {
		this.content = content;
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public String getCode() {
		return code;
	}

	public static String getName(String code) {
		for (SendType p : SendType.values()) {
			if (code.equals(p.getCode())) {
				return p.content;
			}
		}
		return null;
	}

	public static String getCode(String name) {
		for (SendType p : SendType.values()) {
			if (name.equals(p.getContent())) {
				return p.code;
			}
		}
		return "";
	}


}
