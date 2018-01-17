package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_user_balance_record",pkName="record_id")
public class UserBalanceRecord extends BaseModel<UserBalanceRecord> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7422908885179209434L;
	public static UserBalanceRecord dao = new UserBalanceRecord();

}
