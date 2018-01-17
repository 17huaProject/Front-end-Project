package com.yqh.bsp.business.mvc.coupon;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.UserCoupon;

public class CouponService extends BaseService {
	
	public List<UserCoupon>  queryUserCoupon(String userId) {
		return UserCoupon.dao.find("select id ,coupon_name,coupon_amount,create_time,end_time,limit_amount,status from t_user_coupon where user_id=?",userId);
	}
	
	public static float queryCoupon(int couponId, float orderAmount) {
		UserCoupon userCoupon = UserCoupon.dao.findFirst("select coupon_amount,limit_amount from t_user_coupon where id=? and status=0  and end_time>now()", couponId);
		float amount = 0;
		if (userCoupon != null) {
			float limitAmount =  userCoupon.getBigDecimal("limit_amount").floatValue();
			if (limitAmount ==0 || (limitAmount>0 && orderAmount >=limitAmount)) {
				amount = userCoupon.getBigDecimal("coupon_amount").floatValue();
			}
		}
		return amount;
	}
	
	public static void setUsedCoupon(int couponId,String orderId) {
		Db.update("update t_user_coupon set status=1,used_time=now(),order_id=? where id=?",orderId,couponId);
	}
	
	public static void setUnusedCoupon(String orderId) {
		Db.update("update t_user_coupon set status=0,order_id='' where order_id=?",orderId);
	}

}
