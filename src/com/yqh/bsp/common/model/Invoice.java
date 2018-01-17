package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_invoices")
public class Invoice extends BaseModel<Invoice> {
	
	/**
	 */
	private static final long serialVersionUID = -7523413705679739904L;
	public static Invoice dao = new Invoice();

}
