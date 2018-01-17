package com.yqh.bsp.business.mvc.sms;

import com.jfinal.plugin.activerecord.Db;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.enums.SmsCode;
import com.yqh.bsp.common.model.SmsVcode;
import com.yqh.bsp.common.template.ContantTemplate;
import com.yqh.bsp.utils.RandomUtil;
import com.yqh.bsp.utils.SMSUtil;

public class SmsService extends BaseService {
	/**
	 * 
	 * @param phone
	 * @param codeType
	 * @return 发送成功，返回短信验证码
	 */
	public String sendCode(String phone,SmsCode codeType) {
		SmsVcode smsv = SmsVcode.dao.findById(phone);
		String smsCode = RandomUtil.genRandomCode(6);
        String content="";
		switch(codeType){
		case REG_USER:
			content = String.format(ContantTemplate.SMS_REGISTER_CODE,smsCode);
			break;
		case FIND_PWD:
			content = String.format(ContantTemplate.SMS_FINDPWD_CODE,smsCode);
			break;
		case SET_PAYPWD:
			content = String.format(ContantTemplate.SMS_SETPAYPWD_CODE,smsCode);
			break;
		}
		boolean flag = SMSUtil.sendSMS(phone, content);
		
		if (flag) {
			String resultSql ;
			if (smsv != null){
				resultSql = "update t_sms_vcode set vcode=?,send_time=now(),status=0,day_num=day_num+1 where phone=?";
				Db.update(resultSql, smsCode,phone);
			} else {
				resultSql = "insert into t_sms_vcode(vcode,phone,send_time,type) values(?,?,now(),?)";
				Db.update(resultSql, smsCode,phone,codeType.toString());
			}
			
			return smsCode;
		} else {
			return "";
		}
	}
	
	public static boolean verifySmsCode(String phone,String code){
		long count = Db.queryLong("select count(*) from t_sms_vcode where phone=? and vcode=? and send_time> (now()- interval 10 minute)", phone,code);
		if (count ==0) {
			return false;
		} else {
			return true;
		}
	}
	

}
