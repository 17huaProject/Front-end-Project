package com.yqh.bsp.business.mvc.config;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.Bank;
import com.yqh.bsp.common.model.City;

public class ConfigService extends BaseService {
	
	public List<Bank>  queryBank() {
		List<Bank> listBank = CacheKit.get("cache24hour","allbank");
		if (listBank == null) {
			listBank = Bank.dao.find("select id as bank_id,name as bank_name,icon,color from b_bank where status=1 ");
			CacheKit.put("cache24hour","allbank",listBank);
		}
		return listBank;
	}
	
	public List<City>  queryCity() {
		List<City> listCity = CacheKit.get("cache24hour","allcity");
		if (listCity == null) {
			listCity = City.dao.find("SELECT code,name,ename FROM b_city where is_show=1 order by sort");
			CacheKit.put("cache24hour","allcity",listCity);
		}
		return listCity;
	}
	
	public static String queryBankName(int bankId) {
		Bank bank = Bank.dao.findFirst("select name from b_bank where id=?",bankId);
		if(bank != null) {
			return bank.getStr("name");
		} else {
			return "";
		}
	}
	
	public static Record getConfig(String code) {
		Record record = Db.findFirst("select val,remark from b_config where code=?",code);
		return record;
	}
	
	public static List<Record> getCouponConfig(String code) {
		List<Record> records = Db.find("select * from t_coupon_type where coupon_type=? and end_time>now()",code);
		return records;
	}
	
	
}
