package com.yqh.bsp.common.dao;

import java.sql.SQLException;
import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yqh.bsp.common.enums.TransactionType;
import com.yqh.bsp.common.tools.IdGenerator;
import com.yqh.bsp.utils.AESUtil;
import com.yqh.bsp.utils.DateUtil;

public class UserAccountDao {
	
	@Before(Tx.class)
	public static float IncomeTransaction(String userId,String transOrderId,float money,TransactionType transType) throws SQLException{
		float balance = Db.queryBigDecimal("select balance from t_users where user_id=?",userId).floatValue();

		balance = balance+money;
		Db.update("update t_users set balance=?,enc_balance=? where user_id=?",balance,AESUtil.encrypt(balance+""),userId);
		Date now = new Date();
		String datetime = DateUtil.formatDate(now, "yyyyMMddHHmmss");

		Db.update("insert into t_user_balance_record(record_id,user_id,money,account,description,type,create_time,trans_order_id) values(?,?,?,?,?,?,?,?)",
				              datetime+IdGenerator.generatId(),userId,money,balance,transType.getName(),transType.getVal(),now,transOrderId);
		
//		switch (transType) {
//			
//		case CARRY_OVER:			
//			break;
//        case UPAY_CHARGE:
//			
//			break;
//		default:
//			break;
//		}
		
		return balance;
	}
	/**
	 * 
	 * @param userId
	 * @param transOrderId
	 * @param amount
	 * @param transType
	 * @return  -1=资金不足
	 * 
	 */
	@Before(Tx.class)
	public static float ExpenseTransaction(String userId,String transOrderId,float money,TransactionType transType)throws SQLException{
		float balance = Db.queryBigDecimal("select balance from t_users where user_id=?",userId).floatValue();
		if (money>balance) {
			return -1;
		}
		balance = balance-money;
		Db.update("update t_users set balance=?,enc_balance=? where user_id=?",balance,AESUtil.encrypt(balance+""),userId);
//		switch (transType) {
//		case PAY_GET:
//			
//			break;
//		default:
//			break;
//		}
		Date now = new Date();
		String datetime = DateUtil.formatDate(now, "yyyyMMddHHmmss");
		Db.update("insert into t_user_balance_record(record_id,user_id,money,account,description,type,create_time,trans_order_id) values(?,?,?,?,?,?,?,?)",
				              datetime+IdGenerator.generatId(),userId,-money,balance,transType.getName(),transType.getVal(),now,transOrderId);
		return balance;
	}

}
