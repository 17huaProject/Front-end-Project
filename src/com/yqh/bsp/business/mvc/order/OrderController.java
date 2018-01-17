package com.yqh.bsp.business.mvc.order;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.business.mvc.account.AccountService;
import com.yqh.bsp.business.mvc.coupon.CouponService;
import com.yqh.bsp.business.mvc.goods.GoodsService;
import com.yqh.bsp.business.mvc.pay.PayService;
import com.yqh.bsp.common.dao.UserAccountDao;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.enums.OrderStatus;
import com.yqh.bsp.common.enums.OrderType;
import com.yqh.bsp.common.enums.TransactionType;
import com.yqh.bsp.common.interceptor.LoginedAuthInterceptor;
import com.yqh.bsp.common.model.Event;
import com.yqh.bsp.common.model.Goods;
import com.yqh.bsp.common.model.Order;
import com.yqh.bsp.common.model.OrderDetail;
import com.yqh.bsp.common.model.OrderPaid;
import com.yqh.bsp.common.template.ContantTemplate;
import com.yqh.bsp.common.tools.IdGenerator;
import com.yqh.bsp.thirdparty.weixin.WeixinService;
import com.yqh.bsp.utils.RandomUtil;

@Action(controllerKey="/order")
public class OrderController extends BaseController {
	private static OrderService orderService = new OrderService();
	
