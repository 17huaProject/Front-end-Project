package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_venues")
public class Venue extends BaseModel<Venue> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7523413704459739904L;
	public static Venue dao = new Venue();

}
