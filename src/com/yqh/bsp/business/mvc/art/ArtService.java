package com.yqh.bsp.business.mvc.art;

import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.Art;

public class ArtService extends BaseService {
	
	public Art query(int artId) {
		return Art.dao.findFirst("select id,art_name,event_num,easy_level,big_img,small_img,art_desc,content,note from t_arts where id=? and status=1",artId);
	}

}
