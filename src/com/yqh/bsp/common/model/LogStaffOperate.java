package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="log_staff_operate",pkName="id")
public class LogStaffOperate extends BaseModel<LogStaffOperate> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 410785289583051782L;
	public static LogStaffOperate dao = new LogStaffOperate();

}
