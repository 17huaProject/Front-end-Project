package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="b_city",pkName="code")
public class City extends BaseModel<City> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3594227050525636150L;
	public static City dao = new City();

}
