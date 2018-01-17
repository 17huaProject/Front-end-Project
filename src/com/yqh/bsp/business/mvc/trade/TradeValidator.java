package com.yqh.bsp.business.mvc.trade;

import com.jfinal.core.Controller;
import com.yqh.bsp.base.mvc.BaseValidator;

public class TradeValidator extends BaseValidator {

	@Override
	protected void validate(Controller c) {
		String actionKey = getActionKey();
		if (actionKey.equals("/trade/payOilCard")){
			validateInteger("amount","400","请输入正确充值金额");
		    validateResubmit("sign", "400", "重复提交");	
		} else if (actionKey.equals("/trade/payBankCard")){
			validateRequired("amount","400","请输入正确提现金额");
		    validateResubmit("sign", "400", "重复提交");	
		}

	}

}
