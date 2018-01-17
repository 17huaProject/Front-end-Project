package com.yqh.bsp.business.mvc.custom;

import java.util.List;

import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.UserCustom;

public class CustomService extends BaseService {
	
	public List<UserCustom> queryUserCustom(String userId) {
		return UserCustom.dao.find("select c.id,contact,phone,custom_type,est_date,est_num,c.create_time,city,status,trans_time,trans_desc,event_id,e.event_status from t_user_custom c LEFT JOIN t_events e on c.event_id=e.id where c.user_id=? order by c.id desc",userId);
	}
	
	

}
