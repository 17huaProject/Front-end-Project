package com.yqh.bsp.common.enums;

public enum ValidStatus {
	INVALID(0),VALID(1),END(2);
	
    private int val;
	
	private ValidStatus(int v) {
		this.val = v;
	}

	public int getVal() {
		return val;
	}


}
