package com.yqh.bsp.thirdparty.weixin;

import com.yqh.bsp.thirdparty.weixin.api.ApiResult;
import com.yqh.bsp.thirdparty.weixin.api.TemplateData;
import com.yqh.bsp.thirdparty.weixin.api.TemplateMsgApi;

public class WeixinMsgService {
	//通知参加活动
	public static void sendEventNotice(TemplateData templateData) {
		ApiResult result = TemplateMsgApi.send(templateData
			    .setTemplate_id("E5h7Dwv0157Vzv-qkTIlhyZhPKIZUEKXJfXz9rboq2U")   // 模板id
			    .build());
		System.out.println(result);
	}
	
	public static void main(String[] args) {
//		sendEventNotice("oR6oj1NgOJEZd7ev9KBSrwTLpjPY");
	}
	
	
 }
