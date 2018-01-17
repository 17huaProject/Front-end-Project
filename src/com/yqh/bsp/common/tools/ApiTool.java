package com.yqh.bsp.common.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yqh.bsp.utils.MD5Util;


public class ApiTool {
	public static boolean isValidRequest(String pkey,String act,String timestamp,String deviceid,String sign) {
		if ((MD5Util.encode(act+timestamp+deviceid+pkey)).equals(sign.toLowerCase())) {
	        return true;
	    } else {
	        return false;
	    }
	}

	
	
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 获取编码字符集
	 * @param request
	 * @param response
	 * @return String
	 */
	private static String getCharacterEncoding(HttpServletRequest request,
			HttpServletResponse response) {
		
		if(null == request || null == response) {
			return "gbk";
		}
		
		String enc = request.getCharacterEncoding();
		if(null == enc || "".equals(enc)) {
			enc = response.getCharacterEncoding();
		}
		
		if(null == enc || "".equals(enc)) {
			enc = "gbk";
		}
		
		return enc;
	}

}


//	function isLogined(&$request) {
//	    $sessid = $request['session_id'];
//	    if (isset($sessid)){
//	        es_session::set_sessid($sessid);
//	        $GLOBALS['user_info'] = es_session::get("user_info");
//	        if(empty($GLOBALS['user_info'])) {
//	            return false;
//	        }
//	    } else {
//	        return false;
//	    }
//	}

