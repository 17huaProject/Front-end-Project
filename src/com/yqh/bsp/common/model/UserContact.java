package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_user_contact",pkName="id")
public class UserContact extends BaseModel<UserContact> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7422908881569209434L;
	public static UserContact dao = new UserContact();

}
