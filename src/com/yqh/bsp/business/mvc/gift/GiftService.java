package com.yqh.bsp.business.mvc.gift;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.enums.GiftStatus;
import com.yqh.bsp.common.model.Gift;
import com.yqh.bsp.common.model.UserCustom;

public class GiftService extends BaseService {
	
	public List<Gift> queryList(String userId,String sdate,String edate,int start,int size) {
		StringBuilder sql = new StringBuilder();
		sql.append("select gift_id,g.user_id,gift_name,create_time,price,number,order_amount,gift_status,p.nickname receiver_nickname,received_time from t_gifts g left join t_user_profile p on g.receiver_id=p.user_id where g.user_id='");
		sql.append(userId).append("' and gift_status <> 'UNPAID'");

		if(StrKit.notBlank(sdate)) {
			sql.append(" and create_time>='").append(sdate).append("'");
		}
		if(StrKit.notBlank(edate)) {
			sql.append(" and create_time<='").append(edate).append(" 23:59:59'");
		}
		sql.append(" order by create_time desc limit ?,?");
		return Gift.dao.find(sql.toString(),start,size);
	}
	
	
	public Gift queryOne(String userId,String giftId) {
		return Gift.dao.findFirst("select gift_id,g.user_id,gift_name,sender,content,receiver,create_time,price,number,order_amount,gift_status,p.nickname receiver_nickname,received_time from t_gifts g left join t_user_profile p on g.user_id=p.user_id where gift_id=? and g.user_id=?", giftId,userId);
	}
	
	@Before(Tx.class)
	public Gift checkGift(String userId,String giftId,String receiverId) throws Exception {
		Gift gift = Gift.dao.findFirst("select gift_id,user_id,gift_name,sender,content,receiver,create_time,price,number,order_amount,gift_status from t_gifts where gift_id=? and user_id=?", giftId,userId);
		if (gift != null ) {
			String giftStatus = gift.getStr("gift_status");
			if (giftStatus.equals(GiftStatus.PAID.toString())) {
				gift.set("receiver_id", receiverId);
				gift.set("gift_status", GiftStatus.RECEIVED.toString());
				gift.set("received_time", new Date());
				gift.update();
			} else {
				return null;
			}
		} 
		return gift;
	}
	

}
