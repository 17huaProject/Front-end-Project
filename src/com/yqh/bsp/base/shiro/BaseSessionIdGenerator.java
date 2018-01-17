package com.yqh.bsp.base.shiro;

import java.io.Serializable;
import java.util.UUID;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

public class BaseSessionIdGenerator implements SessionIdGenerator{

	@Override
	public Serializable generateId(Session arg0) {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString().trim();
	}

}

