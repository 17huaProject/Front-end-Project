package com.yqh.bsp.business.mvc.insys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.enums.OrderStatus;
import com.yqh.bsp.common.model.OrderDetail;
import com.yqh.bsp.utils.HttpUtil;

public class InsysService extends BaseService {
	private static String INNERLOGIN = "http://localhost:8090/yqhCMS/h5/verifyUser";
	public JSONObject login(String username,String password) {
		Map<String,String> paraMap = new HashedMap();
		paraMap.put("userName", username);
		paraMap.put("password", password);
		String resp = HttpUtil.postWithParamMap(INNERLOGIN, paraMap, "UTF-8");
		if (StrKit.isBlank(resp)) {
			return null;
		}else{
			JSONObject json = JSONObject.parseObject(resp);
			int code = json.getInteger("code");
			if (code == 0) {
                JSONObject rsjson = new JSONObject();
                rsjson.put("user_type", json.getString("userType"));
                rsjson.put("user_id", json.getString("userId"));
				return rsjson;
			} else {
				return null;
			}
		}
	}
	
	public List<OrderDetail> queryTicketList(int eventId) {
		if (eventId > 0) {
			return OrderDetail.dao.find("select d.event_id,d.order_id,ticket_code,used_flag,used_time,realname,use_phone from t_order_detail d left join t_orders o on d.order_id=o.order_id where d.event_id=?", eventId);
		} else {
			return new ArrayList();
		}
	}
	
	public boolean updateTicket(String eventId,String ticketCode,String operatorId) {
		OrderDetail od = OrderDetail.dao.findFirst("select * from t_order_detail where event_id=? and ticket_code=?",eventId,ticketCode);
		if (od != null) {
			if (od.getInt("used_flag") == 0) {
				od.set("used_flag", 1);
				od.set("used_time", new Date());
				od.set("operator", operatorId);
				od.update();
				
				Db.update("update t_orders set status=? where order_id=?",OrderStatus.FINISH.toString(),od.get("order_id"));
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	
	public static void main(String[] args) {
		Map<String,String> paraMap = new HashedMap();
		paraMap.put("userName", "afanti");
		paraMap.put("password", "123456");
		String resp = HttpUtil.postWithParamMap(INNERLOGIN, paraMap, "UTF-8");
		System.out.println(resp);
	}

}
