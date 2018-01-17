package com.yqh.bsp.thirdparty.weixin.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yqh.bsp.thirdparty.weixin.kit.ParaMap;
import com.yqh.bsp.utils.HttpUtil;

public class AccessToken {
	private static final Logger logger = LoggerFactory.getLogger(AccessToken.class);
	
	public static Map<String, String> MSGTOKEN = new ConcurrentHashMap<String, String>();
    private static String refresh_url = "https://api.weixin.qq.com/cgi-bin/token";
	
	public static String getSnsAccessToken(String openId) {
		Record record = Db.findFirst("select * from wx_access_token where openid=?", openId);
		if (record == null) {
			return "";
		} else {
			return record.getStr("access_token");
		}
	}
	/**
	 * 获取全局accesstoken
	 * @return
	 */
	public static String refreshAccessToken() {
		final Map<String, String> queryParas = ParaMap.create("appid", ApiConfig.appId).put("secret", ApiConfig.appSecret).put("grant_type", "client_credential").getData();
		String lastTime = MSGTOKEN.get("createTime");
		String accessToken = "";
		if (StrKit.notBlank(lastTime)) {
			if (System.currentTimeMillis() < (Long.parseLong(lastTime) + Integer.parseInt(MSGTOKEN.get("expiresIn")) - 5)) {
				accessToken = MSGTOKEN.get("accessToken");
				return accessToken;
			} 
		}
		String json = HttpUtil.getWithParamMap(refresh_url, queryParas);
		JSONObject jobj = JSONObject.parseObject(json);
		accessToken = jobj.getString("access_token");
		if (null != jobj) {
			try {
				MSGTOKEN.put("accessToken", jobj.getString("access_token"));
				MSGTOKEN.put("expiresIn", jobj.getString("expires_in"));
				MSGTOKEN.put("createTime", System.currentTimeMillis() + "");
			} catch (Exception e) {
				logger.error("获取token失败 errcode:{} errmsg:{}", jobj.getInteger("errcode"), jobj.getString("errmsg"));
			}
		}
		return accessToken;

	}
    
    public static void main(String[] args) {
    	String json = AccessToken.refreshAccessToken();
 		System.out.println(json);
	}

}
