/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.yqh.bsp.thirdparty.weixin.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yqh.bsp.thirdparty.weixin.kit.ParaMap;
import com.yqh.bsp.utils.HttpUtil;

/**
 * 用户管理 API
 * <pre>
 * https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&amp;openid=OPENID&amp;lang=zh_CN
 * </pre>
 */
public class UserApi {
	
	private static String getUserInfo = "https://api.weixin.qq.com/sns/userinfo";
	private static String getUserInfoGrobal = "https://api.weixin.qq.com/cgi-bin/user/info";
	private static String getFollowers = "https://api.weixin.qq.com/cgi-bin/user/get";
	private static String batchGetUserInfo = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=";

	/**
	 * 获取用户基本信息（包括UnionID机制）
	 * @param openId 普通用户的标识，对当前公众号唯一
	 * @return ApiResult
	 */
	public static JSONObject getUserInfo(String openId) {
		ParaMap pm = ParaMap.create("access_token", AccessToken.getSnsAccessToken(openId)).put("openid", openId).put("lang", "zh_CN");
		JSONObject json = JSONObject.parseObject(HttpUtil.getWithParamMap(getUserInfo, pm.getData()));
		return json;
	}
	/**
	 * 通过全局access_token获取用户信息
	 * @param openId
	 * @return
	 */
	public static JSONObject getUserInfoByGrobal(String openId) {
		ParaMap pm = ParaMap.create("access_token", AccessToken.refreshAccessToken()).put("openid", openId);
		JSONObject json = JSONObject.parseObject(HttpUtil.getWithParamMap(getUserInfoGrobal, pm.getData()));
		return json;
		
	}
	
	public static void main(String[] args) {
//		ParaMap pm = ParaMap.create("access_token", "SSnAIHwDwS75tXA0BV1Jh1RWaDf9a-MqAMiePlHf-Ka12ZxpK-znTdK7JrhTtK8OkdN8wyuwyE4o-lO_ocWaHthC_H2jsXXAXBQsNQ4yKhI").put("openid", "okTOZwGW9pPCU_yg-kZJDNW0P9ng").put("lang", "zh_CN");
//		JSONObject json = JSONObject.parseObject(HttpUtil.getWithParamMap(getUserInfo, pm.getData()));
		System.out.println(JSON.toJSONString(getUserInfoByGrobal("oR6oj1GdRjqXdwz3Y1i0MJWWgrFY")));
	}
	
}
