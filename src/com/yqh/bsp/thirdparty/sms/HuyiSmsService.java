package com.yqh.bsp.thirdparty.sms;

import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.yqh.bsp.common.template.ContantTemplate;

public class HuyiSmsService {
	
	private static String URL = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	private static String ACCOUNT = "cf_kbsy";
	private static String PASSWORD = "265e8b0ef2f12c1f473fe49af5c5a61f";
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
				    new NameValuePair("account", ACCOUNT), 
				    new NameValuePair("password", PASSWORD), 
				    new NameValuePair("mobile", mobile), 
				    new NameValuePair("content", content),
			};
			method.setRequestBody(data);
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {   
               System.err.println("Send req failed: " + method.getStatusLine());   
               return "";
            } else {
            	 InputStream is = null;
            	 is = method.getResponseBodyAsStream();
                 Document document = new SAXBuilder().build(is);
                  // 获取根元素
                 Element element = document.getRootElement();
                 Namespace ns = Namespace.getNamespace("http://106.ihuyi.cn/");
                 String code = element.getChildText("code",ns);
                 
                 if ("2".equals(code)) {     //4085 状态码， 验证码发送超过5条
                	 return element.getChildText("smsid",ns);
                 } else {
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
		HuyiSmsService.sendSMS("15058287668", content);
	}
//	http://blog.csdn.net/kenhins/article/details/19556583
}
