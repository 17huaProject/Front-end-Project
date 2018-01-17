package com.yqh.bsp.base.response;

import com.yqh.bsp.base.mvc.BaseModel;


/**
 * @author xiegh
 *
 * @date 2016-8-10
 */
public class RecordResponse extends BaseResponse {
    private Object record;

    public RecordResponse() {
        super();
    }
    
    public RecordResponse (Object datum) {
        this.record = datum;
    }

    public RecordResponse(Integer code, String message) {
        super(code, message);
    }

    public RecordResponse setRecord(Object record) {
    	if (record instanceof BaseModel) {
    		 this.record = ((BaseModel) record).getMap();
    	} else {
    		 this.record = record;
    	}
       
        return this;
    }

    public Object getRecord() {
        return record;
    }
}
