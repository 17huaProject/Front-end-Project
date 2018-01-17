package com.yqh.bsp.common.tools;

import com.yqh.bsp.common.enums.UserType;
import com.yqh.bsp.utils.DateUtil;


public class IdGenerator {
	private static int seq = 0;
	private static int seqno =1;
	public static int userIdSeq = 1;
	public static int userIdMidSeq = 11;
	public static String preUserIdSeq = "1700";

	public static String serverId = "1";

	
	synchronized public static String generatId() {
		if ((seqno + 1) >= 100000) {
			seqno =1;
		} else
			seqno++;
		String ID = serverId+String.format("%05d", seqno);
		return ID;
	}
	
	synchronized public static String generateUserId() {
		if ((userIdSeq + 1) >= 100000) {
			userIdSeq =1;
			userIdMidSeq++;
		} else {
			userIdSeq++;
		}
		String ID = preUserIdSeq+userIdMidSeq+String.format("%05d", userIdSeq);
		return ID;
	}
	
	
	synchronized public static String generateOrderId(String orderType,String innerStr) {	
		if ((seq + 1) > 99998) {
			seq =1;
		} else {
			seq++;
		}
		StringBuilder ID = new StringBuilder();
		ID.append(orderType);
		ID.append(DateUtil.getNowFormat("yyyyMMddHHmm"));
		ID.append(serverId);	
		ID.append(innerStr);	
		ID.append(String.format("%05d", seq));
		return ID.toString();
	}
	
	
	
	
	
	

	
}
