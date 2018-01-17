package com.yqh.bsp.common.dao;

import java.sql.SQLException;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
/**
 * 用户
 * @author season
 *
 */
public class UserScoreDao {
	
	public static final int SCORE_REG = 1000;    
//	public static final int SCORE_LOGIN = 20;
	public static final int SCORE_MSG = 3000;   //留言
	public static final int SCORE_COMMENT = 3100;
	public static final int SCORE_LIKE = 3200;  //顶踩
	public static final int SCORE_WEIBO = 3300; // 普通微博
	public static final int SCORE_TZ_WEIBO = 3400;// 投注微博
	public static final int SCORE_PAY = 6000;    // 投注发微博 >5000是需要进行计算的积分
	public static final int SCORE_BUY = 6100;    //购买积分
	public static final int SCORE_REWARD = 6200;    //奖励积分	
	public static final int SCORE_GAME_WIN = 7200;  // 游戏中奖  转盘奖励
	
	@Before(Tx.class)
	public static int IncomeScore(String userId,int scoreCode,String remark,int... value) throws SQLException{
		String selectString = "select score from t_users where user_id=?";
		int score = Db.queryInt(selectString, userId);
		int v = 0;
		if (scoreCode < 5000) {
			switch (scoreCode) {
			case SCORE_REG:
				v = 150;
				break;
			case SCORE_COMMENT:
				v = 3;
				break;
			}
			score = score + v;   //变化后的积分
			String saveSql = "insert into t_score_record(user_id,score_code,value,score,create_time,description) values( ?,?,?,?,now(),?)";
		    Db.update(saveSql, userId,scoreCode,v,score,remark);
		    
		} else if (scoreCode > 5000) {
			switch (scoreCode) {
			case SCORE_PAY:
				value[0]=value[0]*2;
				break;
			}
			score = score + value[0];  //变化后的积分
			String saveSql = "insert into t_score_record(user_id,score_code,value,score,create_time,description) values(?,?,?,?,now(),?)";
			Db.update(saveSql, userId,scoreCode, value[0],score,remark );	
		}
		Db.update("update t_users set score=? where user_id=?", score,userId);
		return score;
	}
	/**
	 * 
	 * @param userId
	 * @param scoreCode
	 * @param remark
	 * @param value
	 * @return  -1=资金不足
	 * @throws SQLException
	 */
	@Before(Tx.class)
	public static int ExpenseTransaction(String userId,int scoreCode,String remark,int value)throws SQLException{
		String selectString = "select score from t_users where user_id=?";
		int score = Db.queryInt(selectString, userId);
		score = score-value;
		if (score < 0)
			return -1;
		String saveSql = "insert into t_score_record(user_id,score_code,value,score,create_time,description) values(?,?,?,?,now(),?)";
		Db.update(saveSql, userId,scoreCode, value,score,remark );	
		Db.update("update t_users set score=? where user_id=?", score,userId);
		return score;
	}

}

