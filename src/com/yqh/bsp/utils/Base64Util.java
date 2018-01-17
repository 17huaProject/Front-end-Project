package com.yqh.bsp.utils;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	/**
	 * @param str
	 * @return 编码后值
	 */
	public static String encode(String str) {
		if (str == null || str.length() == 0)
			return str;
		byte[] b = str.getBytes();
//		Base64 base64 = new Base64();
//		System.out.println(new String(base64.encode(b)));
		b = Base64.encodeBase64(b);
		String s = new String(b);
		return s;

	}

	/**
	 * @param pass
	 * @return 解码后值
	 * @throws IOException
	 */
	public static String decode(String encodeStr) {
		if (encodeStr == null || encodeStr.length() == 0)
			return encodeStr;
		byte[] b = encodeStr.getBytes();
		b = Base64.decodeBase64(b);
		String s = new String(b);
		return s;

	}

}