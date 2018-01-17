package com.yqh.bsp.common.init;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yqh.bsp.common.tools.IdGenerator;
import com.yqh.bsp.utils.DateUtil;

public class DataInit {
	
	public static Map<String,String> CACHE_PID_KEY = new HashMap<String, String>();
	public final static String SALES_PHOTO_URL=PropKit.get("config.photo.url");
	public final static String CORS_FILTER=PropKit.get("config.CORSFilter");
	public final static String INNER_SYS_URL=PropKit.get("inner.sys");
	
	public void userIdInit() {
		Record record = Db.findFirst("select max(user_id) as user_id from t_users");
		if (record != null) {
			String userId = record.getStr("user_id");
			IdGenerator.preUserIdSeq = DateUtil.formatYearMonth(new Date());
			String splitStr = "1100000";
			if (userId != null) {
				splitStr = userId.substring(userId.length()-7);
			}
			
			IdGenerator.userIdMidSeq = Integer.parseInt(splitStr.substring(0,2));
			IdGenerator.userIdSeq = Integer.parseInt(splitStr.substring(2));
		}
	}
	
	public void pidKeyInit() {
		List<Record> cfgs = Db.find("SELECT pid,pkey FROM b_pid_key ");
		for(Record cfg:cfgs) {
			CACHE_PID_KEY.put(cfg.getStr("pid"), cfg.getStr("pkey"));
		}
	}
	

}
