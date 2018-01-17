package com.yqh.bsp.base.mvc;

import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.yqh.bsp.base.kit.JsonKit;
import com.yqh.bsp.base.response.BaseResponse;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.base.response.RecordResponse;
import com.yqh.bsp.base.response.RecordsResponse;

public class BaseController extends Controller {

	/*
	 */
	public void returnJson(int errcode,String errmsg) {
		if (StrKit.isBlank(errmsg)) {
		switch (errcode) {
        case Errcode.ERR_INVALID_SIGN:
        	errmsg = "签名不正确";
            break;
        case Errcode.ERR_LACK_PARAM:
        	errmsg = "缺少请求参数";
            break;
        case Errcode.ERR_REPEAT_SUBMIT:
        	errmsg = "重复提交，请稍等";
            break;
        case Errcode.ERR_NOTEXSIT_SERVICE:
        	errmsg = "服务不存在";
            break;
        case Errcode.FAIL_ACCESS_TIMEOUT:
        	errmsg = "登录超时，请重新登录";
            break;
        case Errcode.FAIL_INNER_ERROR:
        	errmsg = "内部错误";
            break;
        case Errcode.SUCC:
            errmsg = "成功";
            break;
        }
		}
		BaseResponse baseResponse = new BaseResponse(errcode,errmsg);
		renderJson(JsonKit.toCompatibleJSONString(baseResponse));
	}
	
	protected void returnJson(Object obj) {
		RecordResponse recordResponse = new RecordResponse();
		recordResponse.setRecord(obj);
		renderJson(JsonKit.toCompatibleJSONString(recordResponse));
	}
	
	protected void returnJson(List<?> records) {
//		BaseResponse baseResponse;
		BaseResponse baseResponse = new RecordsResponse(records);
//		if (records.size() == 0) {
//			baseResponse = new BaseResponse(Errcode.SUCC_NO_DATA,"未查询到数据");
//		} else {
//			baseResponse = new RecordsResponse(records);
//		}
		renderJson(JsonKit.toCompatibleJSONString(baseResponse));
		
	}
	
	protected void returnJson(List<?> records, long totalCount) {
		BaseResponse baseResponse = new RecordsResponse(records,totalCount);
		
		renderJson(JsonKit.toCompatibleJSONString(baseResponse));
	}
	
}

