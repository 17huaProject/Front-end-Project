package com.yqh.bsp.business.mvc.art;

import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;

@Action(controllerKey="/art")
public class ArtController extends BaseController {
	private static ArtService artService = new ArtService();

	//查看单个明细
	public void show() {
		int artId = getParaToInt("id");
		returnJson(artService.query(artId));
	}
}
