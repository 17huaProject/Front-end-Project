package com.yqh.bsp.business.mvc.account;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.User;
import com.yqh.bsp.common.model.UserBalanceRecord;
import com.yqh.bsp.common.model.UserProfile;

public class AccountService extends BaseService {
	
	/**
	 * 验证该手机用户是否存在
	 * @param phone 手机号码
	 * @return 0：不存在  1：存在
	 * @author bibo_qiu
	 */
	public int checkUser(String phone){
		int count = 0;
		User user = User.dao.findFirst("select id from t_user where mobile = ?",phone);
		if(user != null)count = 1;
		return count;
	}
	
	public boolean checkHasIdcard(String idcard) {
		long a =  Db.queryLong("select count(1) from t_user where idcard=?",idcard);
		if (a>0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 我的账户 所需信息
	 * @param phone
	 * @return record （包含了 字段： 手机号码 、用户余额、红包个数、未读消息数）
	 * @author bibo_qiu
	 */
	public Map<String ,Object> queryUserAccount(String userId){
		User record = User.dao.findFirst("select u.user_id,u.phone,u.balance from t_users u where u.user_id = ?", userId);
		Map<String,Object> map = record.getMap();
		//查询未读消息数
//		Record recordMsg = Db.findFirst("select count(1) as unread from t_msg_box where is_read = 0 and to_id = ? ", userId);
//		map.put("unread", recordMsg.getLong("unread"));
//		map.put("unread", 0);
		//查询未使用并且未过期的红包
		Long coupon = Db.queryLong("select count(1) from t_user_coupon where user_id = ? and status = 0 and end_time > ?", userId,new Date());
		map.put("coupon_num", coupon);
		return map;
	}
	
	public List<UserBalanceRecord> queryBalanceRecord(String userId,String type,int pageNo,int pageSize) {
		String url = "select record_id,type,money,account,description,create_time,trans_order_id from t_user_balance_record where user_id=? ";
		if (type.length()> 3) {
			url =url+ " and type='"+type+"' ";
		}
		url = url + " order by record_id desc limit ?,?";
		return UserBalanceRecord.dao.find(url, userId,(pageNo-1)*pageSize,pageSize);
	}
	
	/**
	 * 我的账户 ——个人资料
	 * @return record
	 * @author bibo_qiu
	 */
	public User queryProfile(String userId){
		User user = User.dao.findFirst("select phone,p.nickname,p.province,p.city,p.avatar,p.gender from t_users u LEFT JOIN t_user_profile p ON u.user_id = p.user_id where  u.user_id = ? and u.status = 1",userId);
		return user;
	}
	
	public  User querySecurity(String userId) {
		User user = User.dao.findFirst("select u.user_id,u.mobile,ub.balance,u.realname,idcard,idcard_flag,pay_pwd_flag,bank_flag,oil_flag,bank_id,bank_card,card_type,oil_card_type,oil_card  from t_user u LEFT JOIN t_user_upay ub ON u.user_id = ub.user_id where u.user_id = ?", userId);
		return user;
	}
	
	public static float queryBalance(String userId) {
		User user = User.dao.findFirst("select user_id,balance from t_users  where user_id = ?", userId);
		float balance = 0;
		if (user != null) {
			balance = user.getBigDecimal("balance").floatValue();
		} 
		return balance;
	}
	
	public static int queryUserRank(String userId) {
		User user = User.dao.findFirst("select user_rank from t_user where user_id=?",userId);
		if (user != null) {
			return user.getInt("user_rank");
		} else {
			return 0;
		}
	}
	
	public static String queryUserFlag(String userId){
		User user = User.dao.findFirst("select user_flag from t_user where user_id=?",userId);
		if (user != null) {
			return user.getStr("user_flag");
		} else {
			return "";
		}
	}
	
	
	/**
	 * 我的账户 ——修改个人信息
	 */
	public void updateProfile(User user,UserProfile userInfo){
		if (user != null) {
			user.update();
		}
		if (userInfo != null) {
			userInfo.update();
		}
	}
	
	
	public int updateIdcard(String realName,String idcard,String userId){
		return Db.update("update t_user set realname = ? , idcard = ?,idcard_flag=1 where status = 1 and user_id = ?", realName,idcard,userId);
	}
	
	public Long checkOldPassword(String userId,String oldPassword){
		return Db.queryLong("select count(1) from t_user where user_id = ? and password = ? ",userId,DigestUtils.shaHex(oldPassword));
	}
	
	public int updatePassword(String userId,String newPassword){
		return Db.update("update t_user set password = ? where user_id = ?", DigestUtils.shaHex(newPassword),userId);
	}
	
	
	public String queryMobile(String userId){
		Record record = Db.findFirst("select mobile from t_user where user_id=?",userId);
		if (record != null) {
			return record.getStr("mobile");
		} else {
			return "";
		}
	}
	
	
}
