package com.yqh.bsp.business.mvc.invoice;

import java.math.BigDecimal;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.yqh.bsp.base.mvc.BaseService;
import com.yqh.bsp.common.model.Invoice;
import com.yqh.bsp.common.model.Order;

public class InvoiceService extends BaseService {
	
	public List<Invoice> queryList(String userId,int start,int size) {
		return Invoice.dao.find("select id,invoice_type,pattern,shipping_mode,title,amount,create_time,invoice_status from t_invoices where user_id=? order by id desc limit ?,?",userId,start,size);
	}
	
	public List<Order> queryOpenOrder(String userId,String orderType,int start,int size) {
		return Order.dao.find("select order_id,order_name,order_img,paid_amount,create_time,invoiced,status order_status from t_orders where user_id=? and invoiced=0 and paid_amount>0 and length(paid_id)>2 order by order_id desc limit ?,?", userId,start,size);
	}
	
	public Invoice queryOne(int invoiceId,String userId) {
		return Invoice.dao.findFirst("select id,invoice_type,pattern,shipping_mode,title,company_code,amount,content,bank_name,bank_card,company_info,create_time,invoice_status,order_type from t_invoices where id=? and user_id=?",invoiceId,userId);
	}
	
	public Invoice showUserData(String userId) {
		return Invoice.dao.findFirst("select invoice_type,pattern,title,company_code,content,bank_name,bank_card,company_info from t_invoices where user_id=? order by id desc limit 1",userId);
	}
	
	public boolean checkInvoiceAmount(String userId,String orderIds,float amount) {
		StringBuffer sb = new StringBuffer();
		String[] oids = orderIds.split(",");
		for (String orderId:oids) {
			sb.append("'").append(orderId).append("',");
		}
		String idStr = sb.toString();
		idStr = idStr.substring(0, idStr.length()-1);

		BigDecimal sum = Db.queryBigDecimal("select sum(paid_amount) sum_amount from t_orders where order_id in ("+idStr+") and invoiced=0 and user_id=? ",userId);
			if (sum != null) {
				if (sum.floatValue() == amount) {
					Db.update("update t_orders set invoiced=1 where order_id in ("+idStr+")");
					return true;
				} else {
					return false;
				}
			}
		return false;
	}

}
