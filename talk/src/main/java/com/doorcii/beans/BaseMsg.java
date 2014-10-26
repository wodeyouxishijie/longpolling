package com.doorcii.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseMsg {
	
	public long id;
	
	public long serverTime = new Date().getTime();
	
	public String errorMsg;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
	
	public String getTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");
		return sdf.format(new Date(serverTime));
	}
	
}
