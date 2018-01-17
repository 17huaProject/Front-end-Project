package com.yqh.bsp.business.mvc.shtelecom;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.utils.HttpUtil;
import com.yqh.bsp.utils.RegularUtil;

public class WXActivityService extends BaseService {

//	public List<Venue>  queryVenueList(int pageNo,int pageSize) {
//		String url = "select order_id,user_id,amount,create_time,bank_card,fee,pay_time,pay_status from t_order_drawmoney where user_id=? ";
//		url = url + " order by create_time desc limit ?,?";
//		return Venue.dao.find(url,(pageNo-1)*pageSize,pageSize);
//	}
	
	public static void main(String[] args) {
		String uri = "/api/weixin/%s/address";
		String openId = "oMEL10yxri1Q0bKSVonglua5iSs4";
		String address = "试测试测试测试测试测试测试测试";
		
		String url = WXActivityController.PREURL+String.format(uri, openId);
		Map<String,String> params = new HashMap<String, String>();
		params.put("openId", openId);
		params.put("address", address);
		try {
			String resp = HttpUtil.postWithParamMapNoState(url, params, "UTF-8");
			System.out.println(resp);
			JSONObject json = JSONObject.parseObject(resp);
			if(json.getInteger("code") == 200) {
			} else {
				String msg = json.getJSONObject("data").getString("details");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
