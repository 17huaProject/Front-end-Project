package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_events")
public class Event extends BaseModel<Event> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -462407837469846315L;
	/**
	 * 
	 */
	public static Event dao = new Event();

}
