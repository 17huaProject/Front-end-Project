package com.yqh.bsp.common.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.common.auth.ApiAuthCore;
import com.yqh.bsp.common.init.DataInit;

public class SignAuthActionInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		
		BaseController c = (BaseController)inv.getController();
		
		String pid = c.getPara("pid");
		String timestamp = c.getPara("timestamp");
		String client =  c.getPara("client");
		String sign =  c.getPara("sign");
		String pkey = DataInit.CACHE_PID_KEY.get(pid);
		if (StrKit.isBlank(pkey) || StrKit.isBlank(timestamp) || StrKit.isBlank(client) ) {
			c.returnJson(Errcode.ERR_LACK_PARAM, "缺少系统参数");
			return;
		}
		String phone =  c.getPara("phone");
		if (StrKit.isBlank(phone)) {
			phone = "";
		}
		if (!ApiAuthCore.signVerify(sign,phone,timestamp, pkey)){
			c.returnJson(Errcode.ERR_INVALID_SIGN, "");
			return;
		}
		
		// 校验sign
		
		inv.invoke();
	}

}
