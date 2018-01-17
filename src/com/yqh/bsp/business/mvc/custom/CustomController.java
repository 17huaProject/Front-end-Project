package com.yqh.bsp.business.mvc.custom;

import java.net.URLDecoder;
import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.interceptor.LoginedAuthInterceptor;
import com.yqh.bsp.common.model.UserCustom;

@Action(controllerKey="/custom")
public class CustomController extends BaseController {
	private static CustomService customService = new CustomService();

	//增加定制需求
	@Before(CustomValidator.class)
	public void add() {
//		UserCustom userCustom = getModel(UserCustom.class,"");
		UserCustom userCustom = new UserCustom();
		userCustom.set("phone", getPara("phone"));
		userCustom.set("contact", getPara("contact"));
		userCustom.set("custom_type", getPara("custom_type"));
		userCustom.set("est_date", getPara("est_date"));
		userCustom.set("est_num", getPara("est_num"));
		userCustom.set("city", getPara("city"));
		LoginUser lUser = getSessionAttr("loginUser");
		if (lUser != null) {
			userCustom.set("user_id", lUser.getUserId());
		} else {
			userCustom.set("user_id", "0");
		}
		userCustom.set("create_time", new Date());
		boolean flag = userCustom.save();
		if (flag) {
			returnJson(Errcode.SUCC,"定制申请提交成功");
		} else {
			returnJson(Errcode.FAIL,"定制申请提交失败");
		}
	}
	
	@Before(LoginedAuthInterceptor.class)
	public void feedback() {
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		String title = getPara("title");
		String content = getPara("content");
		
		
		if (StrKit.isBlank(content)) {
			returnJson(Errcode.FAIL,"请输入留言内容");
		} else {
			try{
				if (StrKit.isBlank(title)) {
					title = "";
				}
				title = URLDecoder.decode(title, "UTF-8");
				content = URLDecoder.decode(content, "UTF-8");
			}catch(Exception e) {
				
			}
			
			Record record = new Record();
			record.set("issuer_id", userId);
			record.set("title", title);
			record.set("content", content);
			record.set("issue_time", new Date());
			Db.save("t_feedback", record);
			
			returnJson(Errcode.SUCC,"留言成功");
		}
	}
	
	@Before(LoginedAuthInterceptor.class)
	public void showMyCustom() {
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		returnJson(customService.queryUserCustom(userId));
		
	}
	
	
}
