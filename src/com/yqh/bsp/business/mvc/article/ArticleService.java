package com.yqh.bsp.business.mvc.article;

import java.util.List;

import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.Article;

public class ArticleService extends BaseService {
	
	public List<Article> query(String catId,int start,int size) {
		return Article.dao.find("select article_id, cat_id,title,header_img,introduce,open_type,link,hits,create_time from t_articles where cat_id=? and is_open=1 order by article_id desc limit ?,?",catId,start,size);
	}
	
	public Article queryById(String articleId) {
		return Article.dao.findFirst("select article_id, cat_id,title,header_img,introduce,content,open_type,author,link,hits,create_time from t_articles where article_id=? and is_open=1",articleId);
	}
	
	

}
