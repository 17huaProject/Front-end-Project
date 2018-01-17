package com.yqh.bsp.business.mvc.config;

import java.net.URLDecoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.kit.JsonKit;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.business.mvc.user.UserService;
import com.yqh.bsp.common.model.User;
import com.yqh.bsp.thirdparty.weixin.api.ApiConfig;
import com.yqh.bsp.thirdparty.weixin.api.JSShareApi;
import com.yqh.bsp.thirdparty.weixin.api.UserApi;
import com.yqh.bsp.utils.DateUtil;

@Action(controllerKey="/config")
public class ConfigController extends BaseController {
	private static ConfigService configService = new ConfigService();
	private static String IMAGE_PREFIX = PropKit.get("config.photo.url");
	
	public void supportBank() {
		returnJson(configService.queryBank());
	}
	
	public void supportCity() {
		returnJson(configService.queryCity());
	}
	
	public void getSystem() {
		JSONObject obj = new JSONObject();
		obj.put("site_status", "1");  //网站正常运行， 0=网站暂停运行
		obj.put("site_notice", "欢迎来到一起画！");
		obj.put("sys_time", DateUtil.formatFullfmt(new Date()));
		obj.put("image_prefix", IMAGE_PREFIX);
		obj.put("weixin_appid", ApiConfig.appId);
		returnJson(obj);
	}
	
	public void getWXCode() {
		Map<String,String> params = new HashMap<String,String>();
		Enumeration<String>  en = getParaNames();
		String key = "";
        while(en.hasMoreElements()) {
        	key = en.nextElement();
        	params.put(key, getPara(key));
        }
        renderText(JsonKit.toCompatibleJSONString(params));
	}
	
	/**
	 * 获取微信分享的参数
	 * url ： 当前分享页面的url
	 */
	public void getWXJSConfig() {
		String url = getPara("url");
		if (StrKit.isBlank(url)) {
			returnJson(Errcode.FAIL, "请传入url参数");
			return;
		} else {
			try {
				url = URLDecoder.decode(url,"UTF-8");
				String jsapi_ticket = JSShareApi.getJsApiTicket();
				JSONObject obj = JSShareApi.getJsSignJson(jsapi_ticket, url);
				returnJson(obj);
			} catch (Exception e) {
				e.printStackTrace();
				returnJson(Errcode.FAIL, "获取参数失败");
			}
		}
	}	

}
