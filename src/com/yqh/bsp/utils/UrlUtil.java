package com.yqh.bsp.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlUtil {
	
	public static String urlDecode(String uri,String enc) {
		String deurl = "";
		try {
			deurl = URLDecoder.decode(uri,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deurl;
	}
	
	public static String urlEncode(String uri,String enc) {
		String enurl = "";
		try {
			enurl = URLEncoder.encode(uri,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return enurl;
	}

}
