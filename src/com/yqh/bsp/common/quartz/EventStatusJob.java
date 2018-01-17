package com.yqh.bsp.common.quartz;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.kit.PropKit;
import com.yqh.bsp.common.enums.EventStatus;
import com.yqh.bsp.common.enums.OrderStatus;
import com.yqh.bsp.common.model.Event;
import com.yqh.bsp.common.model.Order;
import com.yqh.bsp.thirdparty.weixin.WeixinMsgService;
import com.yqh.bsp.thirdparty.weixin.api.TemplateData;
import com.yqh.bsp.utils.DateUtil;

public class EventStatusJob implements Job {
	
	private Logger log = Logger.getLogger(EventStatusJob.class);
	
	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		Map data = jobContext.getJobDetail().getJobDataMap();
		
//		System.out.println(i);
		List<Event> events = Event.dao.find("select * from t_events where event_status<>? and is_delete =0 and is_check=1", EventStatus.FINISH.toString());
		String  eventId;
		Date eventTime;
		Date nowTime = new Date();
		for (Event event:events) {
			eventTime = event.getDate("event_time");
			eventId = event.get("id")+"";
			int dtime = DateUtil.getTimeDiffer(eventTime,nowTime);
			if (dtime > 23*60 && dtime <= 24*60) {
				pushEventNotice(eventId);
			} else if (dtime < -600) {
				event.set("event_status", EventStatus.FINISH.toString());
				event.update();
			}
			
		}
		
		log.info(DateUtil.formatFullfmt(new Date())+ "EventStatusJob do num->" +events.size());
		
	}
	
	
	public boolean pushEventNotice(String eventId) {
		try {
			List<Order> orders = Order.dao.find("select order_id,order_name,event_time,o.realname,use_phone,address,wx_open_id from t_orders o left JOIN t_users u on o.user_id=u.user_id left join t_venues v on o.venue_id=v.id where o.event_id=? and o.status=?",eventId,OrderStatus.SUCCESS.toString());
			TemplateData td = null;
			for(Order order:orders) {
				td = TemplateData.New();
				td.setTouser(order.getStr("wx_open_id"))   // 消息接收者
				    .setUrl(PropKit.get("config.CORSFilter")+"/#/oda?orderID="+order.getStr("order_id"))
				    // 模板参数
				    .add("first", "欢迎您参加一起画活动！", "#000")
				    .add("keyword1", order.get("realname", "")+"", "#000")
				    .add("keyword2", order.getStr("order_name"), "#000")
				    .add("keyword3", DateUtil.formatDate(order.getDate("event_time"), "yyyy年MM月dd日 HH:mm"), "#00f") 
				    .add("keyword4", order.getStr("address"), "#000")
				    .add("remark", "点击查看活动详情！", "#999");
				
				WeixinMsgService.sendEventNotice(td);
			}
 			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


}
