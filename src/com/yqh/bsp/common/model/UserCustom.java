package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_user_custom",pkName="id")
public class UserCustom extends BaseModel<UserCustom> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7422908885179209434L;
	public static UserCustom dao = new UserCustom();

}
