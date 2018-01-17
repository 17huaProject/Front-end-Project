package com.yqh.bsp.common.event.Listener;

import java.util.Date;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;

import com.jfinal.plugin.activerecord.Db;
import com.yqh.bsp.common.event.ViewEvent;
import com.yqh.bsp.common.model.EventViewer;

//注解标记，切勿忘记
//@Listener(order = 1, enableAsync = true, tag="save")
//监听器执行顺序order = 1 越小越优先执行，默认 Integer.MAX_VALUE
//单个监听器的，同步或者异步开关enableAsync = true。当然需要先开启EventPlugin全局异步
//指定event tag，重用event。EventKit.post("save", event);
@Listener(order=2,enableAsync=true)
public class ViewerSaveListener implements ApplicationListener<ViewEvent> {

    @Override
	public void onApplicationEvent(ViewEvent event) {
    	EventViewer ev = (EventViewer) event.getSource();
		
		String userId = ev.get("user_id");
		ev.set("view_time", new Date());
		ev.save();
		Db.update("update t_event_detail set view_num=view_num+1 where event_id=?",ev.get("event_id"));
		
	}

}
