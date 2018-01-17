package com.yqh.bsp.common.model;

import com.yqh.bsp.base.annotation.Table;
import com.yqh.bsp.base.mvc.BaseModel;


@Table(tableName="t_sms_vcode",pkName="phone")
public class SmsVcode extends BaseModel<SmsVcode> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7422908885179209434L;
	public static SmsVcode dao = new SmsVcode();

}
