package com.yqh.bsp.business.mvc.faverate;

import com.jfinal.aop.Before;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.enums.FaverateType;
import com.yqh.bsp.common.interceptor.LoginedAuthInterceptor;
import com.yqh.bsp.common.model.UserFaverate;

@Action(controllerKey="/faverate")
@Before(LoginedAuthInterceptor.class)
public class FaverateController extends BaseController {
	private static FaverateService faverateService = new FaverateService();

	public void index() {
		String type = getPara("type");
		FaverateType fType = null;
		try {
		   fType = FaverateType.valueOf(type);
		   
		}catch(Exception e) {
			returnJson(Errcode.FAIL, "不支持的类型");
			return;
		}
		LoginUser lUser = getSessionAttr("loginUser");
		returnJson(faverateService.queryUserFaverate(lUser.getUserId(), fType));
		
	}
	
	public void delete() {
		int id = getParaToInt("id");
		boolean flag = faverateService.delete(id);
		if (flag) {
			returnJson(Errcode.SUCC);
		} else {
			returnJson(Errcode.ERR);
		}
	}
	
	public void add() {
		String type = getPara("type");
		String relatedId = getPara("related_id");
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		boolean flag = false;
		try {
			FaverateType fType = FaverateType.valueOf(type);
			UserFaverate faverate = faverateService.save(userId, fType.toString(), relatedId);
			
			if (faverate != null ) {
				returnJson(faverate);
			} else {
				returnJson(Errcode.FAIL,"您已收藏");
			}
		} catch (IllegalArgumentException e) {
			returnJson(Errcode.FAIL,"不支持的收藏类型");
			return;
		} catch (Exception e) {
			returnJson(Errcode.FAIL,"您已收藏，不能再次收藏");
			return;
		}
		
	}
	
}
