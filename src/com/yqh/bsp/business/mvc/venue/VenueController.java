package com.yqh.bsp.business.mvc.venue;

import com.alibaba.fastjson.JSONObject;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.business.mvc.faverate.FaverateService;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.enums.FaverateType;
import com.yqh.bsp.common.model.UserFaverate;
import com.yqh.bsp.common.model.Venue;

@Action(controllerKey="/venue")
public class VenueController extends BaseController {
	private static VenueService venueService = new VenueService();
	//查看单个明细
	public void show() {
		Integer venueId = getParaToInt("id");
		if (venueId == null) {
			returnJson(Errcode.FAIL, "请传入id参数");
			return;
		}
		String userId = "";
		int is_faverate = 0; //未收藏
		UserFaverate faverate = new UserFaverate();
		Venue venue = venueService.query(venueId);
		LoginUser lUser = getSessionAttr("loginUser");
		if (lUser != null){
			userId = lUser.getUserId();
			faverate = FaverateService.queryUserFaverate(userId, FaverateType.VENUE,venue.get("id").toString());
			if (faverate != null) {
				is_faverate = 1;
			} else {
				faverate = new UserFaverate();
			}
		} 
		JSONObject json = new JSONObject();
		json.put("venue", venue.getMap());
		json.put("is_faverate", is_faverate);
		json.put("faverate", faverate.getMap());
		
		returnJson(json);
	}
	
	
}
