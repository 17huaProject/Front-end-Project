package com.yqh.bsp.business.mvc.custom;

import com.jfinal.core.Controller;
import com.yqh.bsp.base.mvc.BaseValidator;

public class CustomValidator extends BaseValidator {

	@Override
	protected void validate(Controller c) {
		String actionKey = getActionKey();
		if (actionKey.equals("/custom/add")){
			validateRequiredString("contact", "400", "请输入联系姓名");
			validateMobile("phone", "400", "请输入正确手机号");
			validateRegex("custom_type", "^PRIVATE|COMPANY$", "400", "请输入正确定制类型");
			validateDate("est_date", "400", "请输入预约时间");
			validateRequiredString("est_num","400", "请填写预计参加人数");
			validateRequiredString("city","400", "选择城市");
		}

	}

}
