package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="wx_access_token",pkName="openid")
public class WxAccessToken extends BaseModel<WxAccessToken> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7422908885179209439L;
	public static WxAccessToken dao = new WxAccessToken();

}
