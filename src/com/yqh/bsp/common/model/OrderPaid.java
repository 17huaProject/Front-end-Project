package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;

/*

 */

@Table(tableName="t_order_paid",pkName="paid_id")
public class OrderPaid extends BaseModel<OrderPaid> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7422908885179209438L;
	public static OrderPaid dao = new OrderPaid();

}
