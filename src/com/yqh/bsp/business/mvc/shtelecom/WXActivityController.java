package com.yqh.bsp.business.mvc.shtelecom;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.thirdparty.weixin.api.SnsAccessTokenApi;
import com.yqh.bsp.utils.HttpUtil;
import com.yqh.bsp.utils.RegularUtil;

@Action(controllerKey="/shtelecom")
public class WXActivityController extends BaseController {
	private static WXActivityService wxActivityService = new WXActivityService();
//	public static String PREURL = "http://121.201.7.173:9201";
	public static String PREURL = "http://222.68.210.104:5001";
	//校验用户状态
	public void checkUser() {
		String code = getPara("code");
		if (StrKit.isBlank(code)) {
			returnJson(Errcode.FAIL, "缺少code参数");
		}
		try {
			String openId = SnsAccessTokenApi.getSHWXSnsOpenId(code);
			if (StrKit.isBlank(openId)) {
				returnJson(Errcode.FAIL, "获取openid失败");
			}
			String uri = "/api/weixin/%s";
			String url = WXActivityController.PREURL+String.format(uri, openId);
			String resp = HttpUtil.getWithParamString(url, "openId="+openId, "UTF-8");
			
			JSONObject record = new JSONObject();
			record.put("open_id", openId);
			
			JSONObject json = JSONObject.parseObject(resp);
			Boolean isBind = json.getBoolean("data");
			
			if (isBind) {
				record.put("is_binded", "1"); //已绑定	
			} else {
				record.put("is_binded", "0"); //未绑定
			}
			returnJson(record);
		} catch (Exception e) {
			returnJson(Errcode.FAIL, "获取openid失败");
		}
	}
	
	//绑定用户手机号和微信openid
	public void bindUser() {
		String uri = "/api/weixin/%s/phone/%s";
		String openId = getPara("open_id");
		String phone = getPara("phone");
		if(!RegularUtil.isPhoneNumber(phone)) {
			returnJson(Errcode.FAIL, "手机号");
			return;
		}
		
		String url = WXActivityController.PREURL+String.format(uri, openId,phone);
		Map<String,String> params = new HashMap<String, String>();
		params.put("openId", openId);
		params.put("phoneNumber", phone);
		try {
			String resp = HttpUtil.postWithParamMapNoState(url, params, "UTF-8");
			JSONObject json = JSONObject.parseObject(resp);
			if(json.getInteger("code") == 200) {
				returnJson(Errcode.SUCC, "绑定成功");
			} else {
				String msg = json.getJSONObject("data").getString("details");
				returnJson(Errcode.FAIL, msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnJson(Errcode.FAIL, "绑定失败");
		}	
	}
	
	public void getUserInfo() {
		String openId = getPara("open_id");
		String uri = "/api/weixin/actor/%s/rank2";
		String url = WXActivityController.PREURL+String.format(uri, openId);
		String resp;
		try {
			resp = HttpUtil.getWithParamString(url, "", "UTF-8");
			JSONObject record = new JSONObject();
			record.put("open_id", openId);
			
			JSONObject json = JSONObject.parseObject(resp);
			JSONObject jobj = json.getJSONObject("data");
			
			record.put("star_number", jobj.getInteger("starNumber"));
			record.put("rank", jobj.getInteger("rank"));
			record.put("phone",jobj.getString("phoneNumber"));
			record.put("phone_hide",jobj.getString("phoneNumberHide"));
			returnJson(record);
		} catch (IOException e) {
			e.printStackTrace();
			returnJson(Errcode.FAIL, "获取用户信息失败");
		}
	}
	
	//	更新用户邮寄地址
	public void updateUserAddress() {
		String uri = "/api/weixin/%s/address";
		String openId = getPara("open_id");
		String address = getPara("address");
		
		String url = WXActivityController.PREURL+String.format(uri, openId);
		Map<String,String> params = new HashMap<String, String>();
		params.put("openId", openId);
		params.put("address", address);
		try {
			String resp = HttpUtil.postWithParamMapNoState(url, params, "UTF-8");
			JSONObject json = JSONObject.parseObject(resp);
			if(json.getInteger("code") == 200) {
				returnJson(Errcode.SUCC, "更新地址成功");
			} else {
				String msg = json.getJSONObject("data").getString("details");
				returnJson(Errcode.FAIL, msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnJson(Errcode.FAIL, "更新地址失败");
		}
	}
	
	
}
