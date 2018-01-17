package com.yqh.bsp.common.event.Listener;

import java.util.Date;
import java.util.List;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Record;
import com.yqh.bsp.business.mvc.config.ConfigService;
import com.yqh.bsp.common.event.RegisterEvent;
import com.yqh.bsp.common.model.User;
import com.yqh.bsp.common.model.UserCoupon;
import com.yqh.bsp.utils.DateUtil;

//注解标记，切勿忘记
//@Listener(order = 1, enableAsync = true, tag="save")
//监听器执行顺序order = 1 越小越优先执行，默认 Integer.MAX_VALUE
//单个监听器的，同步或者异步开关enableAsync = true。当然需要先开启EventPlugin全局异步
//指定event tag，重用event。EventKit.post("save", event);
@Listener
public class RegisterReferListener implements ApplicationListener<RegisterEvent> {

    @Override
	public void onApplicationEvent(RegisterEvent event) {
		User user = (User) event.getSource();
		
		String refererId = user.get("referer_id");
		List<Record> coupons = null;
		if (refererId.length() > 0) {
		    //被推荐人的配置
			coupons = ConfigService.getCouponConfig("REFERRAL");
		} else {
			//没有推荐人的奖励
			coupons = ConfigService.getCouponConfig("REGISTER");
		}
		
		for (Record couponType:coupons) {
			float referralAmount = couponType.getBigDecimal("amount").floatValue();
			// 发抵用券
			UserCoupon coupon = new UserCoupon();
			coupon.set("user_id", user.get("user_id"));
			coupon.set("coupon_type", couponType.get("coupon_type"));
			coupon.set("coupon_name", couponType.get("type_name"));
			coupon.set("coupon_amount", referralAmount);
			coupon.set("create_time", new Date());
			JSONObject json = JSONObject.parseObject(couponType.getStr("type_param"));
			coupon.set("limit_amount", json.get("limit_amount"));
			coupon.set("end_time", DateUtil.getSkipTime(json.getIntValue("limit_time") + 1));
			coupon.set("status", 0);
			coupon.save();
		}
	}

}
