package com.yqh.bsp.business.mvc.combo;

import com.jfinal.core.Controller;
import com.yqh.bsp.base.mvc.BaseValidator;

public class ComboValidator extends BaseValidator {

	@Override
	protected void validate(Controller c) {
		String actionKey = getActionKey();
		if (actionKey.equals("/combo/preOrder")){
			validateRequired("combo_id","400","缺少参数(combo_id)");
			validateRequired("combo_num","400","缺少参数(combo_num)");
			validateRequired("oil_id","400","缺少参数(oil_id)");
			validateRequired("oil_volume","400","缺少参数(oil_volume)");
			validateRequired("order_amount","400","缺少参数(order_amount)");
			validateRequired("paid_amount","400","缺少参数(paid_amount)");
			validateResubmit("sign", "400", "重复提交");
		} 

	}

}
