/**
 * 
 */
package com.park.localapi.common.constants;

/**
 * @author fangct 
 * created on 2017年11月13日
 */
public class TimeOutConstants {

	/**
	 * 从REDIS读取缴费查询数据的超时时间，单位秒
	 */
	public static final int REDIS_BILLINFO = 2;
	/**
	 * 从REDIS读取远程开关闸返回数据的超时时间，单位秒
	 */
	public static final int REDIS_REMOTEONOFF = 5;
	/**
	 * 本地失联超时上限，单位秒
	 */
	public static final int OFF_LINE_TIME = 6*60;
	
}
