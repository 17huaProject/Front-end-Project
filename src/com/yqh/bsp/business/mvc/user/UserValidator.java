package com.yqh.bsp.business.mvc.user;

import com.jfinal.core.Controller;
import com.yqh.bsp.base.mvc.BaseValidator;

public class UserValidator extends BaseValidator {

	@Override
	protected void validate(Controller c) {
		String actionKey = getActionKey();
		if (actionKey.equals("/user/registerAndBind")){
			validateMobile("phone", "400", "手机号不正确");
			validateRequired("login_type","400","请求参数不正确");
			validateString("open_id", 10, 100, "400", "请求参数不正确");
			validateString("sms_code", 3, 6, "400", "验证码输入不正确");
			
		} else if (actionKey.equals("/user/register")){
			validateMobile("phone", "400", "手机号不正确");
			validateString("password", 5, 50, "400", "请求参数不正确");
			validateString("sms_code", 3, 6, "400", "验证码输入不正确");
		} else if (actionKey.equals("/user/checkMobile")){
			validateResubmit("sign", "400", "重复提交");
		} else if (actionKey.equals("/user/joinus")) {
			validateRequired("name","400","请输入姓名");
			validateMobile("phone", "400", "手机号不正确");
			validateRequired("position","400","请输入职位");
			validateRequired("description","400","请输入自我介绍");
		}

	}

}
