package com.yqh.bsp.business.mvc.userinfo;

import java.io.IOException;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.init.DataInit;
import com.yqh.bsp.common.interceptor.LoginedAuthInterceptor;
import com.yqh.bsp.common.model.UserContact;
import com.yqh.bsp.utils.HttpUtil;
import com.yqh.bsp.utils.RegularUtil;

@Action(controllerKey="/userinfo")
@Before(LoginedAuthInterceptor.class)
public class UserinfoController extends BaseController {
	private static UserinfoService userinfoService = new UserinfoService();

	//**
	public void indexContact() {
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		returnJson(userinfoService.queryUserContact(userId));
	}
	
	//查看单个明细
	public void addContact() {
		String name = getPara("name");
		String phone = getPara("phone");
		
		if (StrKit.isBlank(name) || !RegularUtil.isPhoneNumber(phone)) {
			returnJson(Errcode.FAIL, "请添加正确的联系人信息");
			return;
		}
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		UserContact userContact = new UserContact();
		userContact.set("user_id", userId);
		userContact.set("name", name);
		userContact.set("phone", phone);
		
		String defaultFlag = getPara("defalut_flag");
		
		if ("1".equals(defaultFlag)) {
			userContact.set("defalut_flag", 1);
		}
		userContact.set("create_time", new Date());
		if (StrKit.notBlank(getPara("email"))) {
			userContact.set("email", getPara("email"));
		}
		
		userContact.save();
		returnJson(Errcode.SUCC);
	}
	
	public void updateContact() {
		int id = getParaToInt("id");
		UserContact userContact = UserContact.dao.findById(id);
		if (userContact == null) {
			returnJson(Errcode.FAIL, "传入id不正确");
		}
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		if(userId.equals(userContact.get("user_id"))){
			if (StrKit.notBlank(getPara("name"))) {
				userContact.set("name", getPara("name"));
			}
			if (StrKit.notBlank(getPara("phone"))) {
				userContact.set("phone", getPara("phone"));
			}
			if (StrKit.notBlank(getPara("email"))) {
				userContact.set("email", getPara("email"));
			}
			if ("1".equals(getPara("default_flag"))) {
				userContact.set("default_flag", 1);
			} else {
				userContact.set("default_flag", 0);
			}
			userContact.update();
			returnJson(Errcode.SUCC);
		} else {
			returnJson(Errcode.FAIL, "更新失败");
		}
		
	}
	
	public void deleteContact() {
		int id = getParaToInt("id");
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		boolean flag = userinfoService.delete(id,userId);
		if (flag) {
			returnJson(Errcode.SUCC);
		} else {
			returnJson(Errcode.FAIL, "删除失败");
		}
	}
	
	/**
	 * 创建用户分享图片
	 */
	public void createShare() {
		String genImgUrl = DataInit.INNER_SYS_URL+"/genCouponSharedImg";
		
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		try {
			String respStr = HttpUtil.getWithParamString(genImgUrl, "userId="+userId, "UTF-8");
			if(StrKit.notBlank(respStr)) {
				JSONObject json = JSONObject.parseObject(respStr);
				if (json.getInteger("code") == 0) {
					JSONObject j = new JSONObject();
					j.put("share_img", json.getString("data"));
					returnJson(j);
					return;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnJson(Errcode.FAIL, "生成头像失败");
		
	}	
}
