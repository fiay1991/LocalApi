package com.park.localapi.common.constants;

import java.util.Map.Entry;

import com.park.base.common.constants.CodeConstants;

/**
 * 返回状态码
 * @author fangct 20170705
 *
 */
public class LocalCodeConstants extends CodeConstants {
	
	/**
	 * 请求重复
	 */
	public final static String ERROR_405 = "405";
	/**
	 * 下发本地失败
	 */
	public final static String ERROR_406 = "406";
	/**
	 * 写入数据库失败
	 */
	public final static String ERROR_407 = "407";
	/**
	 * 请求CloudCore失败
	 */
	public final static String ERROR_408 = "408";
	/**
	 * 操作失败
	 */
	public final static String ERROR_409 = "409";
	/**
	 * 不存在的账号
	 */
	public final static String ERROR_410 = "410";
	
	
	static{
		map.put(ERROR_405, "请求重复");
		map.put(ERROR_406, "下发本地失败");
		map.put(ERROR_407, "写入数据库失败");
		map.put(ERROR_408, "请求CloudCore失败");
		map.put(ERROR_409, "操作失败");
		map.put(ERROR_410, "不存在的账号");
	}
	public static String getName(String code) {
		for (Entry<String, String> entry : map.entrySet()) {
			if (code.equals(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}
}
