package com.yqh.bsp.business.mvc.pay;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Clear;
import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.kit.JsonKit;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.common.enums.OrderType;
import com.yqh.bsp.common.enums.PayStatus;
import com.yqh.bsp.common.enums.ValidStatus;
import com.yqh.bsp.common.model.Order;
import com.yqh.bsp.common.model.OrderPaid;
import com.yqh.bsp.thirdparty.weixin.api.ApiConfig;
import com.yqh.bsp.thirdparty.weixin.kit.PaymentKit;
import com.yqh.bsp.utils.HttpClientUtil;

@Action(controllerKey = "/weixin")
@Clear
public class WeixinController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(WeixinController.class);
	private static PayService payService = new PayService();

	public void callback() {
		String respStr = "";
		try {
			respStr = HttpClientUtil.InputStream2String(getRequest().getInputStream());
			logger.info("weixin callback-->" + respStr);
			if (StrKit.isBlank(respStr)) {
				renderText("fail");
				return;
			}
			String orderId = "";
			Map<String, String> respMap = PaymentKit.xmlToMap(respStr);
			System.out.println(JsonKit.toCompatibleJSONString(respMap));
			if (PaymentKit.verifySign(respMap, ApiConfig.mchKey)) {
				String paidId = respMap.get("out_trade_no");
				if ("SUCCESS".equals(respMap.get("return_code")) && "SUCCESS".equals(respMap.get("result_code"))) {
					OrderPaid orderPaid = OrderPaid.dao.findById(paidId);
					if (orderPaid == null) {
						logger.error(orderId + "NO THIS PAID ID : " + paidId);
						renderText(setBackXML("SUCCESS", ""));
						return;
					}

					orderPaid.set("status", ValidStatus.VALID.getVal());
					orderPaid.set("pay_time", new Date());
					orderPaid.set("pay_bank", respMap.get("bank_type"));
					orderPaid.set("remark", respMap.get("openid"));
					orderPaid.set("pay_trace_no", respMap.get("transaction_id"));// 微信订单号
					orderPaid.set("pay_status", PayStatus.SUCC.toString());
					orderPaid.update();
					String orderType = respMap.get("attach");
					if (StrKit.isBlank(orderType)) {
						orderType = OrderType.COMMON.toString(); 
					}
					
					if (orderType.equals(OrderType.GIFT.toString())) {  //购买礼品通知
						payService.giftPaySuccess(orderPaid, orderType);
					} else if (orderType.equals(OrderType.GOODS.toString())) {  //购买商品通知{
						payService.orderPaySuccess(orderPaid,orderType);
					} else {
						Order order = payService.orderPaySuccess(orderPaid,orderType);
						payService.changeEventTicket(order.get("event_id").toString(), order.getInt("number"));
					}
					renderText(setBackXML("SUCCESS", ""));
					return;
				} else {
					logger.error(orderId + " weixin callback fail: " + respMap.get("err_code_des"));
				}
			} else {
				logger.info("weixin callback verifysign fail");
				renderText(setBackXML("FAIL", ""));
				return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String setBackXML(String returnCode, String returnMsg) {
		return "<xml><return_code><![CDATA[" + returnCode + "]]></return_code><return_msg><![CDATA[" + returnMsg + "]]></return_msg></xml>";
	}
}
