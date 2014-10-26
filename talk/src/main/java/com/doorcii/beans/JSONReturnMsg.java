package com.doorcii.beans;

import java.util.List;

import org.eclipse.jetty.continuation.Continuation;

public class JSONReturnMsg {
	
	private long appId;
	
	private int typeId;
	
	private String id;
	
	private boolean success;
	
	private String message;
	
	private List<ChatMsg> data;
	
	public static JSONReturnMsg createJSONReturnMsg(Continuation continuation,boolean success,List<ChatMsg> msgList) {
		JSONReturnMsg jr = new JSONReturnMsg();
		AppConfig appConfig = (AppConfig)continuation.getAttribute(AppConfig.APPCONFIG);
		jr.setAppId(appConfig.getAppId().getAppId());
		jr.setTypeId(appConfig.getTypeId());
		jr.setId(appConfig.getUniqueId());
		jr.setSuccess(success);
		jr.setData(msgList);
		return jr;
	}
	
	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
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
	
	public List<ChatMsg> getData() {
		return data;
	}

	public void setData(List<ChatMsg> data) {
		this.data = data;
	}

}
