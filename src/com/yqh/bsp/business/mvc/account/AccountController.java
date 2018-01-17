package com.yqh.bsp.business.mvc.account;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.business.mvc.config.ConfigService;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.interceptor.LoginedAuthInterceptor;
import com.yqh.bsp.common.model.User;
import com.yqh.bsp.common.model.UserBalanceRecord;
import com.yqh.bsp.common.model.UserProfile;
import com.yqh.bsp.utils.RegularUtil;

@Action(controllerKey = "/account")
@Before(LoginedAuthInterceptor.class)
public class AccountController extends BaseController {

	private static AccountService accountService = new AccountService();
	

	/**
	 * 功能模块：我的账户 所需信息
	 * 功能描述：获取账户和u付的信息
	 * @param phone
	 * @return record （包含了 字段： 手机号码 、用户余额、优惠券个数、未读消息数）
	 * @author 
	 */
	public void show(){
		LoginUser lUser = getSessionAttr("loginUser");
		returnJson(accountService.queryUserAccount(lUser.getUserId()));
	}
	
	/**
	 * 功能模块：我的账户_个人资料
	 * 功能描述：获取个人资料页面相关数据
	 * @return record
	 * @author 
	 */
	public void showProfile(){
		LoginUser lUser = getSessionAttr("loginUser");
		User user = accountService.queryProfile(lUser.getUserId());
		returnJson(user);
	}
	
	/**
	 * 功能模块：我的账户 _修改个人信息
	 * 功能描述：修改个人资料
	 * @author 
	 */
	public void updateProfile(){
		LoginUser lUser = getSessionAttr("loginUser");
		String nickname = getPara("nickname");
		String photo = getPara("photo");
		String province = getPara("province");
		String city = getPara("city");
		String sex = getPara("sex");
		
		String userId = lUser.getUserId();
	
		try {
			if (StrKit.notBlank(nickname) || StrKit.notBlank(photo)) {
				User user = new User();
				user.set("user_id", userId);
				if (StrKit.notBlank(nickname)) {
					nickname = URLDecoder.decode(nickname, "utf-8");
					user.set("nickname", nickname);
				}
				if(StrKit.notBlank(photo)) {
					user.set("photo", photo);
				}
				accountService.updateProfile(user, null);
			} 
			
			if (StrKit.notBlank(province) || StrKit.notBlank(city) || StrKit.notBlank(sex)) {
				UserProfile info = new UserProfile();
				info.set("user_id", userId);
				if (StrKit.notBlank(province)) {
					province = URLDecoder.decode(province, "utf-8");
					info.set("province", province);
				}
				if (StrKit.notBlank(city)) {
					city = URLDecoder.decode(city, "utf-8");
					info.set("city", city);
				}
				if (StrKit.notBlank(sex)) {
					info.set("sex", sex);
				}
				accountService.updateProfile(null, info);
			}
			returnJson(Errcode.SUCC, "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			returnJson(Errcode.FAIL, "系统错误");
			return;
		}
	}
	
	/**
	 * 功能模块：我的账户 _账户与安全
	 * 功能描述：获取账户安全页面相关数据
	 * @param userId 用户ID
	 * @return record
	 * @author bibo_qiu
	 */
	public void showSecurity(){
		LoginUser lUser = getSessionAttr("loginUser");
		User user = accountService.querySecurity(lUser.getUserId());
		Map map = user.getMap();
		if (user.getInt("bank_flag") == 1) {
			map.put("bank_name", ConfigService.queryBankName(user.getInt("bank_id")));
		} else {
			map.put("bank_name", "");
		}
		returnJson(map);
	}
	
	/**
	 * 功能模块：我的账户_账户与安全_实名认证
	 * 功能描述：实名认证
	 * @param userId 用户ID
	 * @return record
	 * @author bibo_qiu
	 */
	public void bindIdcard(){
		try {
			String realName = getPara("realname");
			String idcard = getPara("idcard");
			if(StrKit.isBlank(realName) || StrKit.isBlank(idcard)){
				returnJson(Errcode.FAIL, "姓名和身份证不能为空");
				return;
			} else {
				if (RegularUtil.idcardVerify(idcard) == 1) { //身份证校验不通过
					returnJson(Errcode.FAIL, "请输入正确的身份证号");
					return; 
				}
				if (realName.length()<2 ) {
					returnJson(Errcode.FAIL, "请输入正确姓名");
					return; 
				} else {
					realName = URLDecoder.decode(realName, "utf-8");
				}
			}
			if (accountService.checkHasIdcard(idcard)) {
				returnJson(Errcode.FAIL, "该身份证已被使用");
				return;
			}
			LoginUser lUser = getSessionAttr("loginUser");
			int ret = accountService.updateIdcard(realName, idcard, lUser.getUserId());
			if(ret > 0){
//				lUser.setIdcardFlag(1);
				setSessionAttr("loginUser", lUser);
				returnJson(Errcode.SUCC, "实名认证成功");
				return;
			}else{
				returnJson(Errcode.SUCC, "实名认证失败");
				return;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			returnJson(Errcode.FAIL, "系统错误");
			return;
		}
	}
	
	/**
	 * 功能模块：功能模块：我的账户_账户与安全_密码修改
	 * 功能描述：修改密码
	 * @param oldPassWord 原密码
	 * @param newPassWord 新密码
	 * @param newPassWord_two 重复密码
	 * @return 200 ：成功  400：原密码不能为空、原密码不正确、新密码不能为空、确认密码不能为空、两次输入的密码不一致
	 * 						 修改失败、系统错误
	 * @author bibo_qiu
	 */
	public void updatePassword(){
		String oldPassword = getPara("old_password");
		String newPassword = getPara("new_password");
		long ret = 0;
		if(StrKit.isBlank(oldPassword)){
			returnJson(Errcode.FAIL, "原密码不能为空");
			return;
		}
		if(StrKit.isBlank(newPassword)){
			returnJson(Errcode.FAIL, "新密码不能为空");
			return;
		}
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		ret = accountService.checkOldPassword(userId, oldPassword);
		if(ret <= 0){
			returnJson(Errcode.FAIL, "原密码不正确");
			return;
		}
		
		try {
			ret = accountService.updatePassword(userId,newPassword);
			if(ret > 0){
				returnJson(Errcode.SUCC, "新密码修改成功");
				return;
			}else{
				returnJson(Errcode.FAIL, "密码修改失败");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnJson(Errcode.FAIL, "系统错误");
			return;
		}
	}
	

	/**
	 * 查询用户画贝交易记录
	 */
	public void showMoneyDetail() {
		String type = getPara("type");
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		int pageNo = 1;
		int pageSize = 20;
		try{
			pageNo = getParaToInt("page_no");
			pageSize = getParaToInt("page_size");
			if(StrKit.isBlank(type)) {
				type = "0";
			}
			int a = Integer.parseInt(type);
		}catch (Exception e) {
			returnJson(Errcode.FAIL, "参数错误");
			return;
		}
			
		List<UserBalanceRecord> list = accountService.queryBalanceRecord(userId, type, pageNo, pageSize);
		returnJson(list);
	}
	
	
}
