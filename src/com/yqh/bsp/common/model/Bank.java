package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="b_bank",pkName="id")
public class Bank extends BaseModel<Bank> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7422908885179209439L;
	public static Bank dao = new Bank();

}
