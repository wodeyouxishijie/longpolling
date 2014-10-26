package com.doorcii.beans;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 4024651358256943430L;

	private long id;
	
	private String userId;
	
	private String nickName;
	
	private String avatar;
	
	private String password;
	
	private Date latestLoginDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLatestLoginDate() {
		return latestLoginDate;
	}

	public void setLatestLoginDate(Date latestLoginDate) {
		this.latestLoginDate = latestLoginDate;
	}
	
}
