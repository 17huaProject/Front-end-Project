package com.yqh.bsp.business.mvc.stat;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.utils.DateUtil;

public class StatService extends BaseService {
	
	public List<Record> statSaveOil() {
		String startTime = DateUtil.formatDate(DateUtil.getSkipTime(-2), "yyyy-MM-dd");
		List<Record> list =Db.find("select combo_id,date_format(create_time,'%Y-%m-%d') as date_time,count(*) as num from t_order_save_oil where create_time>? group by date_format(create_time,'%Y-%m-%d'),combo_id ", startTime);
		return list;
	}
	

}
