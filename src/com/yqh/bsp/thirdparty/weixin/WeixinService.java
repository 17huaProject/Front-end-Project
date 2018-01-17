package com.yqh.bsp.thirdparty.weixin;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yqh.bsp.base.config.AppConfig;
import com.yqh.bsp.base.kit.JsonKit;
import com.yqh.bsp.base.kit.Reflect;
import com.yqh.bsp.thirdparty.weixin.api.ApiConfig;
import com.yqh.bsp.thirdparty.weixin.kit.PaymentKit;
import com.yqh.bsp.utils.HttpUtil;
import com.yqh.bsp.utils.RandomUtil;

public class WeixinService {
	
	private static final Logger logger = LoggerFactory.getLogger(WeixinService.class);
	
	 /** <一句话功能简述>中信银行微信支付
     * <功能详细描述>支付请求
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     * @see [类、类#方法、类#成员]
     */
    public static Map<String,String> zxpay(String orderId,String openid,float totalAmount,String redirectUri,String goodsName) {
    	logger.debug("支付请求...");
        Map<String, String> map = new HashMap<String, String>();  
        map.put("service", "pay.weixin.jspay");
        map.put("version", "1.0");
        map.put("charset", "UTF-8");
        map.put("sign_type", "MD5");
        map.put("mch_id", ApiConfig.mchId);
        map.put("is_raw", "1");   //是否原生态  1=是 0=否
        map.put("out_trade_no", orderId);   //
        map.put("body", goodsName);
        map.put("sub_openid", openid);   //
        map.put("total_fee", String.valueOf((int)(totalAmount*100)));   //以分单位
        map.put("mch_create_ip", "127.0.0.1");   //
        map.put("notify_url", ApiConfig.wxNotify);
        map.put("callback_url", redirectUri);
//        map.put("time_start", "");   //订单开始时间
//        map.put("time_expire", "");   //订单超时时间
        map.put("nonce_str", String.valueOf(new Date().getTime())+RandomUtil.genRandomCode(6));

        map.put("sign", PaymentKit.createSign(map, ApiConfig.mchKey));
       
        Map<String, String> resultMap = null;
        Map<String, String> resInfo = new HashMap<String, String>();
        String status = "200";
        String message = "";
        try {
        	String requestString = PaymentKit.toXml(map);
        	logger.debug("reqParams:" + requestString);
        	String xmlStr = HttpUtil.httpsRequest(ApiConfig.wxReq, "POST", requestString);
        	logger.debug("respParams:" + xmlStr);
        	resultMap = PaymentKit.xmlToMap(xmlStr);
        	if ("0".equals(resultMap.get("status"))){  //
        		if (PaymentKit.verifySign(resultMap, ApiConfig.mchKey)) {
            		if ("0".equals(resultMap.get("result_code"))) {
            			resInfo.put("token_id", resultMap.get("token_id"));
            			resInfo.put("pay_info", resultMap.get("pay_info"));
            		} else {
            			status = "400"; 
                		message = resultMap.get("err_code")+":"+resultMap.get("err_msg");
            		}
            	} else {
            		status = "400"; 
            		message = "验证签名不通过";
            	}
        	} else {
        		status = "400"; 
        		message = resultMap.get("message");
        	}
        	
        } catch (Exception e) {
            logger.error("微信下单操作失败，原因：",e);
            message = "系统错误";
        } 
        resInfo.put("status", status);
        resInfo.put("message", message);
        return resInfo;
    }
    
	 /** <一句话功能简述>微信支付
     * <功能详细描述>支付请求
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     * @see [类、类#方法、类#成员]
     */
    public static Map<String,String> pay(String paidId,String openid,float amount,String goodsName,String attach) {
    	logger.debug("支付请求...");
        Map<String, String> map = new HashMap<String, String>();  
        map.put("appid",ApiConfig.appId);
        map.put("body", goodsName);
        map.put("mch_id", ApiConfig.mchId);
        map.put("nonce_str",  String.valueOf(new Date().getTime())+RandomUtil.genRandomCode(6));
        map.put("out_trade_no", paidId);
        map.put("notify_url", ApiConfig.wxNotify);
        map.put("spbill_create_ip", "127.0.0.1");
        map.put("total_fee",String.valueOf((int)(amount*100)));
        map.put("trade_type","JSAPI");
        map.put("attach",attach);  //附加数据
        map.put("openid", openid);

        map.put("sign", PaymentKit.createSign(map, ApiConfig.mchKey));
       
        Map<String, String> resultMap = null;
        Map<String, String> resInfo = new HashMap<String, String>();
        String status = "200";
        String message = "";
        try {
        	String requestString = PaymentKit.toXml(map);
        	logger.debug("reqParams:" + requestString);
        	String xmlStr = HttpUtil.httpsRequest(ApiConfig.wxReq, "POST", requestString);
        	logger.debug("respParams:" + xmlStr);
        	resultMap = PaymentKit.xmlToMap(xmlStr);
        	if ("SUCCESS".equals(resultMap.get("return_code")) && "SUCCESS".equals(resultMap.get("result_code"))){  //
        		if (PaymentKit.verifySign(resultMap, ApiConfig.mchKey)) {
        			Map<String,String> payMap  = new HashMap<>();
        			payMap.put("appId", ApiConfig.appId); 
        			payMap.put("timeStamp", (System.currentTimeMillis() / 1000)+"" ); 
        			payMap.put("nonceStr", String.valueOf(new Date().getTime())+RandomUtil.genRandomCode(6)); 
        			payMap.put("signType", "MD5"); 
        			payMap.put("package", "prepay_id=" + resultMap.get("prepay_id")); 
        		    String paySign = PaymentKit.createSign(payMap, ApiConfig.mchKey); 
        		    payMap.put("paySign", paySign); 
        		    resInfo.put("pay_info", JsonKit.toCompatibleJSONString(payMap));
            	} else {
            		status = "400"; 
            		message = "验证签名不通过";
            	}
        	} else {
        		status = "400"; 
        		message =  resultMap.get("err_code")+":"+resultMap.get("err_code_des");
        	}
        	
        } catch (Exception e) {
            logger.error("微信下单操作失败，原因：",e);
            message = "系统错误";
        } 
        resInfo.put("status", status);
        resInfo.put("message", message);
        return resInfo;
    }
    
    
    public static void main(String[] args) {
    	AppConfig config = new AppConfig();
		Reflect.on("com.jfinal.core.Config").call("configJFinal",config);
		String orderId = new Date().getTime()+"";
		String openid= "oR6oj1NgOJEZd7ev9KBSrwTLpjPY";
		float totalAmount = 0.1f;
		String redirectUri = "";
		String goodsName = "一起画";
		WeixinService.pay(orderId, openid, totalAmount, goodsName,"");
	}    

}
