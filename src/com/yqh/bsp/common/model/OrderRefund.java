package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;

/*

 */

@Table(tableName="t_order_refund",pkName="refund_id")
public class OrderRefund extends BaseModel<OrderRefund> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7422908885179209438L;
	public static OrderRefund dao = new OrderRefund();

}
