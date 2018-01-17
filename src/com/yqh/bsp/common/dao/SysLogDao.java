package com.yqh.bsp.common.dao;


import java.util.Date;

import com.yqh.bsp.common.model.LogStaffOperate;

public class SysLogDao {
	
	public static boolean saveStaffOperateLog(String staffId,String module,String operate,String content) {
		LogStaffOperate staffLog = new LogStaffOperate();
		staffLog.set("staff_id", staffId);
		staffLog.set("module", module);
		staffLog.set("operate_time", new Date());
		staffLog.set("operate", operate);
		staffLog.set("content", content);
		staffLog.save();
		return true;
	}

}
