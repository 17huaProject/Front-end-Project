package com.yqh.bsp.common.template;

import com.jfinal.kit.PropKit;

public class ContantTemplate {
	
	public static int PAGESIZE = PropKit.getInt("config.default.pageSize");
	
	public static String SMS_REGISTER_CODE = "【一起画】您的验证码%s，10分钟内有效！请勿向他人泄露 ";
	
	public static String SMS_FINDPWD_CODE = "【一起画】您的验证码%s，10分钟内有效！请勿向他人泄露";
	
	public static String SMS_SETPAYPWD_CODE = "【一起画】您的验证码%s，10分钟内有效！请勿向他人泄露 ";
	
	public static String SMS_BINDOILCARD_CODE = "【一起画】您的验证码%s，10分钟内有效！请勿向他人泄露 ";
	
}
