package com.yqh.bsp.business.mvc.invoice;

import com.jfinal.core.Controller;
import com.yqh.bsp.base.mvc.BaseValidator;

public class InvoiceValidator extends BaseValidator {

	@Override
	protected void validate(Controller c) {
		String actionKey = getActionKey();
		if (actionKey.equals("/invoice/add")){
			validateRequiredString("order_type", "400", "请选择订单类型");
			validateRegex("invoice_type", "TAXCOM|TAXSPE|PERSON", "400", "请选择发票类型");
			validateRequiredString("pattern", "400", "请选择开票方式");
			validateRequiredString("title", "400", "请输入发票抬头");
			validateDouble("amount", 1, 20000, "400", "请输入发票金额");
			validateRequiredString("order_ids", "400", "请输入需要开票订单");
			validateString("bank_card",8,20, "400", "请输入正确银行卡号");
		}

	}

}
