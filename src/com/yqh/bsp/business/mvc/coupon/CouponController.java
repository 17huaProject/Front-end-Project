package com.yqh.bsp.business.mvc.coupon;

import com.jfinal.aop.Before;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.interceptor.LoginedAuthInterceptor;

@Action(controllerKey="/coupon")
@Before(LoginedAuthInterceptor.class)
public class CouponController extends BaseController {
	private static CouponService couponService = new CouponService();

	//**查询用户的所有优惠券
	public void index() {
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		returnJson(couponService.queryUserCoupon(userId));
	}
	
	//查看单个明细
	public void show() {
	
	}
}
