package com.yqh.bsp.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class HttpUtil {

 
  private static final String URL_PARAM_CONNECT_FLAG = "&";
  private static Log log = LogFactory.getLog(HttpUtil.class);
  
//  public static final HttpClient CLIENT = new HttpClient(new MultiThreadedHttpConnectionManager());

  
  /**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param queryString
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @return 返回请求响应的HTML
 * @throws IOException 
	 */
	public static String getWithParamString(String url, String queryString, String charset) throws IOException {
		HttpClient CLIENT = new HttpClient();
		HttpMethod method = new GetMethod(url);
		BufferedReader reader = null;
		String jsonString = "";
		try {
			// 对get请求参数做了http请求默认编码，好像没有任何问题，汉字编码后，就成为%式样的字符串
		    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			method.setQueryString(URIUtil.encodeQuery(queryString));
			int statusCode = CLIENT.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {   
               System.err.println("Send req failed: " + method.getStatusLine());   
            } else {
//          	reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
//				String line;
//				while ((line = reader.readLine()) != null) {
//					response.append(line);
//				}
            	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    		    int len = 0;
    		    byte[] data = new byte[1024];
    		    while((len = method.getResponseBodyAsStream().read(data))!=-1){
    		            outputStream.write(data,0,len);
    		    }
    		    jsonString = new String(outputStream.toByteArray(),"UTF-8");
            }
		} catch (URIException e) {
			log.error("执行HTTP Get请求时，编码查询字符串“" + queryString + "”发生异常！", e);
		} catch (IOException e) {
			throw e;
		} finally {
			if (reader != null)
				reader.close();
			method.releaseConnection();
		}
		return jsonString;
	}
	
	public static String getWithParamMap(String url ,Map<String,String> map){
		String queryString = getUrl(map, "utf-8");
		try {
			return getWithParamString(url, queryString, "utf-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public static String postWithParamMap(String url, Map<String,String> params,String charSet) {
		HttpClient CLIENT = new HttpClient();
		StringBuffer response = new StringBuffer();
//		PostMethod method = new PostMethod(url);
		PostMethod method = new UTF8PostMethod(url);
//		method.addRequestHeader("Content-Type","multipart/form-data");
		try {			//设置Http Post数据 
			if (params != null) {
				int len = params.size();
				NameValuePair[] np = new NameValuePair[len];  //new NameValuePair("access_token","2.00HGyLyDoELnsC2f47a3e724sKMIRD")
				int i = 0;	
				for (Map.Entry<String,String> entry : params.entrySet()) {
					np[i] = new NameValuePair(entry.getKey(),entry.getValue());
					i++;
			    }
				method.setRequestBody(np);
			    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			} 
			int statusCode = CLIENT.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {   
               System.err.println("Send req failed: " + method.getStatusLine());   
            } else {
            	 byte[] resp = method.getResponseBody();
                 if (null == resp)
     				return null;
     			return new String(resp, charSet);
          }
		} catch (URIException e) {
			log.error("执行HTTP post请求时发生异常！", e);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}
	
	
    public static String postWithParamString(String url, String requestString,String charset) {
    	HttpClient CLIENT = new HttpClient();
        PostMethod method = new PostMethod(url);
        try {
        	method.addRequestHeader("Connection", "Keep-Alive");
            method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,charset);  
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
            method.setRequestEntity(new ByteArrayRequestEntity(requestString.getBytes(charset)));
            method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
            int statusCode = CLIENT.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            byte[] resp = method.getResponseBody();
            if (null == resp)
				return null;
			return new String(resp, charset);

        } catch (SocketTimeoutException e) {
        	return null;
        } catch (Exception e) {
        	return null;
        } finally {
            method.releaseConnection();
        }
    }
    
    
    /**
     * get method URL
     * @param map Map
     * @return String
     */
    public static String getUrl(Map map,String charSet) {
      if (null == map || map.keySet().size() == 0) {
        return ("");
      }
      StringBuffer url = new StringBuffer();
      Set keys = map.keySet();
      for (Iterator i = keys.iterator(); i.hasNext(); ) {
        String key = String.valueOf(i.next());
        if (map.containsKey(key)) {
      	 Object val = map.get(key);
      	 String str = val!=null?val.toString():"";
      	 try {
  			str = URLEncoder.encode(str, charSet);
  		} catch (UnsupportedEncodingException e) {
  			e.printStackTrace();
  		}
          url.append(key).append("=").append(str).
              append(URL_PARAM_CONNECT_FLAG);
        }
      }
      String strURL = "";
      strURL = url.toString();
      if (URL_PARAM_CONNECT_FLAG.equals("" + strURL.charAt(strURL.length() - 1))) {
        strURL = strURL.substring(0, strURL.length() - 1);
      }
      return (strURL);
    }
    
    /** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
     */  
    public static String httpsRequest(String requestUrl, String requestMethod, String intStr) {  
        StringBuffer buffer = new StringBuffer();  
        try {  
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
            TrustManager[] tm = { new MyX509TrustManager() };  
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();  

            URL url = new URL(requestUrl);  
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
            httpUrlConn.setSSLSocketFactory(ssf);  

            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            httpUrlConn.setRequestMethod(requestMethod);  

            if ("GET".equalsIgnoreCase(requestMethod))  
                httpUrlConn.connect();  

            // 当有数据需要提交时  
            if (null != intStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码  
                outputStream.write(intStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  

            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  

            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
             inputStream.close();  
             inputStream = null;  
             httpUrlConn.disconnect();  
         } catch (ConnectException ce) {  
             log.error("Weixin server connection timed out.");  
         } catch (Exception e) {  
             log.error("https request error:{}", e);  
         }  
         return buffer.toString();  
     }  
    
	public static String postWithParamMapNoState(String url, Map<String,String> params,String charSet) {
		HttpClient CLIENT = new HttpClient();
		StringBuffer response = new StringBuffer();
		PostMethod method = new UTF8PostMethod(url);
		try {			//设置Http Post数据 
			if (params != null) {
				int len = params.size();
				NameValuePair[] np = new NameValuePair[len];  //new NameValuePair("access_token","2.00HGyLyDoELnsC2f47a3e724sKMIRD")
				int i = 0;	
				for (Map.Entry<String,String> entry : params.entrySet()) {
					np[i] = new NameValuePair(entry.getKey(),entry.getValue());
					i++;
			    }
				method.setRequestBody(np);
			    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			} 
			int statusCode = CLIENT.executeMethod(method);
			 byte[] resp = method.getResponseBody();
             if (null == resp)
 				return null;
 			return new String(resp, charSet);
		} catch (URIException e) {
			log.error("执行HTTP post请求时发生异常！", e);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}
 
    public static class UTF8PostMethod extends PostMethod{     
        public UTF8PostMethod(String url){     
           super(url);     
       }     
       @Override    
        public String getRequestCharSet() {     
            //return super.getRequestCharSet();     
           return "UTF-8";     
        }     
  }  
	
}

