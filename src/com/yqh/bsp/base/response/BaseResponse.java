package com.yqh.bsp.base.response;


/**
 * @author xiegh
 *
 * @date 2016-8-10
 */
public class BaseResponse {
	
    private Integer errcode = Errcode.SUCC;
    
    private String errmsg = "成功";

    public BaseResponse() {
    }

    public BaseResponse(Integer code, String message) {
        this.errcode = code;
        this.errmsg = message;
    }

    public BaseResponse setCode(Integer code) {
        this.errcode = code;
        return this;
    }

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
 
    
}
