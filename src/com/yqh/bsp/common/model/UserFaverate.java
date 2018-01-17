package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_user_faverate",pkName="id")
public class UserFaverate extends BaseModel<UserFaverate> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -15893414494091194L;
	public static UserFaverate dao = new UserFaverate();

}
