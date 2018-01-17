package com.yqh.bsp.business.mvc.pay;

import java.sql.SQLException;
import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.enums.EventStatus;
import com.yqh.bsp.common.enums.GiftStatus;
import com.yqh.bsp.common.enums.OrderStatus;
import com.yqh.bsp.common.enums.OrderType;
import com.yqh.bsp.common.enums.PayStatus;
import com.yqh.bsp.common.enums.ValidStatus;
import com.yqh.bsp.common.model.Event;
import com.yqh.bsp.common.model.Gift;
import com.yqh.bsp.common.model.Order;
import com.yqh.bsp.common.model.OrderDetail;
import com.yqh.bsp.common.model.OrderPaid;
import com.yqh.bsp.utils.RandomUtil;

public class PayService extends BaseService {
	
	@Before(Tx.class)
	public Order orderPaySuccess(OrderPaid orderPaid,String orderType) throws SQLException {	
		String orderId = orderPaid.get("order_id");
		Order order = Order.dao.findById(orderId);

		if (OrderStatus.UNPAID.toString().equals(order.getStr("status"))) {
			order.set("status", OrderStatus.SUCCESS.toString());
			
			order.set("paid_id", orderPaid.get("paid_id"));
			order.set("balance_amount", orderPaid.get("balance_amount"));
			order.set("paid_amount", orderPaid.get("paid_amount"));
			order.set("coupon_amount", orderPaid.get("coupon_amount"));
			order.update();
			if (orderType.equals(OrderType.COMMON.toString())) {
				int number = order.getInt("number");
				OrderDetail oDetail = null;
				for (int i = 0; i < number; i++) {
					oDetail = new OrderDetail();
					oDetail.set("order_id", orderId);
					oDetail.set("user_id", order.get("user_id"));
					oDetail.set("event_id", order.get("event_id"));
					oDetail.set("ticket_code", RandomUtil.genRandomCode(5) + RandomUtil.genRandomCode(5));
					oDetail.save();
				}
			}
		} 
		return order;
	}
	/**
	 * 直接账户余额支付成功
	 * @param order
	 * @param orderPaid
	 * @param orderType
	 * @return
	 * @throws SQLException
	 */
	@Before(Tx.class)
	public Order balancePaySuccess(Order order,OrderPaid orderPaid,String orderType) throws SQLException {	
		String orderId = orderPaid.get("order_id");

		if (OrderStatus.UNPAID.toString().equals(order.getStr("status"))) {
			order.set("status", OrderStatus.SUCCESS.toString());
			order.set("paid_id", orderPaid.get("paid_id"));
			order.set("paid_amount", orderPaid.get("paid_amount"));
			order.set("coupon_amount", orderPaid.get("coupon_amount"));
			order.update();
			
			orderPaid.set("status", ValidStatus.VALID.getVal());
			orderPaid.set("pay_time", new Date());
			orderPaid.set("pay_status", PayStatus.SUCC.toString());
			orderPaid.save();
			if (orderType.equals(OrderType.COMMON.toString())) {
				int number = order.getInt("number");
				OrderDetail oDetail = null;
				for (int i = 0; i < number; i++) {
					oDetail = new OrderDetail();
					oDetail.set("order_id", orderId);
					oDetail.set("user_id", order.get("user_id"));
					oDetail.set("event_id", order.get("event_id"));
					oDetail.set("ticket_code", RandomUtil.genRandomCode(5) + RandomUtil.genRandomCode(5));
					oDetail.save();
				}
			}
		} 
		return order;
	}
	
	@Before(Tx.class)
	public Gift giftPaySuccess(OrderPaid orderPaid,String orderType) throws SQLException {	
		String giftId = orderPaid.get("order_id");
		Gift gift = Gift.dao.findById(giftId);

		if (GiftStatus.UNPAID.toString().equals(gift.getStr("gift_status"))) {
			gift.set("gift_status", GiftStatus.PAID.toString());
			
			gift.set("paid_id", orderPaid.get("paid_id"));
			gift.update();
			
		} 
		return gift;
	}
	
	/**
	 * 支付成功后，修改锁票数和 订单已购票
	 * @param eventId
	 * @param number
	 * @return
	 */
	@Before(Tx.class)
	public boolean changeEventTicket(String eventId,int number) throws SQLException {
		try {
			Event event = Event.dao.findById(eventId);
			if (event != null) {
				int capacity = event.getInt("capacity");
				int sold = event.getInt("sold");
				event.set("sold", sold + number);
				if (capacity == sold + number) {
					event.set("event_status", EventStatus.SOLDOUT.toString());
				}
				event.update();
			}
			Db.update("update t_event_detail set lock_num=lock_num-"+number+" where event_id=?", new Object[] { eventId });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
