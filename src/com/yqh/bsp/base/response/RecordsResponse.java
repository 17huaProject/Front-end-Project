package com.yqh.bsp.base.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yqh.bsp.base.mvc.BaseModel;


/**
 * @author xiegh
 *
 * @date 2016-8-10
 */
public class RecordsResponse extends BaseResponse {
    private List<?> records;
    
    private long total;

    public RecordsResponse() {
        super();
    }

    public RecordsResponse (List<?> records) {
    	super();
        this.setRecords(records);
    }
    
    public RecordsResponse (List<?> records,long total) {
    	super();
        this.setRecords(records);
        if (total > 0) {
        	this.setTotal(total);
        }
    }


    public RecordsResponse(Integer code, String message) {
        super(code, message);
    }

	public List<?> getRecords() {
		return records;
	}

	public void setRecords(List<?> records) {
		if (records.size() > 0) {
			if (records.get(0) instanceof BaseModel) {
				this.records = convertToMaps((List<? extends BaseModel<?>>) records);
			} else {
				this.records = records;
			}
		} else {
			this.records = records;
		}
	}
	
	public List<Map<String ,Object>> convertToMaps(List<? extends BaseModel<?>> lists) {
		List<Map<String ,Object>> listMap = new ArrayList<>();
		for(BaseModel<?> list:lists) {
			listMap.add(list.getMap());
		}
		return listMap;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
