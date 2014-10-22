package com.doorcii.utils;


public enum AppIdCenter {
	FIRST_APP(100001L,"第一个测试APP");
	
	private AppIdCenter(long appId,String desc) {
		this.appId = appId;
		this.desc = desc;
	}
	
	private long appId;
	
	private String desc;
	
	public long getAppId() {
		return appId;
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static boolean contains(long appId) {
		for(AppIdCenter appCenter : AppIdCenter.values()) {
			if(appCenter.getAppId() == appId) {
				return true;
			}
		}
		return false;
	}
	
	public static AppIdCenter getByAppId(long appId) {
		for(AppIdCenter appCenter : AppIdCenter.values()) {
			if(appCenter.getAppId() == appId) {
				return appCenter;
			}
		}
		return null;
	}
	
}
