package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_arts")
public class Art extends BaseModel<Art> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7523413704459739904L;
	public static Art dao = new Art();

}
