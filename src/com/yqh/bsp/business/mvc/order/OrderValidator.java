package com.yqh.bsp.business.mvc.order;

import com.jfinal.core.Controller;
import com.yqh.bsp.base.mvc.BaseValidator;

public class OrderValidator extends BaseValidator {

	@Override
	protected void validate(Controller c) {
		String actionKey = getActionKey();
		if (actionKey.equals("/order/wxPayRequest")){
			validateRequired("event_id","400","缺少参数(event_id)");
			validateRequired("order_name","400","缺少参数(order_name)");
			validateRequired("order_amount","400","缺少参数(order_amount)");
			validateInteger("number",1,100,"400","缺少参数(number)");
			validateRequired("realname","400","缺少参数(realname)");
			validateResubmit("use_phone", "400", "缺少参数(use_phone)");
			validateResubmit("is_balance", "400", "缺少参数(is_balance)");
		} 

	}

}
