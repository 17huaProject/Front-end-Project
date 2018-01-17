package com.yqh.bsp.business.mvc.event;

import net.dreamlu.event.EventKit;

import org.apache.solr.common.SolrDocumentList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.business.mvc.faverate.FaverateService;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.enums.EventStatus;
import com.yqh.bsp.common.enums.FaverateType;
import com.yqh.bsp.common.event.ViewEvent;
import com.yqh.bsp.common.model.Event;
import com.yqh.bsp.common.model.EventViewer;
import com.yqh.bsp.common.model.UserFaverate;
import com.yqh.bsp.common.solr.SolrTemplate;
import com.yqh.bsp.common.template.ContantTemplate;

@Action(controllerKey="/event")
public class EventController extends BaseController {
	private static EventService eventService = new EventService();
	private static SolrTemplate solrTemplate  = new SolrTemplate();
	//查看活动明细
	public void show() {
		Integer eventId = getParaToInt("id");
		if (eventId == null) {
			returnJson(Errcode.FAIL,"请传入活动ID");
		}
		Event event = eventService.query(eventId);
		String userId = "";
		int is_faverate = 0; //未收藏
		UserFaverate faverate = new UserFaverate();
		LoginUser lUser = getSessionAttr("loginUser");
		if (lUser != null){
			userId = lUser.getUserId();
			if (event == null) {
				int a = FaverateService.deleteUserFaverate(userId, FaverateType.EVENT,eventId+"");
				if (a > 0) {
					returnJson(Errcode.FAIL, "该活动已失效或删除");
					return;
				}
			}
			
			faverate = FaverateService.queryUserFaverate(userId, FaverateType.EVENT,event.get("id").toString());
			if (faverate != null) {
				is_faverate = 1;
			} else {
				faverate = new UserFaverate();
			}
			EventViewer viewer = new EventViewer();
			viewer.set("user_id", userId);
			viewer.set("event_id", event.get("id"));
			EventKit.post(new ViewEvent(viewer));
		} 
		JSONObject json = new JSONObject();
		json.put("event", event.getMap());
		json.put("is_faverate", is_faverate);
		json.put("faverate", faverate.getMap());
		
		returnJson(json);

	}
	
	//search
	public void search() {
		String cityCode = getPara("city_code");  
		String p = getPara("p"); //纬、经度 ，用逗号分割
		Integer d = getParaToInt("d"); //距离
		String stext = getPara("stext");  //查询文字
		String start = getPara("start");
		String end   = getPara("end");
		String num =  getPara("num"); //人数
		Integer pageNo =  getParaToInt("page_no"); //开始页数
		Integer pageSize =  getParaToInt("page_size"); //每页显示 
		//sortkey|0,    0=从小到大  1=从大到小   sortKey  distance/closing_time
		String sort = getPara("sort");  //排序 
		
//		seniorQuery(String[] field, String[] value, int start, int count, String[] sortfield, Boolean[] flag, Boolean hightlight)
		String [] field = new String[5];
		String [] value = new String[5];
		int i =0;
		if (StrKit.notBlank(p) && p.indexOf(".")> 0) {
			if (d == null || d < 1) {
				d = 20 ;
			} 
			field[i] = "position";
			value[i] = p+"|"+d.toString();
			i++;
		}

		if (StrKit.notBlank(stext)) {
			field[i] = "stext";
			value[i] = stext;
			i++;
		}
		if (StrKit.notBlank(cityCode)) {
			field[i] = "city_code";
			value[i] = cityCode;
			i++;
		}
		if (StrKit.notBlank(num)) {
			field[i] = "left_num";
			value[i] = "["+num+" TO *]";
			i++;
		}
			
		if (StrKit.isBlank(start)) {
			start = "NOW-1DAY";
		} else {
			start = start+"T00:00:00Z";
		}
		if (StrKit.isBlank(end)) {
			end = "*";
		} else {
			end = end +"T23:59:59.999Z";
		}
		
		field[i] = "event_time";
		value[i] = "["+start+" TO "+end+"]";
		
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = ContantTemplate.PAGESIZE;
		}
		String[] sortfield = null;
		Boolean[] flag = null;
		if (StrKit.notBlank(sort)) {
			String[] sts = sort.split(",");
			int len = sts.length;
			sortfield = new String[len];
			flag = new Boolean[len];
			for(int j=0;j<len;j++) {
				String[] tmps = sts[j].split("\\|");
				sortfield[j] = tmps[0];
				flag[j] = Boolean.parseBoolean(tmps[1]);
			}
		} else {
			sortfield = new String[]{"distance"};
			flag = new Boolean[]{false};
		}
		long totalCount = 0;
		SolrDocumentList resp = solrTemplate.seniorQuery(field, value, (pageNo-1)*pageSize, pageSize, sortfield, flag, false);
		if (pageNo == 1) {
			totalCount = resp.getNumFound();
		}
		returnJson(resp,totalCount);
	}
	
	
	public void showPaidUser() {
		int eventId = getParaToInt("id");
		Event event = Event.dao.findById(eventId);
		if (EventStatus.PRESALE.toString().equals(event.getStr("event_status"))) {
			returnJson(new JSONArray());
		} else {
			returnJson(eventService.queryPaidUser(eventId));
		}
	}
}
