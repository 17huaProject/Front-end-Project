package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_orders",pkName="order_id")
public class Order extends BaseModel<Order> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8365104698844459397L;
	public static final String ORDERFlAG = "01";
	public static Order dao = new Order();

}
