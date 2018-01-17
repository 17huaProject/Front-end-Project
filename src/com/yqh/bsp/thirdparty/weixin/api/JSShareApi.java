package com.yqh.bsp.thirdparty.weixin.api;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.yqh.bsp.utils.HttpUtil;

public class JSShareApi {

	private static String TokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + ApiConfig.appId + "&secret=" + ApiConfig.appSecret;
	private static String TicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	private static final Logger logger = LoggerFactory.getLogger(JSShareApi.class);

	/**
	 * 获取jsapi相关参数map
	 * 
	 * @param jsapi_ticket
	 * @param url
	 * @return
	 */
	public static JSONObject getJsSignJson(String jsapi_ticket, String url) {

		String nonce_str = UUID.randomUUID().toString();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String s = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		String signature = sha1(s);

		JSONObject json = new JSONObject();
		json.put("appId", ApiConfig.appId);
//		json.put("jsapi_ticket", jsapi_ticket);
		json.put("nonceStr", nonce_str);
		json.put("timestamp", timestamp);
		json.put("signature", signature);

		return json;
	}

	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static String getAccessToken() {
		JSONObject jsonObject = null;
		String accessToken = "";
		String back = CacheKit.get("cache1hour", "js_access_token");
		if (StrKit.isBlank(back)) {
			back = HttpUtil.httpsRequest(TokenUrl, "GET", null);
			CacheKit.put("cache1hour", "js_access_token", back);
		}
		jsonObject = JSONObject.parseObject(back);
		// 如果请求成功
		String back1 = CacheKit.get("cache1hour", "js_access_token");
		if (null != jsonObject) {
			try {
				accessToken = jsonObject.getString("access_token");
				jsonObject.getIntValue("expires_in");
			} catch (JSONException e) {
				// 获取token失败
				logger.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	public static String getJsApiTicket() {
		String accessToken = getAccessToken();
		String jsTicketUrl = TicketUrl.replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject = null;
		String jsApiTicket = "";
		String back = CacheKit.get("cache1hour", "js_ticket");
		if (StrKit.isBlank(back)) {
			back = HttpUtil.httpsRequest(jsTicketUrl, "GET", null);
			CacheKit.put("cache1hour", "js_ticket", back);
		}
		jsonObject = JSONObject.parseObject(back);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				jsApiTicket = jsonObject.getString("ticket");
				jsonObject.getIntValue("expires_in");
			} catch (JSONException e) {
				accessToken = null;
				// 获取jsApiTicket失败
				logger.error("获取jsApiTicket失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return jsApiTicket;
	}

	private static String sha1(String str) {
		String enStr = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(str.getBytes("UTF-8"));
			enStr = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return enStr;

	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	public static void main(String[] args) {
		String url = "http://cyg.oil-z.com/share";
		String jsapi_ticket = JSShareApi.getJsApiTicket();
		JSONObject obj = JSShareApi.getJsSignJson(jsapi_ticket, url);
	}

}
