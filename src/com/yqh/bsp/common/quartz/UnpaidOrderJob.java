package com.yqh.bsp.common.quartz;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yqh.bsp.base.kit.JsonKit;
import com.yqh.bsp.business.mvc.coupon.CouponService;
import com.yqh.bsp.common.dao.UserAccountDao;
import com.yqh.bsp.common.enums.OrderStatus;
import com.yqh.bsp.common.enums.TransactionType;
import com.yqh.bsp.common.model.Order;
import com.yqh.bsp.utils.DateUtil;

public class UnpaidOrderJob implements Job {
	
	private Logger log = Logger.getLogger(UnpaidOrderJob.class);
	
	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		Map data = jobContext.getJobDetail().getJobDataMap();
		
//		System.out.println(i);
		List<Order> orders = Order.dao.find("select * from t_orders where status =? and create_time < (now() - INTERVAL 18 MINUTE)", OrderStatus.UNPAID.toString());
		String orderId, eventId;
		for (Order order:orders) {
			order.set("status", OrderStatus.CLOSED.toString());
			int number = order.getInt("number");
			eventId = order.get("event_id").toString();
			orderId = order.getStr("order_id");
			try {
				changeOrderAndEvent(eventId, orderId, number);
				float balance = order.getBigDecimal("balance_amount").floatValue();
				if (balance > 0) {
					UserAccountDao.IncomeTransaction(order.getStr("user_id"), orderId, balance, TransactionType.ORDER_REFUND);
				}
				//如果使用电子券，退回
				float couponAmount = order.getBigDecimal("coupon_amount").floatValue();
				if (couponAmount > 0) {
					CouponService.setUnusedCoupon(orderId);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(JsonKit.toCompatibleJSONString(data));
		
		log.info(DateUtil.formatFullfmt(new Date())+ "UnpaidOrderJob do num->" +orders.size());
		
	}
	
	
	@Before(Tx.class)
	public boolean changeOrderAndEvent(String eventId,String orderId,int number) throws SQLException {
		try {
			Db.update("update t_orders set status=? where order_id=?",OrderStatus.CLOSED.toString(),orderId);
			Db.update("update t_event_detail set lock_num=lock_num-"+number+" where event_id=?", new Object[] { eventId });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


}
