package com.yqh.bsp.thirdparty.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class QixintongSmsService {
	
	private static String URL = "http://api.cnsms.cn";
	private static String ACCOUNT = "116445";
	private static String PASSWORD = "8c26800a17e8524961bf3635a6a2b6cc";
	/**
	 * 
	 * @param mobile
	 * @param content
	 * @return  返回smsid， 接口服务
	 */
	public static String sendSMS(String mobile,String content) {

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(URL);
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
		try {			//设置Http Post数据 
			NameValuePair[] data = {//提交短信
					new NameValuePair("ac", "send"), 
				    new NameValuePair("uid", ACCOUNT), 
				    new NameValuePair("pwd", PASSWORD), 
				    new NameValuePair("mobile", mobile), 
				    new NameValuePair("content", content),
				    new NameValuePair("encode", "UTF-8")
			};
			method.setRequestBody(data);
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {   
               System.err.println("Send req failed: " + method.getStatusLine());   
               return "";
            } else {
            	 String code = method.getResponseBodyAsString();
                 
                 if ("100".equals(code)) {    
                	 return code;
                 } else {
                	 System.out.println(mobile+ " Qixintong SMS failure:"+code);
                	 return "";
                 }
            }
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			method.releaseConnection();
		}
	}
	
	public static void main(String[] args) {
		
		
		String content = "";
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		QixintongSmsService.sendSMS("18057496397", content);
	}
}
