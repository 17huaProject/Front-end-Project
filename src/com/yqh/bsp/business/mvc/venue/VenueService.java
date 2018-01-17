package com.yqh.bsp.business.mvc.venue;

import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.UserFaverate;
import com.yqh.bsp.common.model.Venue;

public class VenueService extends BaseService {

//	public List<Venue>  queryVenueList(int pageNo,int pageSize) {
//		String url = "select order_id,user_id,amount,create_time,bank_card,fee,pay_time,pay_status from t_order_drawmoney where user_id=? ";
//		url = url + " order by create_time desc limit ?,?";
//		return Venue.dao.find(url,(pageNo-1)*pageSize,pageSize);
//	}
	
	public Venue query(int venueId) {
		return Venue.dao.findFirst("SELECT id,venue_name,big_img,city,address,latitude,longitude,venue_desc,content FROM t_venues  where id=? and status=1",venueId);
	}

}
