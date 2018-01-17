package com.yqh.bsp.base.response;

/**
 * 
 * @author xiegh
 *
 * @date 2016-8-10
 *  200	成功
 *  300	请求错误(XXX)
 *  301	签名无效
 *  302	参数无效或缺失
 *  303	重复提交
 *  304	接口不被支持
 *  305	请求过快
 *  400	失败（XXXXX）
 *  401	访问超时，重新登录
 *  402	系统内部错误
 *
 */
public class Errcode {
	
	public static final int SUCC = 200;
	
	public static final int SUCC_NO_DATA = 201;
	//3XX   请求层的错误
	public static final int ERR = 300;
	
	public static final int ERR_INVALID_SIGN = 301;
	
	public static final int ERR_LACK_PARAM = 302;
	
	public static final int ERR_REPEAT_SUBMIT = 303;
	
	public static final int ERR_NOTEXSIT_SERVICE = 304;
	//4XX     业务层处理失败
	public static final int FAIL = 400;
	
	public static final int FAIL_ACCESS_TIMEOUT = 401;
	
	public static final int FAIL_INNER_ERROR = 402;
	
	public static final int FAIL_UNBIND_IDCARD = 403;
	

}
