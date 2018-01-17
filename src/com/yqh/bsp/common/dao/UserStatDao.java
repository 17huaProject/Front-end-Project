package com.yqh.bsp.common.dao;

import com.jfinal.plugin.activerecord.Db;
import com.yqh.bsp.common.enums.TransactionType;
/**
 * @author xiegh
 *
 * @date 2016-10-12
 */
public class UserStatDao {
	/**
	 * 
	 * @param userId
	 * @param transType  交易类型
	 * @param num         数量值
	 * @param money       资金
	 * @param extraMoney  额外资金
	 * @return
	 */
	public static boolean updateUserStat(String userId,TransactionType transType,int num,float money,float extraMoney){
		int updateFlag = 1;
		switch (transType) {

		case CASH_PRESENT:   //活动赠送
			Db.update("update t_user_stat set total_reward=total_reward+? where user_id=?", extraMoney, userId);
			break;
		
		default:
			break;
		}
		
		if (updateFlag == 0) {
			Db.update("insert into t_user_stat(user_id,total_oil_volume,total_oil_amount,total_reward) values(?,?,?,?)", userId,num,money,extraMoney);
		}
		
		return true;
	}

}
