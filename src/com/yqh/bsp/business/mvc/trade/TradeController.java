package com.yqh.bsp.business.mvc.trade;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.business.mvc.account.AccountService;
import com.yqh.bsp.common.dao.UserStatDao;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.enums.OrderStatus;
import com.yqh.bsp.common.enums.PayStatus;
import com.yqh.bsp.common.enums.TransactionType;
import com.yqh.bsp.common.interceptor.LoginedAuthInterceptor;
import com.yqh.bsp.common.model.OrderPaid;
import com.yqh.bsp.common.model.OrderRefund;
import com.yqh.bsp.common.model.User;
import com.yqh.bsp.common.tools.IdGenerator;

@Action(controllerKey="/trade")
@Before(LoginedAuthInterceptor.class)
public class TradeController extends BaseController {
	private static AccountService accountService = new AccountService();
	private static TradeService tradeService = new TradeService();
	
	@Before(TradeValidator.class)
	public void payOilCard() {
		int amount = getParaToInt("amount");
		if (amount<50 || amount%50 != 0) {
			returnJson(Errcode.FAIL, "油卡充值金额需50的倍数");
			return;
		}
		
		int a=1;
		if (a ==1) {
			returnJson(Errcode.FAIL, "油卡充值服务暂停服务，请选择提现");
			return;
		}
		
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		User user = accountService.querySecurity(userId);
		if (user.getInt("oil_flag")!=1){
			returnJson(Errcode.FAIL, "您未绑定油卡");
			return;
		} 
		
		int userRank = AccountService.queryUserRank(userId);
		if (userRank < 2) {
			returnJson(Errcode.FAIL, "您还未攒油，攒一单就可充油卡啦！");
			return;
		}
		
		if(tradeService.queryUserChargeOilCardNum(userId)>0) {
			returnJson(Errcode.FAIL, "您已有订单在处理中，不能再次申请");
			return;
		}
		
		OrderRefund order = new OrderRefund();
		String orderId =  IdGenerator.generateOrderId("C", "");
		order.set("order_id",orderId);
		order.set("user_id", userId);
		order.set("amount", amount);
		order.set("oil_card_type", user.getStr("oil_card_type"));
		order.set("oil_card", user.getStr("oil_card"));
		order.set("create_time", new Date());
		order.set("pay_status", PayStatus.WAIT.toString());
		order.set("source", getPara("pid"));
		order.set("status", OrderStatus.PENDING);
		boolean status = tradeService.saveOrderOilCard(order, userId, orderId, amount, TransactionType.PAY_ORDER);
		if (status) {
			// 更新统计数据
			UserStatDao.updateUserStat(userId, TransactionType.PAY_ORDER, 0, amount, 0);
			returnJson(Errcode.SUCC, "申请成功，等待处理");
		} else {
			returnJson(Errcode.FAIL, "申请失败");
		}
	}
	@Before(TradeValidator.class)
    public void payBankCard() {
		float amount = Float.parseFloat(getPara("amount"));
		if (amount<2) {
			returnJson(Errcode.FAIL, "提现金额错误");
			return;
		}
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		User user = accountService.querySecurity(userId);
		if (user.getInt("bank_flag")!=1){
			returnJson(Errcode.FAIL, "您未绑定银行卡");
			return;
		} 
		
		int userRank = AccountService.queryUserRank(userId);
		if (userRank < 2) {
			returnJson(Errcode.FAIL, "您还未攒油，攒一单就可提现啦！");
			return;
		}
		
//		float limit = tradeService.queryDrawLimit(userId);
//		if (amount > limit) {
//			returnJson(Errcode.FAIL, "提现额度剩余："+limit+"元,其余请充油卡");
//			return;
//		}
		
		OrderPaid order = new OrderPaid();
		String orderId =  IdGenerator.generateOrderId("Y", "");
		order.set("order_id",orderId);
		order.set("user_id", userId);
		order.set("amount", amount);
		order.set("bank_id", user.get("bank_id"));
		order.set("realname", user.getStr("realname"));
		order.set("bank_card", user.getStr("bank_card"));
		order.set("create_time", new Date());
		int fee = 1;
		if (amount >= 1000) {
			fee = 0;
		}
		order.set("fee", fee);
		order.set("pay_status", PayStatus.WAIT.toString());
		order.set("source", getPara("pid"));
		order.set("status", OrderStatus.PENDING);
		boolean status = tradeService.saveOrderBankCard(order, userId, orderId, amount, TransactionType.PAY_WITHDRAW);
		if (status) {
			// 更新统计数据
			UserStatDao.updateUserStat(userId, TransactionType.PAY_WITHDRAW, 0, amount, 0);
			returnJson(Errcode.SUCC, "申请成功，等待处理");
		} else {
			returnJson(Errcode.FAIL, "申请失败");
		}
		
	}
    /**
     * 充油卡记录
     */
    public void showOilCardDetail() {
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		int pageNo = 1;
		int pageSize = 20;
		try{
			pageNo = getParaToInt("page_no");
			pageSize = getParaToInt("page_size");
		}catch (Exception e) {
		}
		
		List<OrderRefund> list = tradeService.queryOilCardDetail(userId, pageNo, pageSize);
		returnJson(list);
    }
    /**
     * 提现记录
     */
    public void showBankCardDetail() {
    	LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		int pageNo = 1;
		int pageSize = 20;
		try{
			pageNo = getParaToInt("page_no");
			pageSize = getParaToInt("page_size");
		}catch (Exception e) {
		}
		
		List<OrderPaid> list = tradeService.queryDrawMoneyDetail(userId, pageNo, pageSize);
		returnJson(list);
    }

}
