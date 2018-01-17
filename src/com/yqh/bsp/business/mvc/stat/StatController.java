package com.yqh.bsp.business.mvc.stat;

import java.util.List;

import org.eclipse.jetty.server.Authentication.SendSuccess;

import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Record;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;

@Action(controllerKey="/stat",viewPath="/")
@Clear
public class StatController extends BaseController {
	private static StatService statService = new StatService();
		
	public void saveOil() {
		List<Record> list = statService.statSaveOil();
		if (list.size()>0) {
			StringBuilder sb = new StringBuilder();
//			sb.append("<html><body>");
			String comboName = "";
			for(Record rd:list) {
				int comboId = rd.getInt("combo_id");
				if(comboId == 1){
					comboName = "经济套餐500L";
				} else if(comboId == 2){
					comboName = "体验套餐100L";
				} else {
					comboName = "专属套餐1500L";
				}
				sb.append(rd.getStr("date_time")+", "+comboName+", 订单数: "+rd.getLong("num")+" <br />");
			}
//			sb.append("</body></html>");
			setAttr("content", sb.toString());
			renderJsp("view/stat/saveOil.jsp");
//			renderText(sb.toString());
		} else {
			renderText("无攒油订单");
		}
	}
	

}
