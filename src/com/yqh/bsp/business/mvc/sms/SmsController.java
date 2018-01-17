package com.yqh.bsp.business.mvc.sms;

import com.jfinal.aop.Before;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.business.mvc.user.UserService;
import com.yqh.bsp.common.enums.SmsCode;
import com.yqh.bsp.common.interceptor.LoginedAuthInterceptor;
import com.yqh.bsp.utils.RegularUtil;
@Action(controllerKey = "/sms")
public class SmsController extends BaseController {
	
	private static SmsService smsService = new SmsService(); 
	
	public void sendRegister() {
		String phone = getPara("phone");
		if (RegularUtil.isPhoneNumber(phone)) {
			if (UserService.queryPhoneIsExist(phone)) {
				returnJson(Errcode.FAIL, "用户已注册！");
				return;
			}	
			if(smsService.sendCode(phone,SmsCode.REG_USER).length()>0) {
				returnJson(Errcode.SUCC, "");
			} else {
				returnJson(Errcode.FAIL, "短信发送失败");
			}
		} else {
			returnJson(Errcode.FAIL, "手机号不正确");
		}
		
	}
	
	/**
	 * 忘记密码发送验证短信
	 */
	public void sendFindPassword() {
		String phone = getPara("phone");
		if (RegularUtil.isPhoneNumber(phone)) {
			
			if(smsService.sendCode(phone, SmsCode.FIND_PWD).length()>0) {
				returnJson(Errcode.SUCC, "");
			} else {
				returnJson(Errcode.FAIL, "短信发送失败");
			}
		} else {
			returnJson(Errcode.FAIL, "手机号不正确");
		}
	}
	
	@Before(LoginedAuthInterceptor.class)
	public void sendSetPaypassword() {
		String phone = getPara("phone");
		if (RegularUtil.isPhoneNumber(phone)) {
			
			if(smsService.sendCode(phone, SmsCode.SET_PAYPWD).length()>0) {
				returnJson(Errcode.SUCC, "");
			} else {
				returnJson(Errcode.FAIL, "短信发送失败");
			}
		} else {
			returnJson(Errcode.FAIL, "手机号不正确");
		}
	}
	


}
