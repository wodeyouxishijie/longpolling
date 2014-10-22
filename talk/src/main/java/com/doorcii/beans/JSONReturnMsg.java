package com.doorcii.beans;

import org.eclipse.jetty.continuation.Continuation;

public class JSONReturnMsg {
	
	private long appId;
	
	private int typeId;
	
	private String id;
	
	private boolean success;
	
	private BaseMsg data;
	
	public static JSONReturnMsg createJSONReturnMsg(Continuation continuation,boolean success,BaseMsg msg) {
		JSONReturnMsg jr = new JSONReturnMsg();
		AppConfig appConfig = (AppConfig)continuation.getAttribute(AppConfig.APPCONFIG);
		jr.setAppId(appConfig.getAppId().getAppId());
		jr.setTypeId(appConfig.getTypeId());
		jr.setId(appConfig.getUniqueId());
		jr.setSuccess(success);
		jr.setData(msg);
		return jr;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getAppId() {
		return appId;
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public BaseMsg getData() {
		return data;
	}

	public void setData(BaseMsg data) {
		this.data = data;
	}

}
