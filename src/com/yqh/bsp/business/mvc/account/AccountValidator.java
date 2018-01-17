package com.yqh.bsp.business.mvc.account;

import com.jfinal.core.Controller;
import com.yqh.bsp.base.mvc.BaseValidator;

public class AccountValidator extends BaseValidator {

	@Override
	protected void validate(Controller c) {
		String actionKey = getActionKey();
		if (actionKey.equals("/account/bindBankCard")){
			validateRequiredString("bank_id", "400", "请选择银行");
			validateRequiredString("card_type", "400", "请选择卡类型");
			validateString("bank_card",13,20, "400", "请输入正确银行卡号");
//			validateRequiredString("bank_open", "400", "请输入开户行");
		}

	}

}
