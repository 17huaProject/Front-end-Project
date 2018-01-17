package com.yqh.bsp.business.mvc.faverate;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.enums.FaverateType;
import com.yqh.bsp.common.model.UserFaverate;

public  class FaverateService extends BaseService {

	public List<UserFaverate>  queryUserFaverate(String userId,FaverateType fType ) {
		String sql = "";
		switch (fType) {
		case EVENT:
			sql = "select f.id faverate_id,e.id event_id,e.event_name,e.event_time,d.event_desc,e.event_status,f.create_time,v.address,a.big_img,a.easy_level from t_user_faverate f left JOIN (t_events e left join t_venues v on e.venue_id=v.id left JOIN t_arts a on e.art_id=a.id left join t_event_detail d on e.id=d.event_id ) on f.related_id=e.id where user_id=? ";
			break;
		case ARTIST:
			sql = "select f.id faverate_id,a.id artist_id,a.artist_name,a.artist_level,a.big_img as artist_avatar,a.artist_desc,f.create_time from t_user_faverate f left JOIN t_artists a  on f.related_id=a.id where user_id=? ";
			break;
		case VENUE:
			sql = "select f.id faverate_id,v.id venue_id,v.venue_name,v.address,v.venue_desc,v.big_img,f.create_time from t_user_faverate f left JOIN t_venues v  on f.related_id=v.id where user_id=? ";
			break;
		}

		if (fType != null) {
			sql = sql + " and f.type='"+fType.toString()+"'";
		}
		return UserFaverate.dao.find(sql,userId);
	}
	
	public static UserFaverate  queryUserFaverate(String userId,FaverateType fType,String relatedId ) {
		String sql = "select id,type,related_id,create_time from t_user_faverate where user_id=? and type=? and related_id=?";
		return UserFaverate.dao.findFirst(sql,userId,fType.toString(),relatedId);
	}
	
	
	public boolean delete(int id) {
		return UserFaverate.dao.deleteById(id);
	}
	
	public static int deleteUserFaverate(String userId,FaverateType fType,String relatedId) {
		return Db.update("delete from t_user_faverate where  user_id=? and type=? and related_id=?",userId,fType.toString(),relatedId);
	}
	
	
	public UserFaverate save(String userId,String type,String relateId) {
		UserFaverate faverate = new UserFaverate();
		faverate.set("user_id", userId);
		faverate.set("type", type);
		faverate.set("related_id", relateId);
		boolean flag = faverate.save();
		if (flag) {
			return faverate;
		} else {
			return null;
		}
	}

}
