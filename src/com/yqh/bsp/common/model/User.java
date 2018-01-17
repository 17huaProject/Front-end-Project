package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;

@Table(tableName="t_users",pkName="user_id")
public class User extends BaseModel<User> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 993532188096173124L;
	public static User dao = new User();
	private long userId;
	private String phone;
	private String password;
	private UserProfile userProfile;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public UserProfile getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

}
