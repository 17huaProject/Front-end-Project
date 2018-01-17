package com.yqh.bsp.business.mvc.goods;

import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.common.template.ContantTemplate;

@Action(controllerKey="/goods")
public class GoodsController extends BaseController {
	private static GoodsService goodsService = new GoodsService();
	
	public void index() {
		Integer pageNo =  getParaToInt("page_no"); //开始页数
		Integer pageSize =  getParaToInt("page_size"); //每页显示
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = ContantTemplate.PAGESIZE;
		}
		
		returnJson(goodsService.query((pageNo-1)*pageSize,pageSize));
	}
	
	//查看单个明细
	public void show() {
		Integer goodsId = getParaToInt("id");
		if (goodsId == null ||goodsId<1 ) {
			returnJson(Errcode.FAIL, "请输入id号");
		} else {
			returnJson(goodsService.queryOne(goodsId));
		}
	}
	
	
	
	
}
