package com.yqh.bsp.business.mvc.order;

import java.sql.SQLException;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.enums.OrderStatus;
import com.yqh.bsp.common.enums.OrderType;
import com.yqh.bsp.common.model.Event;
import com.yqh.bsp.common.model.Order;
import com.yqh.bsp.common.model.OrderDetail;

public class OrderService extends BaseService {
    //事务

	@Before(Tx.class)
	public Event checkOrder(int eventId,int number,float orderAmount) throws SQLException {
		Event event = Event.dao.findFirst("select e.id,event_name,event_time,closing_time,art_id,artist_id,venue_id,capacity,(sold+lock_num) sold,event_status,price,e.create_time,a.big_img  from t_events e left join t_event_detail d on e.id=d.event_id  left join t_arts a on e.art_id=a.id where e.id=?",eventId);
		if (event != null) {
			float price = Float.parseFloat(event.get("price").toString());
			int capacity = event.getInt("capacity");
			int sold = Integer.parseInt(event.get("sold").toString());
			
			if (number > (capacity-sold) || orderAmount != price*number ) {
				return null;
			} else {
				Db.update("update t_event_detail set lock_num=lock_num+? where event_id=?",new Object[]{number,event.get("id")});
			}
		}
		return event;
	}
	
	
	public Order queryOrder(String orderId,String userId) {
		return Order.dao.findFirst("select * from t_orders where order_id=? and user_id=?",orderId,userId);
	}
	
	
	public List<Order> query(String userId,String orderType,OrderStatus orderStatus,String sdate,String edate,int start,int size) {
		StringBuilder sql = new StringBuilder();
		sql.append("select order_id,order_name,order_img,event_time,o.create_time,number,order_amount,cost_price,v.address,o.status order_status,o.order_type from t_orders o left join t_venues v on o.venue_id=v.id where user_id='");
		sql.append(userId).append("'");
		if (orderStatus != null) {
			sql.append(" and o.status='").append(orderStatus.toString()).append("'");
		}
		if (StrKit.notBlank(orderType)) {
			sql.append(" and o.order_type ='").append(orderType).append("'");
		}
		if(StrKit.notBlank(sdate)) {
			sql.append(" and o.create_time>='").append(sdate).append("'");
		}
		if(StrKit.notBlank(edate)) {
			sql.append(" and o.create_time<='").append(edate).append(" 23:59:59'");
		}
		sql.append(" order by order_id desc limit ?,?");
		return Order.dao.find(sql.toString(),start,size);
	}
	
	public Order queryOrderDetail(String orderId,String userId,OrderType orderType) {
		String sql="";
		switch (orderType) {
		case COMMON:
			sql = "select order_id,order_name,order_img,event_time,o.create_time,number,order_amount,balance_amount,paid_amount,coupon_amount,cost_price,v.venue_name,v.address,v.latitude,v.longitude,o.status order_status from t_orders o left join t_venues v on o.venue_id=v.id where order_id=? and user_id=? ";
			break;
		case GOODS:
			sql = "select order_id,g.id goods_id,order_name,order_img,o.create_time,number,order_amount,balance_amount,paid_amount,coupon_amount,cost_price,o.status order_status from t_orders o left join t_goods g on g.id=o.event_id where order_id=? and user_id=? and order_type='GOODS'";
			break;
		default:
			sql = "select order_id,order_name,order_img,event_time,o.create_time,number,order_amount,balance_amount,paid_amount,coupon_amount,cost_price,v.venue_name,v.address,v.latitude,v.longitude,o.status order_status from t_orders o left join t_venues v on o.venue_id=v.id where order_id=? and user_id=? ";
			break;
		}
			
		return Order.dao.findFirst(sql,orderId,userId);
	}
	
	public List<OrderDetail> queryOrderTicket(String orderId,String userId) {
		return OrderDetail.dao.find("select ticket_code,used_flag,used_time from t_order_detail where order_id=? and user_id=?",orderId,userId);
	}

}
