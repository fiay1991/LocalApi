package com.park.localapi.common.constants;

import java.util.HashMap;
import java.util.Map;

public class SignKeyConstants {
	/**
	 * 所有对接项目的名称以及对应的私钥
	 */
	public static Map< String, String>  keyMap =new HashMap<String, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put("WYTC","ed3c8e7159665fe719945ad8427b92a7");
			put("LocalApi","3795165b559c865cb6cdb1a8b341b58c");
			

			
		}
	};
}
