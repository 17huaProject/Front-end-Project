package com.yqh.bsp.business.mvc.article;

import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.common.model.Article;
import com.yqh.bsp.common.template.ContantTemplate;

@Action(controllerKey="/article")
public class ArticleController extends BaseController {
	private static ArticleService articleService = new ArticleService();

	//文章列表
	public void index() {
		String catId = getPara("cat_id");          //文章分类
		if(StrKit.isBlank(catId)) {
			catId = "1";
		}
		
		Integer pageNo =  getParaToInt("page_no"); //开始页数
		Integer pageSize =  getParaToInt("page_size"); //每页显示
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = ContantTemplate.PAGESIZE;
		}
		returnJson(articleService.query(catId,(pageNo-1)*pageSize,pageSize));
	}
	//查看单个明细
	public void show() {
		String articleId = getPara("id");
		if (StrKit.isBlank(articleId)) {
			returnJson(Errcode.FAIL, "请输入id号");
			return;
		}
		Article article = articleService.queryById(articleId);
		if (article != null) {
			article.set("hits", article.getInt("hits")+1);
			article.update();
		}
		returnJson(article);
	}
}
