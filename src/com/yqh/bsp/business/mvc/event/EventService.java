package com.yqh.bsp.business.mvc.event;

import java.util.List;

import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.Event;
import com.yqh.bsp.common.model.Order;

public class EventService extends BaseService {

	public List<Event>  queryEventList(int pageNo,int pageSize) {
		String url = "";
		return Event.dao.find(url,(pageNo-1)*pageSize,pageSize);
	}
	
	public Event query(int eventId) {
		return Event.dao.findFirst("select e.id,event_name,event_time,closing_time,e.sold,e.capacity,(e.capacity-e.sold) as left_num,e.type ,price,d.view_num,e.city_code,d.event_desc,d.content event_content,d.free_service,artist_id,i.artist_name,i.big_img artist_avatar,i.artist_desc,i.artist_level,art_id,a.art_name,a.big_img,a.content art_content,a.easy_level,venue_id,v.venue_name,v.big_img venue_img,v.address,v.content venue_content,CONCAT_WS(',',v.latitude,v.longitude) as position,e.event_status,is_refund,share_desc from t_events e left join t_event_detail d on e.id=d.event_id left join t_arts a on e.art_id=a.id left join t_artists i on e.artist_id=i.id left join t_venues v on e.venue_id=v.id where e.id=? and e.is_delete=0 ",eventId);
	}
	
	public List<Order> queryPaidUser(int eventId) {
		return Order.dao.find("select p.nickname,p.avatar,o.create_time from t_orders o left join t_user_profile p on o.user_id=p.user_id where o.event_id=? and o.status='SUCCESS'",eventId);
	}

}
