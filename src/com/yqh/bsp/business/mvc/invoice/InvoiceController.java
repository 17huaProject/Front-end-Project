package com.yqh.bsp.business.mvc.invoice;

import java.util.Date;

import com.jfinal.aop.Before;
import com.yqh.bsp.base.annotation.Action;
import com.yqh.bsp.base.mvc.BaseController;
import com.yqh.bsp.base.response.Errcode;
import com.yqh.bsp.common.entity.LoginUser;
import com.yqh.bsp.common.enums.InvoiceStatus;
import com.yqh.bsp.common.interceptor.LoginedAuthInterceptor;
import com.yqh.bsp.common.model.Invoice;
import com.yqh.bsp.common.template.ContantTemplate;

@Action(controllerKey="/invoice")
@Before(LoginedAuthInterceptor.class)
public class InvoiceController extends BaseController {
	private static InvoiceService invoiceService = new InvoiceService();

	public void index() {
		Integer pageNo =  getParaToInt("page_no");     //开始页数
		Integer pageSize =  getParaToInt("page_size"); //每页显示
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = ContantTemplate.PAGESIZE;
		}
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		returnJson(invoiceService.queryList(userId, (pageNo-1)*pageSize, pageSize));
	}
	
	public void indexOpenOrder() {
		String orderType = getPara("order_type");
		Integer pageNo =  getParaToInt("page_no");     //开始页数
		Integer pageSize =  getParaToInt("page_size"); //每页显示
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = ContantTemplate.PAGESIZE;
		}
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		returnJson(invoiceService.queryOpenOrder(userId, orderType, (pageNo-1)*pageSize, pageSize));
	}
	
	public void show() {
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		Integer invoiceId = getParaToInt("invoice_id");
		if (invoiceId == null) {
			returnJson(Errcode.FAIL,"发票id不存在");
			return;
		}
		returnJson(invoiceService.queryOne(invoiceId,userId));
	}
	
	public void showUserData() {
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		
		returnJson(invoiceService.showUserData(userId));
	}
	
	@Before(InvoiceValidator.class)
	public void add() {
		String invoiceType = getPara("invoice_type");
		Invoice invoice = new Invoice();
		LoginUser lUser = getSessionAttr("loginUser");
		String userId = lUser.getUserId();
		Float amount = Float.parseFloat(getPara("amount"));
		invoice.put("user_id", userId);
		invoice.put("order_type", getPara("order_type"));
		invoice.put("invoice_type", invoiceType);
		invoice.put("pattern", getPara("pattern"));
		invoice.put("title", getPara("title"));
		invoice.put("amount", amount);
		String orderIds = getPara("order_ids");
		invoice.put("order_ids", orderIds);
		if (!"PERSON".equals(invoiceType)) {
			invoice.put("company_code", getPara("company_code"));
			invoice.put("company_info", getPara("company_info"));
			invoice.put("bank_name", getPara("bank_name"));
			invoice.put("bank_card", getPara("bank_card"));
		} 
		String shippingMode = getPara("shipping_mode");
		invoice.put("shipping_mode", shippingMode);
		if ("ONSITE".equals(shippingMode)) {
		} else {
		}
		boolean flag = invoiceService.checkInvoiceAmount(userId, orderIds, amount);
		if (flag) {
			invoice.set("invoice_status", InvoiceStatus.HANDLING.toString());
			invoice.set("create_time", new Date());
			boolean a = invoice.save();
			if (a) {
				returnJson(Errcode.SUCC,"申请发票成功");
			} else {
				returnJson(Errcode.FAIL,"申请发票失败");
			}
		} else {
			returnJson(Errcode.FAIL,"申请发票金额错误！");
		}

	}
	
}
