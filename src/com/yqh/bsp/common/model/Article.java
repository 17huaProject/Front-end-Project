package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_articles" ,pkName="article_id")
public class Article extends BaseModel<Article> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7523413704459739905L;
	public static Article dao = new Article();

}
