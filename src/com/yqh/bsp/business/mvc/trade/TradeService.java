package com.yqh.bsp.business.mvc.trade;

import java.sql.SQLException;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.dao.UserAccountDao;
import com.yqh.bsp.common.enums.TransactionType;
import com.yqh.bsp.common.model.OrderPaid;
import com.yqh.bsp.common.model.OrderRefund;

public class TradeService extends BaseService {
	
	public boolean saveOrderOilCard(OrderRefund order,String userId,String transOrderId,float money,TransactionType transType) {
		float flag = 0;
		try {
			flag = UserAccountDao.ExpenseTransaction(userId, transOrderId, money, transType);
			if (flag >=0) {
				order.save();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean saveOrderBankCard(OrderPaid order,String userId,String transOrderId,float money,TransactionType transType) {
		try {
			float flag = UserAccountDao.ExpenseTransaction(userId, transOrderId, money, transType);
			if (flag >=0) {
				order.save();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public List<OrderRefund> queryOilCardDetail(String userId,int pageNo,int pageSize){
		String url = "select order_id,user_id,amount,create_time,oil_card,oil_card_type,pay_time,pay_status from t_order_oilcard where user_id=? ";
		url = url + " order by create_time desc limit ?,?";
		return OrderRefund.dao.find(url, userId,(pageNo-1)*pageSize,pageSize);
	}
	
	public List<OrderPaid> queryDrawMoneyDetail(String userId,int pageNo,int pageSize){
		String url = "select order_id,user_id,amount,create_time,bank_card,fee,pay_time,pay_status from t_order_drawmoney where user_id=? ";
		url = url + " order by create_time desc limit ?,?";
		return OrderPaid.dao.find(url, userId,(pageNo-1)*pageSize,pageSize);
	}
	
	/**
	 * 查询用户在提现数量
	 * @param userId
	 * @return
	 */
	public long queryUserDrawMoneyNum(String userId) {
		return Db.queryLong("select count(*) from t_order_drawmoney where user_id=? and status<2",userId);
	}
	
	public float queryDrawLimit(String userId) {
		return Db.queryBigDecimal("select (total_oil_amount-total_draw_money-total_reward) as amount from t_user_stat where user_id=? ",userId).floatValue();
	}
	
	/**
	 * 查询用户在充油卡数量
	 * @param userId
	 * @return
	 */
	public long queryUserChargeOilCardNum(String userId) {
		return Db.queryLong("select count(*) from t_order_oilcard where user_id=? and status<2",userId);
	}
	
}
