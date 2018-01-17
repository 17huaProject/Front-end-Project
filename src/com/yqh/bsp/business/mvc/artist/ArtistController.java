package com.yqh.bsp.business.mvc.artist;

import com.alibaba.fastjson.JSONObject;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.business.mvc.faverate.FaverateService;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.enums.FaverateType;
import com.yqh.bsp.common.model.Artist;
import com.yqh.bsp.common.model.UserFaverate;

@Action(controllerKey="/artist")
public class ArtistController extends BaseController {
	private static ArtistService artistService = new ArtistService();

	//查看单个明细
	public void show() {
		int artistId = getParaToInt("id");
		Artist artist = artistService.query(artistId);
		
		String userId = "";
		int is_faverate = 0; //未收藏
		UserFaverate faverate = new UserFaverate();
		LoginUser lUser = getSessionAttr("loginUser");
		if (lUser != null){
			userId = lUser.getUserId();
			faverate = FaverateService.queryUserFaverate(userId, FaverateType.ARTIST,artistId+"");
			if (faverate != null) {
				is_faverate = 1;
			} else {
				faverate = new UserFaverate();
			}
		} 
		JSONObject json = new JSONObject();
		json.put("artist", artist.getMap());
		json.put("is_faverate", is_faverate);
		json.put("faverate", faverate.getMap());
		
		returnJson(json);
	}
}
