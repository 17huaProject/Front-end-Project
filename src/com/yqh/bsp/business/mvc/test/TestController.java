package com.yqh.bsp.business.mvc.test;

import com.jfinal.aop.Clear;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;

@Action(controllerKey="/test",viewPath="/")
public class TestController extends BaseController {
	@Clear
	public void invoicePrint() {
		String url = "http://service.oil-z.com:8080/SeamTax/doPrint";
		TestService.invoicePrint("aaa",url);
		returnJson(Errcode.SUCC);
	}
}
