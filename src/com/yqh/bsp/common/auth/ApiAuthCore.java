package com.yqh.bsp.common.auth;

import com.yqh.bsp.utils.MD5Util;

public class ApiAuthCore {
	/**
	 * 
	 * @param params
	 * @param pidKey
	 * @return
	 */
	public static boolean signVerify(String sign,String phone,String timestamp, String pidKey) {
	    
	    if (sign.equalsIgnoreCase(genSignValue(phone,timestamp, pidKey,sign))) {
	    	return true;
	    } else {
	    	return false;
	    }

	}
	
	public static String genSignValue(String phone,String timestamp, String pidKey,String sign) {
		StringBuilder basestring = new StringBuilder();
	    basestring.append(phone).append(timestamp).append(pidKey);
		return MD5Util.MD5Encode(basestring.toString(), "UTF-8");
	}

}
