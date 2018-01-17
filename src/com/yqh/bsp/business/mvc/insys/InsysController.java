package com.yqh.bsp.business.mvc.insys;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.business.mvc.user.UserService;
import com.yqh.bsp.common.model.OrderDetail;
import com.yqh.bsp.common.model.User;
import com.yqh.bsp.thirdparty.weixin.api.UserApi;

@Action(controllerKey="/insys")
public class InsysController extends BaseController {
	private static InsysService insysService = new InsysService();

	//**
	public void login() {
		String username = getPara("username");
		String password = getPara("password");
		if (StrKit.isBlank(username) || StrKit.isBlank(password)) {
			returnJson(Errcode.FAIL, "用户名或密码错误");
		} else {
			JSONObject json = insysService.login(username, password);
			if (json != null) {
				Session session = SecurityUtils.getSubject().getSession();
				json.put("username", username);
				json.put("access_token", session.getId());
				session.setAttribute("inUser", json);

				returnJson(json);
			} else {
				returnJson(Errcode.FAIL, "用户名或密码错误");
			}
		}
	}
	
	
	public void showTicketList() {
		JSONObject json = getSessionAttr("inUser");
		if (json == null) {
			returnJson(Errcode.FAIL_ACCESS_TIMEOUT, "");
			return;
		}
		String userId = json.getString("user_id");
		String userType = json.getString("user_type");
		int eventId = 0;
		if ("4".equals(userType)) {         //助教
			Record record = Db.findFirst("select id from t_events where assistant_id=? and event_status<>'FINISH' and event_time>(now()-interval 3 hour) and event_time<(now()+interval 24 hour)",userId);
			if (record != null) {
				eventId = record.getInt("id");
			}
		} else if("5".equals(userType)) {   //画师
			Record record = Db.findFirst("select * from t_sysuser_artist where sys_user_id=?",userId);
			if (record != null) {
				int artistId = record.getInt("artist_id");
				record = Db.findFirst("select id from t_events where artist_id=? and event_status<>'FINISH' and event_time>(now()-interval 3 hour) and event_time<(now()+interval 24 hour)",artistId);
				if (record != null) {
					eventId = record.getInt("id");
				}
			}
		}
		List<OrderDetail> list = insysService.queryTicketList(eventId);
		returnJson(list);
	}
	
	
	public void checkTicket() {
		JSONObject json = getSessionAttr("inUser");
		if (json == null) {
			returnJson(Errcode.FAIL_ACCESS_TIMEOUT, "");
			return;
		}
		String eventId = getPara("event_id");
		String ticketCode = getPara("ticket_code");
		String userId = json.getString("user_id");
		if (StrKit.isBlank(eventId) || StrKit.isBlank(ticketCode)) {
			returnJson(Errcode.FAIL, "参数错误");
		} else {
			String[] tickets = ticketCode.split(",");
			boolean flag = false;
			for (String ticket:tickets){
				flag = insysService.updateTicket(eventId, ticket, userId);
				if (!flag) {
					break;
				}
			}
			if (flag) {
				returnJson(Errcode.SUCC, "核销成功");
			} else {
				returnJson(Errcode.FAIL, "核销失败");
			}
		}
	}
	/**
	 * 获取用户微信信息
	 */
	public void getWXUserInfo() {
		String userId = getPara("user_id");
		if (StrKit.isBlank(userId)) {
			returnJson(Errcode.FAIL, "请传入user_id参数");
			return;
		} else {
			User user = UserService.queryByUserId(userId);
			JSONObject json = UserApi.getUserInfoByGrobal(user.getStr("wx_open_id"));
			if (json != null) {
				returnJson(json);
			} else {
				returnJson(Errcode.FAIL, "获取用户信息失败");
			}
		}
	}
}
