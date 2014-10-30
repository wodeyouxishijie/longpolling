package com.doorcii.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.doorcii.beans.AppConfig;
import com.doorcii.beans.UserInfo;
import com.doorcii.ibatis.UserDAO;

public class CacheManagerImpl implements CacheManager {
	
	private RedisTemplate<String,Long>  longCounterTemplate; 
	
	private RedisTemplate<String,UserInfo>  userTemplate;  
	
	private UserDAO userDAO;
	
	private static final String USER_ARE = "_user_info_";
	
	private static final String USER_MSG_COUNT = "_counter_";
	
	private static final String COUNTER = "_counter_";

	@Override
	public long getMessageRedId(String userId) throws Exception {
		Long maxId = longCounterTemplate.opsForValue().get(USER_MSG_COUNT+userId);
		if(null != maxId) {
			return maxId;
		}
		return 0;
	}

	@Override
	public void setMessageRedId(String userId, long messageId) throws Exception {
		longCounterTemplate.opsForValue().set(USER_MSG_COUNT+userId, messageId);
	}

	@Override
	public List<UserInfo> batchGetUserInfo(Set<String> userSet)
			throws Exception {
		List<UserInfo> userList = new ArrayList<UserInfo>();
		for(String userId : userSet) {
			UserInfo userInfo = getUser(userId);
			if(userInfo!=null) {
				userList.add(userInfo);
			}
		}
		return userList;
	}

	@Override
	public UserInfo getUser(String userId) throws Exception {
		UserInfo userInfo = userTemplate.opsForValue().get(USER_ARE+userId);
		if(null != userInfo) {
			UserInfo userPersist = userDAO.getUserById(userId);
			if(null != userPersist) {
				this.setUser(userPersist);
				return userPersist;
			}
		}
		return userInfo;
	}

	@Override
	public void setUser(UserInfo user) throws Exception {
		userTemplate.opsForValue().set(USER_ARE+user.getUserId(), user,24,TimeUnit.HOURS);
	}

	@Override
	public Long incrKey(AppConfig appConf, String key) throws Exception {
		return longCounterTemplate.opsForValue().increment(appConf.getAppId()+"_"+appConf.getTypeId()+"_"+COUNTER+key, 1);
	}

	@Override
	public Long getIncrValue(AppConfig appConf, String key) throws Exception {
		Long value = longCounterTemplate.opsForValue().increment(appConf.getAppId()+"_"+appConf.getTypeId()+"_"+COUNTER+key,0);
		return value==null?0L:value;
	}

	public void setLongCounterTemplate(
			RedisTemplate<String, Long> longCounterTemplate) {
		this.longCounterTemplate = longCounterTemplate;
	}

	public void setUserTemplate(RedisTemplate<String, UserInfo> userTemplate) {
		this.userTemplate = userTemplate;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
