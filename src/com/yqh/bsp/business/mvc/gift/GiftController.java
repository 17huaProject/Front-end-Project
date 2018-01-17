package com.yqh.bsp.business.mvc.gift;

import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.common.dao.UserAccountDao;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.enums.GiftStatus;
import com.yqh.bsp.common.enums.OrderStatus;
import com.yqh.bsp.common.enums.OrderType;
import com.yqh.bsp.common.enums.TransactionType;
import com.yqh.bsp.common.interceptor.LoginedAuthInterceptor;
import com.yqh.bsp.common.model.Gift;
import com.yqh.bsp.common.model.Order;
import com.yqh.bsp.common.model.OrderPaid;
import com.yqh.bsp.common.template.ContantTemplate;
import com.yqh.bsp.common.tools.IdGenerator;
import com.yqh.bsp.common.tools.IdGenerator24;
import com.yqh.bsp.thirdparty.weixin.WeixinService;
import com.yqh.bsp.utils.DateUtil;
import com.yqh.bsp.utils.RandomUtil;

@Action(controllerKey="/gift")
public class GiftController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(GiftController.class);
	private static GiftService giftService = new GiftService();

	@Before(LoginedAuthInterceptor.class)
	public void index() {
		String start = getPara("start_time");
		String end = getPara("end_time");
		Integer pageNo =  getParaToInt("page_no");     //开始页数
		Integer pageSize =  getParaToInt("page_size"); //每页显示
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = ContantTemplate.PAGESIZE;
		}
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		OrderStatus gsenums = null;
//		try{
//			if (StrKit.notBlank(giftStatus) || !"ALL".equals(giftStatus)){
//				gsenums = OrderStatus.valueOf(giftStatus);
//			}
//		}catch(Exception e){
//		}
		returnJson(giftService.queryList(userId, start, end, (pageNo-1)*pageSize, pageSize));
	}
	
	//增加礼品卡
	@Before({LoginedAuthInterceptor.class, GiftValidator.class})
	public void add() {
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		String sender = getPara("sender");
		String receiver = getPara("receiver");
		String content = getPara("content");
		float price = Float.parseFloat(getPara("price"));
		float orderAmount = Float.parseFloat(getPara("order_amount"));
		int number = Integer.parseInt(getPara("number"));
	
		String payChannel = getPara("pay_channel");
		if (StrKit.isBlank(payChannel)) {
			payChannel = "WXPAY";
		}
		String giftName =  price+"元礼品卡";
		Gift gift = new Gift();
		String giftId = IdGenerator24.get();
		gift.set("gift_id", giftId);
		gift.set("user_id", userId);
		gift.set("gift_name",giftName);
		gift.set("sender", sender);
		gift.set("content", content);
		gift.set("receiver", receiver);
		gift.set("price", price);
		gift.set("number", number);
		gift.set("order_amount", orderAmount);  
		Date now = new Date(); 
		gift.set("create_time", now);  //实际时间
		Date expiryNow = DateUtil.getSkipTime(now, 3);
		gift.set("expiry_time",expiryNow);
		gift.set("gift_status", GiftStatus.UNPAID.toString());
		gift.set("source", getPara("pid"));
//		boolean flag = gift.save();
		
		if ("WXPAY".equals(payChannel)) {
			float payMoney = orderAmount;
			String paidId = IdGenerator.generateOrderId("", Order.ORDERFlAG)+RandomUtil.genRandomCode(4);
			OrderPaid orderPaid = new OrderPaid();
			orderPaid.set("paid_id", paidId);
			orderPaid.set("order_id", giftId);
			orderPaid.set("user_id", userId);
			orderPaid.set("order_amount", orderAmount);
			orderPaid.set("balance_amount", 0);
			orderPaid.set("paid_amount", orderAmount);
			Date payDate = new Date();
			orderPaid.set("create_time", payDate);
			orderPaid.set("pay_channel", payChannel);

			String openid = lUser.getWxOpenId();
			Map<String,String> respMap = WeixinService.pay(paidId, openid, payMoney, giftName,OrderType.GIFT.toString());
	
			if ("200".equals(respMap.get("status"))) {
				gift.save();
				orderPaid.save();
				JSONObject json = new JSONObject();
				json.put("gift_id", giftId);
				json.put("paid_amount", payMoney);
				json.put("pay_time", payDate);
				json.put("pay_info", JSONObject.parseObject(respMap.get("pay_info")));
				returnJson(json);
			} else {
				returnJson(Errcode.FAIL, respMap.get("message"));
				return;
			}
		}
	}
	
	public void show() {
		String userId = getPara("sender_id");
		String giftId = getPara("gift_id");
		
		Gift gift = giftService.queryOne(userId, giftId);
		if (gift == null ) {
			returnJson(Errcode.FAIL, "该礼品卡不存在");
		} else {
			returnJson(gift);
		}
	}
	
	@Before(LoginedAuthInterceptor.class)
	public void receiveGift() {
		String userId = getPara("sender_id");
		String giftId = getPara("gift_id");
		
		LoginUser lUser = getSessionAttr("loginUser");
		String receiverId = lUser.getUserId();
		if (receiverId.equals(userId)) {
			returnJson(Errcode.FAIL, "您不能领取自己发的卡");
			return;
		}
		try {
			Gift gift = giftService.checkGift(userId, giftId, receiverId);
			if (gift == null) {
				returnJson(Errcode.FAIL, "该礼品卡不存在或已领取");
				return;
			} else {
				float price = gift.getBigDecimal("price").floatValue();
				UserAccountDao.IncomeTransaction(receiverId, gift.getStr("gift_id"), price, TransactionType.CASH_GIFT);
				returnJson(gift);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			returnJson(Errcode.FAIL, "礼品卡领取错误");
		}
	}
	
	
	@Before(LoginedAuthInterceptor.class)
	public void feedback() {
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		String title = getPara("title");
		String content = getPara("content");
		
		
		if (StrKit.isBlank(content)) {
			returnJson(Errcode.FAIL,"请输入留言内容");
		} else {
			try{
				if (StrKit.isBlank(title)) {
					title = "";
				}
				title = URLDecoder.decode(title, "UTF-8");
				content = URLDecoder.decode(content, "UTF-8");
			}catch(Exception e) {
				
			}
			
			Record record = new Record();
			record.set("issuer_id", userId);
			record.set("title", title);
			record.set("content", content);
			record.set("issue_time", new Date());
			Db.save("t_feedback", record);
			
			returnJson(Errcode.SUCC,"留言成功");
		}
	}
	
}
