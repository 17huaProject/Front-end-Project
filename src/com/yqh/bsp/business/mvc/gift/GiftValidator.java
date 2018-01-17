package com.yqh.bsp.business.mvc.gift;

import com.jfinal.core.Controller;
import com.yqh.bsp.base.mvc.BaseValidator;

public class GiftValidator extends BaseValidator {

	@Override
	protected void validate(Controller c) {
		String actionKey = getActionKey();
		if (actionKey.equals("/gift/add")){
			validateRequiredString("sender", "400", "请输入发送人姓名");
			validateRequiredString("content", "400", "请输入礼品卡留言");
			validateLong("price", 0, 2000, "400", "请输入正确的礼品卡金额");
			validateInteger("number", 1, 100, "400", "请输入正确的礼品卡数量");
			validateInteger("order_amount", "400", "请输入订单总金额");
		}

	}

}
