package com.yqh.bsp.common.enums;

public enum TransactionType {
	/**
	 **/

	CASH_CHARGE("1001","充值"),
	CASH_GIFT("1002","Gift卡充值"),
	CASH_PRESENT("1003","活动赠送"),
	CASH_COMMISSION("1004","佣金"),
	ORDER_REFUND("1005","退款"),
	GIFT_REFUND("1006","Gift卡退款"),
	WITHDRAW_REFUND("1007","提现失败"),
	PAY_ORDER("2001","订单支付"),
	PAY_WITHDRAW("2002","提现");
	 
	private String val;
	private String name;
		
	 // 构造方法，注意：构造方法不能为public，因为enum并不可以被实例化
    private TransactionType(String val, String name) {
      this.val = val;
      this.name = name;
    }
 
    // 普通方法
    public static String getName(String val) {
      for (TransactionType c : TransactionType.values()) {
        if (val.equals(c.getVal())) {
          return c.name;
        }
      }
      return null;
    }
 
    // get set 方法
    public String getName() {
      return name;
    }
 
    public void setName(String name) {
      this.name = name;
    }
 
    public String getVal() {
      return val;
    }
 
    public void setVal(String val) {
      this.val = val;
    }

}
