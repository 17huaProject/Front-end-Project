package com.yqh.bsp.business.mvc.goods;

import java.util.List;

import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.Goods;

public class GoodsService extends BaseService {

	
	public List<Goods> query(int start,int size) {
		return Goods.dao.find("SELECT id,goods_no,goods_img,name,count,price,sale_price,attribute FROM t_goods  where goods_status=1 and display=1 and deleted=0 limit ?,?",start,size);
	}
	
	
	public Goods queryOne(int goodsId) {
		return Goods.dao.findFirst("SELECT id,goods_no,goods_img,name,count,price,sale_price,attribute,content,sale_count FROM t_goods  where id=? and goods_status=1 and display=1",goodsId);
	}
	
	public static Goods queryGoods(int goodsId) {
		return Goods.dao.findFirst("select * from  t_goods  where id=? and goods_status=1 and display=1 and count>0",goodsId);
	}
}
