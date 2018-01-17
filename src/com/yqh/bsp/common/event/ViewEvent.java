package com.yqh.bsp.common.event;

import net.dreamlu.event.core.ApplicationEvent;

public class ViewEvent extends ApplicationEvent {


    /**
	 * 查看活动详情事件。
	 */
	private static final long serialVersionUID = -3346779442051134533L;

	public ViewEvent(Object source) {
        super(source);
    }

}

