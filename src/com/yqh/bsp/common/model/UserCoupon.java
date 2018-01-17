package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_user_coupon",pkName="id")
public class UserCoupon extends BaseModel<UserCoupon> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7422908765179209434L;
	public static UserCoupon dao = new UserCoupon();

}
