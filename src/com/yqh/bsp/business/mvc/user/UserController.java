package com.yqh.bsp.business.mvc.user;

import java.io.IOException;
import java.util.Date;

import net.dreamlu.event.EventKit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.config.AppConfig;
import com.yqh.bsp.base.kit.Reflect;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.business.mvc.sms.SmsService;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.enums.UserType;
import com.yqh.bsp.common.event.RegisterEvent;
import com.yqh.bsp.common.init.DataInit;
import com.yqh.bsp.common.interceptor.LoginedAuthInterceptor;
import com.yqh.bsp.common.model.User;
import com.yqh.bsp.common.model.WxAccessToken;
import com.yqh.bsp.common.tools.ApiTool;
import com.yqh.bsp.common.tools.IdGenerator;
import com.yqh.bsp.thirdparty.weixin.api.SnsAccessTokenApi;
import com.yqh.bsp.utils.HttpUtil;
import com.yqh.bsp.utils.RegularUtil;

@Action(controllerKey = "/user")
public class UserController extends BaseController {

	private static UserService userService = new UserService();

    @Before(UserValidator.class)
	public void checkPhone() {
		String Phone = getPara("phone");
		String loginType = getPara("login_type");
		String openId = getPara("open_id");
		if (StrKit.isBlank(Phone) || StrKit.isBlank(loginType) || StrKit.isBlank(openId)) {
			returnJson(Errcode.ERR_LACK_PARAM, "");
			return;
		} else {
			int flag = userService.queryByPhoneOpenId(Phone, loginType, openId);
			JSONObject record = new JSONObject();
			record.put("result", flag);
			if (flag == 0) {
				returnJson(Errcode.FAIL, "用户账号绑定失败");
				return;
			} else if (flag == 1) {
				Subject currentUser = SecurityUtils.getSubject();
				UsernamePasswordToken token = new UsernamePasswordToken(Phone, loginType.toUpperCase());
				token.setRememberMe(true);
				try {
					currentUser.login(token);
					User user = new User();
					record.put("login_user", doAfterLogin(user));
					returnJson(record);
					return;
				} catch (UnknownAccountException e) {
					returnJson(Errcode.FAIL, e.getMessage());
				} catch (DisabledAccountException e) {
					returnJson(Errcode.FAIL, e.getMessage());
				} catch (AuthenticationException e) {
					returnJson(Errcode.FAIL, e.getMessage());
				} catch (Exception e) {
					returnJson(Errcode.FAIL_INNER_ERROR, e.getMessage());
				}
			} else {
				returnJson(record);
				return;
			}
		}
	}
   

    /**
     * 
     * @param user
     * @return
     */
	private JSONObject doAfterLogin(User user) {

		// update session
		LoginUser lUser = getSessionAttr("loginUser");
		lUser.setAccessToken(getSession().getId());
		setSessionAttr("loginUser", lUser);

		// update user info
		if (user != null) {
			user.set("user_id", lUser.getUserId());
			user.set("lastip", ApiTool.getIpAddr(this.getRequest()));
			user.set("last_time", new Date());
			user.set("login_num", lUser.getLoginNum() + 1);
			
			userService.afterLogin(user);
		}
		// 是否有推荐人
		
		// return json struction
		JSONObject json = new JSONObject();
		json.put("user_id", lUser.getUserId());
		json.put("phone", lUser.getPhone());
		json.put("nickname", lUser.getNickname());
		json.put("balance", lUser.getBalance());
		json.put("avatar", lUser.getAvatar());
		json.put("last_time", lUser.getLastTime());
		json.put("user_type", lUser.getUserType());
		json.put("access_token", lUser.getAccessToken());
		return json;
	}

