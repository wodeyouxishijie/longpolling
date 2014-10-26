package com.doorcii.manager;

import java.util.List;
import java.util.Set;

import com.doorcii.beans.AppConfig;
import com.doorcii.beans.UserInfo;

public interface CacheManager {
	
	public void setStringValue(AppConfig appConf,String key,String value,long expireTime) throws Exception;
	
	public String getStringValue(AppConfig appConf,String key) throws Exception;
	
	public void setObject(AppConfig appConf,String key,Object obj) throws Exception;
	
	public Object getObject(AppConfig appConf,String key) throws Exception;
	
	public UserInfo getUser(String userId) throws Exception;
	
	public List<UserInfo> batchGetUserInfo(Set<String> userSet) throws Exception;
	
	public void setUser(UserInfo user) throws Exception;
	
	public long getMessageRedId(String userId) throws Exception;
	
	public void setMessageRedId(String userId,long messageId) throws Exception;
	
	public Long incrKey(AppConfig appConf,String key) throws Exception;
	
	public Long getIncrValue(AppConfig appConf,String key) throws Exception;
}
