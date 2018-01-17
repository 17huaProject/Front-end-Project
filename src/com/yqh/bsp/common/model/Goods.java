package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_goods",pkName="id")
public class Goods extends BaseModel<Goods> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8365104696837459397L;
	public static Goods dao = new Goods();

}
