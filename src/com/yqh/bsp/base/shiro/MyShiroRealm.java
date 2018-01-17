package com.yqh.bsp.base.shiro;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

import com.jfinal.kit.StrKit;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.model.User;

/**
 * 自实现用户与权限查询. 演示关系，密码用明文存储，因此使用默认 的SimpleCredentialsMatcher.
 */
public class MyShiroRealm extends AuthorizingRealm {
	
	private Logger log = Logger.getLogger(MyShiroRealm.class);

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) authcToken;
		String username = upToken.getUsername();
		if (username == null) {
			log.warn("用户名不能为空");
			throw new AccountException("用户名不能为空");
		}

		User user = null;
		try {
			user = User.dao.findFirst("select t.*,u.nickname,u.avatar from t_users t left join t_user_profile u on t.user_id=u.user_id where phone=?",username);
		} catch(Exception ex) {
			throw new UnknownAccountException("用户不存在");
		}
		if (user == null) {
		    throw new UnknownAccountException("用户不存在");
		}
		if(user.getInt("status") == 0) {
		    throw new DisabledAccountException("用户被禁止使用");
		}
		if(user.getInt("status") == 9) {
			throw new DisabledAccountException("用户账号已注销");
		}

		String password =  String.valueOf(upToken.getPassword());
		if ("WX".endsWith(password)) {
			//使用openid进行登录
		} else {
			String pwd = user.getStr("password");
			if (StrKit.isBlank(pwd)) {
				 throw new AuthenticationException("您是微信用户，密码通过忘记密码设置");
			}
			String enpwd = DigestUtils.shaHex(password);
			
			if(!enpwd.equals(pwd)) {
			    throw new AuthenticationException("密码不正确");
			}
		}
		
		LoginUser lUser = new LoginUser();
		lUser.setUserId(user.getPKValue());
		lUser.setPhone(username);
		lUser.setNickname(user.getStr("nickname"));
		lUser.setBalance(user.getBigDecimal("balance"));
		lUser.setLoginNum(user.getInt("login_num"));
		lUser.setUserType(user.getStr("user_type"));
		lUser.setLastTime(user.getDate("last_time"));
		lUser.setAvatar(user.getStr("avatar"));
		lUser.setNickname(user.getStr("nickname"));
		lUser.setWxOpenId(user.getStr("wx_open_id"));
		
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute("loginUser", lUser);
		
//		log.info("用户【" + username + "】登录成功");
//		byte[] salt = EncodeUtils.hexDecode(user.getStr("salt"));
//		ShiroPrincipal subject = new ShiroPrincipal(user);
//		List<String> authorities = User.dao.getAuthoritiesName(user.getInt("id"));
//		List<String> rolelist = User.dao.getRolesName(user.getInt("id"));
//		subject.setAuthorities(authorities);
//		subject.setRoles(rolelist);
//		subject.setAuthorized(true);
		
		return new SimpleAuthenticationInfo(username, password, getName());
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		int userId = (int) principals.fromRealm(getName()).iterator().next();
		User user = User.dao.findById(userId);
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			
			return info;
		} else {
			return null;
		}
	}

}