	/**
	 * 检查微信的openid是否已注册并绑定账号 传入获取到的code， 根据code获取openid
	 */
	public void checkWX() {
		String code = getPara("code");
		if (StrKit.isBlank(code)) {
			returnJson(Errcode.FAIL, "缺少code参数");
		}
		
//		 JSONObject obj = SnsAccessTokenApi.getSnsAccessToken(code);
		String openId = SnsAccessTokenApi.getSnsOpenId(code);
		if (StrKit.isBlank(openId)) {
			returnJson(Errcode.FAIL, "获取opend id失败");
		} else {
			User user = userService.queryByOpenId("WX", openId);
			JSONObject record = new JSONObject();
			if (user == null) {
				record.put("result", 2);
				returnJson(record);
				return;
			} else {
				record.put("result", 1);
				// 根据openid 获取用户Phone
				Subject currentUser = SecurityUtils.getSubject();
				UsernamePasswordToken token = new UsernamePasswordToken(user.getStr("phone"), "WX");
				token.setRememberMe(true);
				try {
					currentUser.login(token);
					record.put("login_user", doAfterLogin(user));
					returnJson(record);
					return;
				} catch (UnknownAccountException e) {
					returnJson(Errcode.FAIL, e.getMessage());
				} catch (DisabledAccountException e) {
					returnJson(Errcode.FAIL, e.getMessage());
				} catch (AuthenticationException e) {
					returnJson(Errcode.FAIL, e.getMessage());
				} catch (Exception e) {
					returnJson(Errcode.FAIL_INNER_ERROR, e.getMessage());
				}
			}
		}
	}

