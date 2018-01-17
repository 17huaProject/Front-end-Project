package com.yqh.bsp.business.mvc.userinfo;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.UserContact;

public class UserinfoService extends BaseService {
	
	public List<UserContact>  queryUserContact(String userId) {
		return UserContact.dao.find("select id ,name,phone,email,default_flag,create_time from t_user_contact where user_id=?",userId);
	}
	
	public boolean  delete(int id ,String userId) {
		int f = Db.update("delete from t_user_contact where id=? and user_id=?", new Object[]{id,userId});
		if (f>0) {
			return true;
		} else {
			return false;
		}
	}
	
	

}