	@Before(LoginedAuthInterceptor.class)
	public void index() {
		String orderStatus = getPara("order_status");
		String start = getPara("start_time");
		String end = getPara("end_time");
		Integer pageNo =  getParaToInt("page_no"); //开始页数
		Integer pageSize =  getParaToInt("page_size"); //每页显示
		String orderType = getPara("order_type");
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = ContantTemplate.PAGESIZE;
		}
		
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		OrderStatus osenums = null;
		try{
			if (StrKit.notBlank(orderStatus) || !"ALL".equals(orderStatus)){
				osenums = OrderStatus.valueOf(orderStatus);
			}
		}catch(Exception e){
		}
		returnJson(orderService.query(userId, orderType,osenums, start, end, (pageNo-1)*pageSize, pageSize));
	}
	
	@Before({LoginedAuthInterceptor.class, OrderValidator.class})
	public void wxPayRequest() {
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		int eventId = getParaToInt("event_id");
		String orderName = getPara("order_name");
		
		float orderAmount = Float.parseFloat(getPara("order_amount"));
		int number = Integer.parseInt(getPara("number"));
//		if (number<1 || number>5) {
//			returnJson(Errcode.FAIL, "一次只能购票1-5张票！");
//			return;
//		}
		float couponAmount = 0;
		int couponId = 0;
		if (StrKit.notBlank(getPara("coupon_id"))) {
			couponId = getParaToInt("coupon_id");
			couponAmount = CouponService.queryCoupon(couponId, orderAmount);
		}
		
		String payChannel = getPara("pay_channel");
		if (StrKit.isBlank(payChannel)) {
			payChannel = "WXPAY";
		}
		//订单类型  COMMON =普通活动订单  PRIVATE=定制活动订单
		String orderType = getPara("order_type");  
		if (StrKit.isBlank(orderType)) {
			orderType =  OrderType.COMMON.toString();
		}
		
		Event event = null;
		try {
			event = orderService.checkOrder(eventId, number, orderAmount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (event == null) {
			returnJson(Errcode.FAIL, "该活动已售完！");
			return;
		}
		
		Order order = new Order();
		String orderId = IdGenerator.generateOrderId("", Order.ORDERFlAG);
		order.set("order_id", orderId);
		order.set("order_name", orderName);
		order.set("order_img", event.get("big_img"));
		order.set("user_id", userId);
		order.set("event_id", event.get("id"));
		order.set("art_id", event.get("art_id"));
		order.set("artist_id", event.get("artist_id"));
		order.set("venue_id", event.get("venue_id"));
		order.set("cost_price", event.get("price"));  //实际价格
		order.set("sale_price", event.get("price"));  //销售价格
		order.set("create_time", new Date());  //实际时间
		order.set("event_time", event.get("event_time"));
		order.set("order_type", orderType);
		order.set("status", OrderStatus.UNPAID.toString());
		order.set("realname", getPara("realname"));
		order.set("use_phone", getPara("use_phone"));
		order.set("remark", getPara("email"));
		order.set("number", number);
		order.set("order_amount", orderAmount);
		order.set("source", getPara("pid"));
		
		float payMoney = orderAmount ;
		if (couponAmount >0) {
			payMoney = payMoney - couponAmount;
			order.set("coupon_amount", couponAmount);
		}
		float balance = 0;
		if ("1".equals(getPara("is_balance"))) {
			balance = AccountService.queryBalance(userId);
			if (payMoney > balance) {
				payMoney = payMoney - balance;
			} else {
				balance = payMoney;
				payMoney = 0;
			}
			try {
				float bm = UserAccountDao.ExpenseTransaction(userId, orderId, balance, TransactionType.PAY_ORDER);
				if (bm < 0) {  //余额扣款失败
					returnJson(Errcode.FAIL, "账户余额扣款失败");
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		if (payMoney == 0) {
			payChannel = "YUEPAY";	
		}
		String paidId = IdGenerator.generateOrderId("", Order.ORDERFlAG)+RandomUtil.genRandomCode(4);
		OrderPaid orderPaid = new OrderPaid();
		orderPaid.set("paid_id", paidId);
		orderPaid.set("order_id", orderId);
		orderPaid.set("user_id", userId);
		orderPaid.set("order_amount", orderAmount);
		orderPaid.set("balance_amount", balance);
		orderPaid.set("paid_amount", payMoney);
		Date payDate = new Date();
		orderPaid.set("create_time", payDate);
		orderPaid.set("pay_channel", payChannel);
		orderPaid.set("coupon_amount", couponAmount);
		orderPaid.set("coupon_id", couponId);
		
		order.set("balance_amount", balance);
		boolean flag = order.save();

		if (flag) {
			//设置优惠券已使用
			CouponService.setUsedCoupon(couponId, orderId);
			if ("WXPAY".equals(payChannel)) {  //微信支付
				orderPaid.save();
                
				String openid = lUser.getWxOpenId();
				Map<String, String> respMap = WeixinService.pay(paidId, openid, payMoney, orderName, orderType);

				if ("200".equals(respMap.get("status"))) {
					JSONObject json = new JSONObject();
					json.put("order_id", orderId);
					json.put("paid_amount", payMoney);
					json.put("pay_time", payDate);
					json.put("pay_channel", payChannel);
					json.put("pay_info", JSONObject.parseObject(respMap.get("pay_info")));
					returnJson(json);
					return;
				} else {
					returnJson(Errcode.FAIL, respMap.get("message"));
					return;
				}
			} else if ("YUEPAY".equals(payChannel)) {  //全部使用余额支付
				PayService payService  = new PayService();
				try {
					payService.balancePaySuccess(order, orderPaid, orderType);
					//成功后处理票数
					payService.changeEventTicket(order.get("event_id").toString(), order.getInt("number"));
					JSONObject json = new JSONObject();
					json.put("order_id", orderId);
					json.put("paid_amount", payMoney);
					json.put("pay_time", payDate);
					json.put("pay_channel", payChannel);
					returnJson(json);
					return;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			returnJson(Errcode.FAIL, "支付订单不存在");
			return;
		}
	}
	
	@Before(LoginedAuthInterceptor.class)
	public void show() {
		String orderId = getPara("order_id");
	    String orderType = getPara("oder_type");   // COMMON=普通活动 PRIVATE=定制活动 GOODS=商品订单
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		JSONObject json = new JSONObject();
		if (StrKit.isBlank(orderType)) {
			orderType = "COMMON";
		}
		OrderType type = OrderType.valueOf(orderType);
		Order order = orderService.queryOrderDetail(orderId, userId,type);
		if (order != null) {
			OrderStatus os = OrderStatus.valueOf(order.getStr("order_status"));		
			json.putAll(order.getMap());
			if (os == OrderStatus.CLOSED || os==OrderStatus.UNPAID || os==OrderStatus.REFUND) {
				json.put("tickets", new JSONArray());
			} else {
				JSONArray jarr = new JSONArray();
				if (type == OrderType.COMMON) {  //普通订单取出票列表
					List<OrderDetail> ods = orderService.queryOrderTicket(orderId, userId);
					for (OrderDetail od : ods) {
						jarr.add(od.getMap());
					}
				}
				json.put("tickets", jarr);
			}		
		}
		returnJson(json);
	}
	
	@Before(LoginedAuthInterceptor.class)
	public void payAgain() {
		String orderId = getPara("order_id");
		
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		int bonusId = 0;
//		if (StrKit.notBlank(getPara("coupon_id"))) {
//			bonusId = getParaToInt("coupon_id");
//		}

		String payChannel = getPara("pay_channel");
		
		if ("WXPAY".equals(payChannel)) {
			Order order = orderService.queryOrder(orderId, userId);
			if(order != null){
				float orderAmount = order.getBigDecimal("order_amount").floatValue();
				float balance = order.getBigDecimal("balance_amount").floatValue();
				float couponAmount = order.getBigDecimal("coupon_amount").floatValue();
				float payMoney = orderAmount-balance-couponAmount ;

				String paidId = IdGenerator.generateOrderId("", Order.ORDERFlAG)+RandomUtil.genRandomCode(4);
				OrderPaid orderPaid = new OrderPaid();
				orderPaid.set("paid_id", paidId);
				orderPaid.set("order_id", orderId);
				orderPaid.set("user_id", userId);
				orderPaid.set("order_amount", orderAmount);
				orderPaid.set("balance_amount", balance);
				orderPaid.set("paid_amount", payMoney);
				orderPaid.set("coupon_amount", couponAmount);
				Date payDate = new Date();
				orderPaid.set("create_time", payDate);
				orderPaid.set("pay_channel", payChannel);
				orderPaid.save();

				String openid = lUser.getWxOpenId();
				Map<String,String> respMap = WeixinService.pay(paidId, openid, payMoney, order.getStr("order_name"),order.getStr("order_type"));
		
				if ("200".equals(respMap.get("status"))) {	
					JSONObject json = new JSONObject();
					json.put("order_id", orderId);
					json.put("paid_amount", payMoney);
					json.put("pay_time", payDate);
					json.put("pay_channel", payChannel);
					json.put("pay_info", JSONObject.parseObject(respMap.get("pay_info")));
					returnJson(json);
				} else {
					returnJson(Errcode.FAIL, respMap.get("message"));
					return;
				}
			} else {
				returnJson(Errcode.FAIL, "支付订单不存在");
				return;
			}
		}
	}
	
	
	@Before(LoginedAuthInterceptor.class)
	public void payGoods() {
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		Integer goodsId = getParaToInt("goods_id");
		
		float orderAmount = Float.parseFloat(getPara("order_amount"));
		int number = Integer.parseInt(getPara("number"));
		if (number<1) {
			returnJson(Errcode.FAIL, "购买数量不正确");
			return;
		}
		
		float couponAmount = 0;
		int couponId = 0;
		if (StrKit.notBlank(getPara("coupon_id"))) {
			couponId = getParaToInt("coupon_id");
			couponAmount = CouponService.queryCoupon(couponId, orderAmount);
		}
		
		String payChannel = getPara("pay_channel");
		if (StrKit.isBlank(payChannel)) {
			payChannel = "WXPAY";
		}
		
		Goods goods = GoodsService.queryGoods(goodsId);
		if (goods == null) {
			returnJson(Errcode.FAIL, "该商品已售完！");
			return;
		}
		
		float price = goods.getBigDecimal("price").floatValue();
		if (orderAmount != price*number) {
			returnJson(Errcode.FAIL, "订单金额错误！");
			return;
		}
		String orderName = goods.get("name");
		String orderType = OrderType.GOODS.toString();
		Order order = new Order();
		String orderId = IdGenerator.generateOrderId("", Order.ORDERFlAG);
		order.set("order_id", orderId);
		order.set("order_name", orderName);
		order.set("order_img", goods.get("goods_img"));
		order.set("user_id", userId);
		order.set("event_id", goods.get("id"));
		order.set("cost_price", goods.get("price"));  //实际价格
		order.set("sale_price", goods.get("sale_price"));  //销售价格
		order.set("create_time", new Date());  //实际时间
		order.set("order_type",orderType );
		order.set("status", OrderStatus.UNPAID.toString());
		order.set("number", number);
		order.set("order_amount", orderAmount);
		order.set("source", getPara("pid"));
		
		float payMoney = orderAmount ;
		if (couponAmount >0) {
			payMoney = payMoney - couponAmount;
			order.set("coupon_amount", couponAmount);
		}
		float balance = 0;
		if ("1".equals(getPara("is_balance"))) {
			balance = AccountService.queryBalance(userId);
			if (payMoney > balance) {
				payMoney = payMoney - balance;
			} else {
				balance = payMoney;
				payMoney = 0;
			}
			try {
				float bm = UserAccountDao.ExpenseTransaction(userId, orderId, balance, TransactionType.PAY_ORDER);
				if (bm < 0) {  //余额扣款失败
					returnJson(Errcode.FAIL, "账户余额扣款失败");
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		if (payMoney == 0) {
			payChannel = "YUEPAY";	
		}
		String paidId = IdGenerator.generateOrderId("", Order.ORDERFlAG)+RandomUtil.genRandomCode(4);
		OrderPaid orderPaid = new OrderPaid();
		orderPaid.set("paid_id", paidId);
		orderPaid.set("order_id", orderId);
		orderPaid.set("user_id", userId);
		orderPaid.set("order_amount", orderAmount);
		orderPaid.set("balance_amount", balance);
		orderPaid.set("paid_amount", payMoney);
		Date payDate = new Date();
		orderPaid.set("create_time", payDate);
		orderPaid.set("pay_channel", payChannel);	
		orderPaid.set("coupon_amount", couponAmount);
		orderPaid.set("coupon_id", couponId);
		
		order.set("balance_amount", balance);
		boolean flag = order.save();

		if (flag) {
			//设置优惠券已使用
			CouponService.setUsedCoupon(couponId, orderId);
			if ("WXPAY".equals(payChannel)) {
				orderPaid.save();
				
				String openid = lUser.getWxOpenId();
				Map<String, String> respMap = WeixinService.pay(paidId, openid, payMoney, orderName, orderType);

				if ("200".equals(respMap.get("status"))) {
					JSONObject json = new JSONObject();
					json.put("order_id", orderId);
					json.put("paid_amount", payMoney);
					json.put("pay_time", payDate);
					json.put("pay_channel", payChannel);
					json.put("pay_info", JSONObject.parseObject(respMap.get("pay_info")));
					returnJson(json);
					return;
				} else {
					returnJson(Errcode.FAIL, respMap.get("message"));
					return;
				}
			} else if ("YUEPAY".equals(payChannel)) {  //全部使用余额支付
				PayService payService  = new PayService();
				try {
					payService.balancePaySuccess(order, orderPaid, orderType);
					JSONObject json = new JSONObject();
					json.put("order_id", orderId);
					json.put("paid_amount", payMoney);
					json.put("pay_time", payDate);
					json.put("pay_channel", payChannel);
					returnJson(json);
					return;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			returnJson(Errcode.FAIL, "支付订单不存在");
			return;
		}
	}

}
