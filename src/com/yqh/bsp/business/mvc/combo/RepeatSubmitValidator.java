package com.yqh.bsp.business.mvc.combo;

import com.jfinal.core.Controller;
import com.yqh.bsp.base.mvc.BaseValidator;

public class RepeatSubmitValidator extends BaseValidator {

	protected void validate(Controller c) {
	    validateToken("blogToken", "msg", "alert('请不要重复提交')");
	}

}