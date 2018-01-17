package com.yqh.bsp.common.interceptor;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.common.entity.LoginUser;

public class LoginedAuthInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		BaseController c = (BaseController)inv.getController();
		HttpSession session = c.getSession(false);
		boolean isLogin = false;
		if (session !=null) {
			LoginUser lUser = (LoginUser) session.getAttribute("loginUser");
			if (lUser != null) {
				isLogin = true;
			}
		}
		
		if (!isLogin) {
			c.returnJson(Errcode.FAIL_ACCESS_TIMEOUT, "");
			return;
		}
		inv.invoke();

	}

}
