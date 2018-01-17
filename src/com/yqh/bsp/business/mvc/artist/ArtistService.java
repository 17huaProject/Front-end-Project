package com.yqh.bsp.business.mvc.artist;

import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.Artist;

public class ArtistService extends BaseService {
	
	public Artist query(int artistId) {
		return Artist.dao.findFirst("SELECT id,artist_name,avatar,big_img,artist_level,artist_desc,city,content FROM t_artists t where id=?",artistId);
	}

}
