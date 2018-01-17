package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_artists")
public class Artist extends BaseModel<Artist> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7523413704459739904L;
	public static Artist dao = new Artist();

}
