package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_user_profile",pkName="user_id")
public class UserProfile extends BaseModel<UserProfile> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7422908885179209434L;
	public static UserProfile dao = new UserProfile();

}
