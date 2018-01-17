package com.yqh.bsp.business.mvc.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yqh.bsp.base.kit.JsonKit;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.utils.Base64Util;
import com.yqh.bsp.utils.HttpUtil;


/**
 * 
{"documentNo": "0001",
    "invoiceSort": 1,
    "priceWay": 1,
    "custCode": "",
    "custName": "浙B34231",
    "custTaxNo": "",
    "custAddress": "",
    "custBankAccount": "",
    "sellerAddress": "",
    "sellerBankAccount": "",
    "remark": "",
    "printer": "",
    "review": "",
    "remittee": "",
    "inventoryGoodsName": "",
    "ref01": "",
    "ref02": "",
    "ref03": "",
    "ref04": "",
    "ref05": "",
    "details":
}
 * @author xiegh
 *
 * @date 2016-11-7
 */
public class TestService extends BaseService{

	public static String invoicePrint(String clientKey,String url) {
		String result = "";
		JSONObject subObj = new JSONObject();
		subObj.put("action", "create");   //货物
		subObj.put("name", "王氏果菜测试1");    //规格型号 
		subObj.put("mobile", "13381143958");         //单位
		subObj.put("group","购物");        //单价
		subObj.put("custid", "123456780");   //数量,升数
		subObj.put("tel", "01064916144");      //金额
		subObj.put("address", "北京市朝阳区安慧里二区4号楼底商");    //税率
		subObj.put("uname", "杨娜");    //税率
		subObj.put("endtime", "2018-09-09 23:59:59");    //税率
		subObj.put("serviceid", "130");    //税率
		
		Map arr = new HashMap();
		arr.put("auth", Base64Util.encode("dxhyjk|dxhyjk!@#123"));
		arr.put("data", Base64Util.encode(subObj.toJSONString()));
		
		String inString = JsonKit.toCompatibleJSONString(arr);

		result = HttpUtil.postWithParamMap(url, arr,"UTF-8");
		
		System.out.println("ok->"+result);
		
		return result;
	}
	
	public static void main(String[] args) {
		String url = "http://p.yellowpage.com.cn/index.php/api/newdxhyapi/newindex";
		TestService.invoicePrint("aaa",url);
		
	}
}
