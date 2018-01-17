package com.yqh.bsp.business.mvc.user;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yqh.bsp.base.config.AppConfig;
import com.yqh.bsp.base.kit.Reflect;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.User;
import com.yqh.bsp.common.model.UserProfile;
import com.yqh.bsp.thirdparty.weixin.api.UserApi;

public class UserService extends BaseService {
	
	private static Logger log = Logger.getLogger(UserService.class);
	
	public int queryByPhoneOpenId(String phone,String loginType,String openId) {
		User user = User.dao.findFirst("select user_id,login_type,wx_open_id,wb_open_id,qq_open_id from t_users where phone=?", phone);
		if(user == null) {
			return 2;  //手机号未注册
		} else {
			if (openId.equals(user.getStr(loginType.toLowerCase()+"_open_id"))) {
//				LoginUser lUser = new LoginUser();
//				lUser.setUserId(user.getStr("user_id"));
//				lUser.setPhone(user.getStr("Phone"));
//				lUser.setNickname(user.getStr("nickname"));
//				Session session = SecurityUtils.getSubject().getSession();
//				session.setAttribute("loginUser", lUser);
//				lUser.setAccessToken(session.getId().toString());
				return 1;   // 注册并绑定
			} else {
				if (loginType.equalsIgnoreCase(user.getStr("login_type"))) {
					//该用户已绑定其他账号，
					return 0;
				}
				return 3;   //用户手机号已注册，未绑定微信号
			}
		}
	}
	
	
	public User queryByOpenId(String loginType,String openId) {
		return User.dao.findFirst("select user_id,phone from t_users where wx_open_id=?", openId);
	}
	
	public static User queryByUserId(String userId) {
		return User.dao.findFirst("select t.user_id,phone,wx_open_id,balance from t_users t  where t.user_id=?", userId);
	}
	
	public static User queryByPhone(String phone) {
		return User.dao.findFirst("select user_id,wx_open_id from t_users where phone=?", phone);
	}
	
	public boolean saveUser(User user) {
		user.save();
		user.getUserProfile().set("user_id", user.getPKValue()).save();
		
		return true;
	}
	
	public boolean updateUser(User user) {
		user.update();
		
		return true;
	}
	
	public boolean updatePassword(String phone,String newPassword) {
		String userId = queryIdByPhone(phone);
		User user = new User();
		user.set("user_id", userId);
		user.set("password",DigestUtils.shaHex(newPassword));
		user.update();
		return true;
	}
	
	public static Record queryUserByPhone(String phone) {
		Record record = Db.findFirst("select user_id,wx_open_id from t_users where phone=?",phone);
		return record;
	}
	
	public boolean afterLogin(User user){
		return user.update();
	}
	
	public static boolean queryPhoneIsExist(String phone) {
		long count = Db.queryLong("select count(*) from t_users where phone=? ", phone);
		if(count>0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String queryIdByPhone(String phone) {
		Record record = Db.findFirst("select user_id from t_users where phone=? ", phone);
		if(record == null) {
			return "";
		} else {
			return record.getStr("user_id");
		}
	}
	
	public User getWXUserInfo(String openid,User user) {
		JSONObject jsonObj = UserApi.getUserInfo(openid);
//		JSONObject jsonObj  = JSONObject.parseObject("{\"country\":\"中国\",\"province\":\"上海\",\"city\":\"\",\"openid\":\"oR6oj1JG_LSH6V14LsQmurQfKVXI\",\"sex\":1,\"nickname\":\"上杉\",\"headimgurl\":\"http://wx.qlogo.cn/mmhead/jRoggJ2RF3DZXgV2R6FTTA2HaMEicFFamKP7iacAhPzwS0bhyX0HNbhg/0\",\"language\":\"zh_CN\",\"privilege\":[]}");
		
		if (jsonObj != null) {
			log.info("get weixin userinfo==>"+jsonObj.toJSONString());
			String nickname =jsonObj.getString("nickname");
//			{"sex":1,"nickname":"eric·xie","privilege":[],"province":"","openid":"oR6oj1NgOJEZd7ev9KBSrwTLpjPY","language":"zh_CN","headimgurl":"http://wx.qlogo.cn/mmopen/PiajxSqBRaEJ2eOPRkW7W8qwV00qL0ScBFgQ1UePwlP8MlaIAbDBT8r4m5jlib2HHeaeuTASsBnnUSpEfbzrovnA/0","country":"安道尔","city":""}
//			String photo = jsonObj.getString("headimgurl").replace("/0", "/46");
			String photo = jsonObj.getString("headimgurl");

			UserProfile userProfile = new UserProfile();
			userProfile.set("nickname", nickname);
			userProfile.set("avatar", photo);
			userProfile.set("country", jsonObj.getString("country"));
			userProfile.set("province", jsonObj.getString("province"));
			userProfile.set("city", jsonObj.getString("city"));
			userProfile.set("gender", jsonObj.getIntValue("sex"));
			user.setUserProfile(userProfile);
		}
		return user;
		
	}
	
	public static void main(String[] args) {
		AppConfig config = new AppConfig();
		Reflect.on("com.jfinal.core.Config").call("configJFinal",config);
		JSONObject jsonObj = UserApi.getUserInfo("okTOZwO2VKkDpYev32MjD5xqlJ3s");
		if (jsonObj != null) {
			System.out.println(jsonObj.toJSONString());
			String nickname =new String(jsonObj.getString("nickname"));
			nickname = "";
			String photo = jsonObj.getString("headimgurl").replace("/0", "/46");

		}
	}

}
