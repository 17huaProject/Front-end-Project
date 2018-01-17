package com.yqh.bsp.utils;

import com.jfinal.kit.StrKit;
import com.yqh.bsp.thirdparty.sms.FeiTianSmsService;
import com.yqh.bsp.thirdparty.sms.HuyiSmsService;


public class SMSUtil {
	
	public static boolean sendSMS(String mobile,String content) {
		String flag = FeiTianSmsService.sendSMS(mobile, content,"");
		if(StrKit.isBlank(flag)) {
			return false;
		} else {
			return true;
		}
		
	}
	
	
	
}
