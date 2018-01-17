package com.yqh.bsp.thirdparty.sms;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.yqh.bsp.common.template.ContantTemplate;
import com.yqh.bsp.utils.MD5Util;

public class FeiTianSmsService {
	
	private static String URL = "http://sms6.hui00.cn/if/sendsms";
	private static String ACCOUNTID = "20170317112757002";
	private static String ACCOUNT = "zwhy";
	private static String PASSWORD = "e3cf37dd2fee348f9a48160d88422ef1";  // "zwhy123";


	/**
	 * 
	 * @param phone
	 * @param content
	 * @param sendTime  定时发送，为空立即发送
	 * @return
	 */
	public static String sendSMS(String phone,String content,String sendTime) {

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(URL);
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/xml;charset=UTF-8");
		try {			//设置Http Post数据 
			NameValuePair[] data = {//提交短信
//					new NameValuePair("act", "sendmsg"),
					new NameValuePair("unitid", ACCOUNTID),
					new NameValuePair("username", ACCOUNT),
					new NameValuePair("passwd", PASSWORD),
					new NameValuePair("msg", content),
					new NameValuePair("phone",phone),
					new NameValuePair("port", ""),
					new NameValuePair("sendtime", sendTime),
			};
			method.setRequestBody(data);
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {   
               System.err.println(phone+" Send req failed: " + method.getStatusLine());   
               return "";
            } else {
            	 String resp = method.getResponseBodyAsString();
            	 System.out.println(resp);
                 if ("0".equals(resp.substring(0,resp.indexOf(",")))) {     //4085 状态码， 验证码发送超过5条
                	 return "0";
                 } else {
                	 System.err.println(phone+" Send failed: " + resp);   
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
		String content = String.format(ContantTemplate.SMS_REGISTER_CODE, "233412");
		FeiTianSmsService.sendSMS("18057496397", content,"");
	}
}
