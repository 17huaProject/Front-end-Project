package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_gifts",pkName="gift_id")
public class Gift extends BaseModel<Gift> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8365104698867459397L;
	public static Gift dao = new Gift();

}
