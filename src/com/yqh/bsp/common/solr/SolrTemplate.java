package com.yqh.bsp.common.solr;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.kit.StrKit;
import com.yqh.bsp.common.template.ContantTemplate;


public class SolrTemplate {

	/**  
	 * 简单查询  
	 * @param mQueryStr  
	 * @return query result  
	 */  
	public SolrDocumentList basicQuery(String queryStr,int start,int rows ) {  

	    try {  
	        HttpSolrClient httpSolrClient = SolrFactory.connect();  
	        SolrQuery query = new SolrQuery();  
	        //设定查询字段  
	        query.setQuery(queryStr);  
	        //指定返回结果字段  
	        query.setIncludeScore(true);  
//	        query.set("fl","id,name");  
	        //覆盖schema.xml的defaultOperator（有空格时用"AND"还是用"OR"操作逻辑），一般默认指定。必须大写  
	        query.set("q.op","AND");  
	        //设定返回记录数，默认为15条  
	        if (rows==0) {
	        	rows = ContantTemplate.PAGESIZE;
	        } 
	        //分页开始页数  
	        query.setStart(start);  
	        //设定返回记录数，默认为10条  
	        query.setRows(rows);  
	        //设定对查询结果是否高亮  
//	        query.setHighlight(false);  
//	        //设定高亮字段前置标签  
//	        query.setHighlightSimplePre("<span style=\"color:red\">");  
//	        //设定高亮字段后置标签  
//	        query.setHighlightSimplePost("</span>");  
//	        //设定高亮字段  
//	        query.addHighlightField("name");  
//	        //设定拼写检查  
//	        query.setRequestHandler("/spell");  
	        QueryResponse response = httpSolrClient.query(query);  
	        //获取bean  
		    //  List<Object> bean = response.getBeans(Object.class);  
	        SolrDocumentList list = response.getResults();  
	        return  list;  
	    } catch (SolrServerException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    return null;  
	}  

	 /**  
	  * 添加一个实体  
	  *  
	  * @param object  
	  */  
	 public void addBean(Object object) {  
	     try {  
	         HttpSolrClient httpSolrClient = SolrFactory.connect();  
	         httpSolrClient.addBean(object);  
	         httpSolrClient.commit();  
	     } catch (IOException e) {  
	         e.printStackTrace();  
	     } catch (SolrServerException e) {  
	         e.printStackTrace();  
	     }  
	 }  
	 
	// 查询  
	// SolrJ提供的查询功能比较强大，可以进行结果中查询、范围查询、排序等。  
	// 补充一下范围查询的格式：[star t TO end]，start与end是相应数据格式的值的字符串形式，“TO” 一定要保持大写！  
	/* 
	 * field 查询的字段名称数组 value 查询的字段名称对应的值 start 查询的起始位置 count 一次查询出来的数量 sortfield 
	 * 需要排序的字段数组 flag 需要排序的字段的排序方式如果为true 升序 如果为false 降序 hightlight 是否需要高亮显示 
	 * 如果要根据距离查询， field[0] =position
	 */  
	public SolrDocumentList seniorQuery(String[] field, String[] value, int start, int count, String[] sortfield, Boolean[] sortflag, Boolean hightlight) {  
	    // 检测输入是否合法  
		if (null == field || null == value || field.length != value.length) {
			return null;
		}

		SolrQuery query = null;
		try {
			// 初始化查询对象
			query = new SolrQuery();
			String poString = "{!geofilt sfield=position pt=%s  d=%s score=distance %s}";
			// 设置排序
			if (sortfield != null) {
				for (int i = 0; i < sortfield.length; i++) {
					if ("distance".equals(sortfield[i])) {
						if (sortflag[i]) {
							poString = "{!geofilt sfield=position pt=%s d=%s score=reciDistance %s}";
						} 
						query.addSort("score", SolrQuery.ORDER.asc);
					} else {
						if (sortflag[i]) {
							query.addSort(sortfield[i], SolrQuery.ORDER.desc);
						} else {
							query.addSort(sortfield[i], SolrQuery.ORDER.asc);
						}
					}
				}
			}
			if ("position".equals(field[0])) {
				String filter = "";
				String[] vqs= value[0].split("\\|");
				if (vqs.length>2) {
					filter = "filter:"+vqs[2];
				}
				//{!geofilt pt=31.209261,121.561955 sfield=position d=1 score=distance}
				query.setQuery(String.format(poString,vqs[0],vqs[1],filter));
			} else {
//				query.setQuery("*:*");
				query.setQuery(field[0] + ":" + value[0]);
			}
			
			for (int i = 1; i < field.length; i++) {
				if (StrKit.notBlank(field[i])) {
					query.addFilterQuery(field[i] + ":" + value[i]);
				}
			}
			// 设置起始位置与返回结果数
			
			// 设置高亮
			// if ( hightlight) {
			// query.setHighlight(true); // 开启高亮组件
			// query.addHighlightField("jobsName");// 高亮字段
			// query.setHighlightSimplePre("<font color=\"red\">");// 标记
			// query.setHighlightSimplePost("</font>");
			// query.setHighlightSnippets(1);// 结果分片数，默认为1
			// query.setHighlightFragsize(1000);// 每个分片的最大长度，默认为100
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		QueryResponse rsp = null;
		try {
			HttpSolrClient httpSolrClient = SolrFactory.connect();
			query.setIncludeScore(true);
			System.out.println(query.toString());
			if (start == 1) {
				rsp = httpSolrClient.query(query);
			} else {
				query.setStart(start);
				query.setRows(count);
				rsp = httpSolrClient.query(query);
			}
			
			SolrDocumentList list = rsp.getResults();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	 }  


	 /**  
	  * 添加简单索引  
	  *  
	  * @param map  
	  */  
	 public void addDoc(Map<String, Object> map) {  
	     try {  
	         HttpSolrClient httpSolrClient = SolrFactory.connect();  
	         SolrInputDocument document = new SolrInputDocument();  
	         document = SolrFactory.addFileds(map,document);  
	         UpdateResponse response = httpSolrClient.add(document);  
	         httpSolrClient.commit();  
	     } catch (SolrServerException e) {  
	         e.printStackTrace();  
	     } catch (IOException e) {  
	         e.printStackTrace();  
	     }  
	 }  

	// 删除索引  
	 // 据查询结果删除：  
	 public void deleteByQuery(String queryStr) {  
	     try {  
	    	 HttpSolrClient httpSolrClient = SolrFactory.connect();  
	         // 删除所有的索引  "jobsName:高级技术支持"
	    	 httpSolrClient.deleteByQuery(queryStr);  
	    	 httpSolrClient.commit();  
	     } catch (Exception e) {  
	         e.printStackTrace();  
	     }  
	 }  
	 /**  
	  * 删除索引  
	  *  
	  * @param id  
	  */  
	 public void deleteById(String id) {  
	     try {  
	         HttpSolrClient httpSolrClient = SolrFactory.connect();  
	         httpSolrClient.deleteById(id);  
	         httpSolrClient.commit();  
	     } catch (SolrServerException e) {  
	         e.printStackTrace();  
	     } catch (IOException e) {  
	         e.printStackTrace();  
	     }  

	 }  
	 
	 public static void main(String[] args) {
//		 AppConfig config = new AppConfig();
//			Reflect.on("com.jfinal.core.Config").call("configJFinal",config);
		SolrTemplate solrTemplate = new SolrTemplate();
//		SolrDocumentList list = solrTemplate.basicQuery("id:1", 0, 0);
		SolrDocumentList list = solrTemplate.basicQuery("{!geofilt pt=31.209261,121.561955 sfield=position d=1 score=distance}", 0, 0);
		System.out.println(JSONArray.toJSONString(list));
	}

}



//http://blog.csdn.net/backzero333/article/details/53588787
//	http://blog.csdn.net/future_ins/article/details/52085976
//		http://www.2cto.com/kf/201608/535379.html
