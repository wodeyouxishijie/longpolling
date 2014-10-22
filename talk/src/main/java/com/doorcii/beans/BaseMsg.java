package com.doorcii.beans;

import java.util.Date;

public abstract class BaseMsg {
	
	public long serverTime = new Date().getTime();
	
	public String errorMsg;

	public long getServerTime() {
		return serverTime;
	}

	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
