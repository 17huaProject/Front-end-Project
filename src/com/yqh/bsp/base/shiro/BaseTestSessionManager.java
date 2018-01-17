package com.yqh.bsp.base.shiro;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

public class BaseTestSessionManager extends DefaultWebSessionManager {
	
	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
//		认证操作
		// TODO Auto-generated method stub
		return super.getSessionId(request, response);
	}

}
