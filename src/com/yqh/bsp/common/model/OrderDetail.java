package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_order_detail",pkName="id")
public class OrderDetail extends BaseModel<OrderDetail> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8365104689944459397L;
	public static OrderDetail dao = new OrderDetail();

}