	// 根据scope=snsapi_userinfo 的code 调用获取用户信息， 查询用户是否注册
	public void checkWXUser() {
		String code = getPara("code");
		if (StrKit.isBlank(code)) {
			returnJson(Errcode.FAIL, "缺少code参数");
		}
		JSONObject obj = SnsAccessTokenApi.getSnsAccessToken(code);
		/**
		 * { "access_token":"ACCESS_TOKEN", "expires_in":7200,
		 * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE",
		 * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL" }
		 */
		
		// save and return openid
		try {
			String openId = obj.getString("openid");
			if (StrKit.isBlank(openId)) {
				returnJson(Errcode.FAIL, "获取opend id失败");
			} else {
				User user = userService.queryByOpenId("WX", openId);
				JSONObject record = new JSONObject();
				record.put("open_id", openId);
				if (user == null) {
					WxAccessToken wxToken = new WxAccessToken();
					wxToken.set("openid", openId);
					wxToken.set("access_token", obj.get("access_token"));
					wxToken.set("expires_in", obj.get("expires_in"));
					wxToken.set("refresh_token", obj.get("refresh_token"));
					wxToken.set("unionid", obj.get("unionid"));
					if (StrKit.notBlank(obj.getString("access_token"))) {
						Db.update("delete from wx_access_token where openid=?", openId);
						wxToken.save();
					}
					
					record.put("is_user", "0"); //未注册
					returnJson(record);
					return;
				} else {
					record.put("is_user", "1"); //已注册
					// 根据openid 获取用户Phone
					Subject currentUser = SecurityUtils.getSubject();
					UsernamePasswordToken token = new UsernamePasswordToken(user.getStr("phone"), "WX");
					token.setRememberMe(true);
					try {
						currentUser.login(token);
						record.put("login_user", doAfterLogin(user));
						returnJson(record);
						return;
					} catch (UnknownAccountException e) {
						returnJson(Errcode.FAIL, e.getMessage());
					} catch (DisabledAccountException e) {
						returnJson(Errcode.FAIL, e.getMessage());
					} catch (AuthenticationException e) {
						returnJson(Errcode.FAIL, e.getMessage());
					} catch (Exception e) {
						returnJson(Errcode.FAIL_INNER_ERROR, e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 登录进行登录
	 * 
	 * Phone password转md5传过来
	 */
	public void login() {

		String Phone = getPara("phone");
		String password = getPara("password");
		if (StrKit.isBlank(Phone) || StrKit.isBlank(password)) {
			returnJson(Errcode.ERR_LACK_PARAM, "");
			return;
		}

		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(Phone, password);
		token.setRememberMe(true);
		try {
			currentUser.login(token);

			JSONObject record = new JSONObject();
			User user = new User();
			record.put("login_user", doAfterLogin(user));
			returnJson(record);
		} catch (UnknownAccountException e) {
			returnJson(Errcode.FAIL, e.getMessage());
		} catch (DisabledAccountException e) {
			returnJson(Errcode.FAIL, e.getMessage());
		} catch (AuthenticationException e) {
			returnJson(Errcode.FAIL, e.getMessage());
		} catch (Exception e) {
			returnJson(Errcode.FAIL_INNER_ERROR, e.getMessage());
		}
	}
	
	public void logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
		}
		returnJson(Errcode.SUCC, "退出成功！");
	}

	/**
	 * 验证登录并绑定openid号
	 */
//	public void loginAndBind() {
//
//		String Phone = getPara("phone");
//		String password = getPara("password");
//		if (StrKit.isBlank(Phone) || StrKit.isBlank(password)) {
//			returnJson(Errcode.ERR_LACK_PARAM, "");
//			return;
//		}
//		String loginType = getPara("login_type");
//		String openId = getPara("open_id");
//		if (StrKit.isBlank(loginType) || StrKit.isBlank(openId)) {
//			returnJson(Errcode.ERR_LACK_PARAM, "");
//			return;
//		}
//
//		Subject currentUser = SecurityUtils.getSubject();
//		UsernamePasswordToken token = new UsernamePasswordToken(Phone, password);
//		token.setRememberMe(true);
//		try {
//			currentUser.login(token);
//			JSONObject record = new JSONObject();
//			Users user = new Users();
//			user.set("login_type", loginType.toUpperCase());
//			user.set(loginType.toLowerCase() + "_open_id", openId);
//			if ("WX".equals(loginType.toUpperCase())) {
//				user = userService.getWXUserInfo(openId, user);
//			}
//			record.put("login_user", doAfterLogin(user));
//			returnJson(record);
//		} catch (UnknownAccountException e) {
//			returnJson(Errcode.FAIL, e.getMessage());
//		} catch (DisabledAccountException e) {
//			returnJson(Errcode.FAIL, e.getMessage());
//		} catch (AuthenticationException e) {
//			returnJson(Errcode.FAIL, e.getMessage());
//		} catch (Exception e) {
//			returnJson(Errcode.FAIL_INNER_ERROR, e.getMessage());
//		}
//	}

	/**
	 * 
	 */
	@Before(UserValidator.class)
	public void registerAndBind() {
		String phone = getPara("phone");
		String loginType = getPara("login_type");
		String openId = getPara("open_id");
		String smsCode = getPara("sms_code");
		String referer = getPara("referer");

		if (!SmsService.verifySmsCode(phone, smsCode)) {
			returnJson(Errcode.FAIL, "验证码已过期");
			return;
		}
		if (UserService.queryPhoneIsExist(phone)) {
			returnJson(Errcode.FAIL, "手机号已注册");
			return;
		}
		String refererId = "";
		User us = UserService.queryByUserId(referer);
		if (us != null) {
			refererId = us.getStr("user_id");
//			serviceId = referR.getStr("service_id");  
		}

		User user = new User();
		user.set("user_id", IdGenerator.generateUserId());
		user.set("phone", phone);
		user.set("login_type", loginType);
		user.set(loginType.toLowerCase() + "_open_id", openId);
		user.set("user_type", UserType.COMMON.toString());
		user.set("referer_id", refererId);
		user.set("create_time", new Date());
		if ("WX".equals(loginType.toUpperCase())) {
			user = userService.getWXUserInfo(openId, user);
		}
		if (userService.saveUser(user)) {
			Subject currentUser = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(phone, loginType);
			token.setRememberMe(true);
			try {
				currentUser.login(token);
				JSONObject record = new JSONObject();
				record.put("login_user", doAfterLogin(user));	
				//新增推荐记录
				EventKit.post(new RegisterEvent(user));
				returnJson(record);
			} catch (UnknownAccountException e) {
				returnJson(Errcode.FAIL, e.getMessage());
			} catch (DisabledAccountException e) {
				returnJson(Errcode.FAIL, e.getMessage());
			} catch (AuthenticationException e) {
				returnJson(Errcode.FAIL, e.getMessage());
			} catch (Exception e) {
				returnJson(Errcode.FAIL_INNER_ERROR, e.getMessage());
			}
		} else {
			returnJson(Errcode.FAIL_INNER_ERROR, "注册失败，内部错误");
		}
	}

	/*
	 * 注册
	 */
	@Before(UserValidator.class)
	public void register() {
		String phone = getPara("phone");
		String password = getPara("password").toLowerCase();
		String smsCode = getPara("sms_code");
		String referer = getPara("referer");

		if (!SmsService.verifySmsCode(phone, smsCode)) {
			returnJson(Errcode.FAIL, "验证码已过期");
			return;
		}
		if (UserService.queryPhoneIsExist(phone)) {
			returnJson(Errcode.FAIL, "手机号已注册");
			return;
		}
		String refererId = "";
		String serviceId = "";  //客服号
		//如果推荐人没攒油没有推荐资格
		if (RegularUtil.isPhoneNumber(referer)) {
			Record referR = userService.queryUserByPhone(referer);
			if (referR != null) {
				if (referR.getInt("user_rank")>1) {
					refererId = referR.getStr("user_id");
					serviceId = referR.getStr("service_id");  //继承推荐人客服号
				}
			}
		}

		User user = new User();
		user.set("user_id", IdGenerator.generateUserId());
		user.set("phone", phone);
		user.set("password", DigestUtils.shaHex(password));
		user.set("referer_id", refererId);
		user.set("service_id", serviceId);
		user.set("user_type", UserType.COMMON.toString());
		user.set("create_time", new Date());
		if (userService.saveUser(user)) {
			Subject currentUser = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(phone, password);
			token.setRememberMe(true);
			try {
				currentUser.login(token);
				JSONObject record = new JSONObject();
				user = null;
				record.put("login_user", doAfterLogin(user));
				
				//注册事件处理
				EventKit.post(new RegisterEvent(user));
				returnJson(record);
			} catch (UnknownAccountException e) {
				returnJson(Errcode.FAIL, e.getMessage());
			} catch (DisabledAccountException e) {
				returnJson(Errcode.FAIL, e.getMessage());
			} catch (AuthenticationException e) {
				returnJson(Errcode.FAIL, e.getMessage());
			} catch (Exception e) {
				returnJson(Errcode.FAIL_INNER_ERROR, e.getMessage());
			}
		} else {
			returnJson(Errcode.FAIL_INNER_ERROR, "注册失败，内部错误");
		}
	}

	/**
	 * 找回密码
	 */
	public void findPassword() {
		String phone = getPara("phone");
		String newPassword = getPara("new_password");
		String smsCode = getPara("sms_code");

		if (StrKit.isBlank(newPassword)) {
			returnJson(Errcode.FAIL, "请输入新密码");
			return;
		}
		if (!SmsService.verifySmsCode(phone, smsCode)) {
			returnJson(Errcode.FAIL, "验证码已过期");
			return;
		}
		userService.updatePassword(phone, newPassword);
		returnJson(Errcode.SUCC, "密码重置成功！");
	}
	
	@Before(UserValidator.class)
	public void joinus() {
		Record record = new Record();
		record.set("name", getPara("name"));
		record.set("phone", getPara("phone"));
		record.set("email", getPara("email"));
		record.set("position", getPara("position"));
		record.set("create_time", new Date());
		record.set("description", getPara("description"));
		boolean flag = Db.save("t_joinus", record);
		if (flag) {
			returnJson(Errcode.SUCC, "成功！");
		} else {
			returnJson(Errcode.FAIL, "添加信息失败");
		}
	}
	
	
}
