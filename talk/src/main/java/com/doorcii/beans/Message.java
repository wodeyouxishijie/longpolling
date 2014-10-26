package com.doorcii.beans;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 5919039495959228271L;
	
	private long messageId;
	
	private String userId;
	
	private String msg;
	
	private long creatTime;
	
	/**
	 * 1.表示发给所有人
	 * 0.点对点发送
	 */
	private int type ;
	
	/**
	 * 1为null
	 * 2为具体Id
	 */
	private String receiverId;
	
	private long appId;
	
	private int typeId;
	

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

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(long creatTime) {
		this.creatTime = creatTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
}
