package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_event_viewer")
public class EventViewer extends BaseModel<EventViewer> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5054938215847858289L;
	public static EventViewer dao = new EventViewer();

}
