package com.doorcii.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.ShardedJedis;

import com.doorcii.beans.AppConfig;
import com.doorcii.beans.UserInfo;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class CacheManagerImpl implements CacheManager {

	private ShardedJedis jedisClient;
	
	private static final String USER_ARE = "_user_info_";
	
	private static final String USER_MSG_COUNT = "_counter_";
	
	private static final String COUNTER = "_counter_";
	
	@Override
	public void setStringValue(AppConfig appConf,String key, String value,long expireTime) throws Exception {
		if(expireTime < 0) {
			jedisClient.set(getStringKey(appConf,key), value);
		} else {
			jedisClient.set(getStringKey(appConf,key), value, null, "PX", expireTime);
		}
	}

	@Override
	public long getMessageRedId(String userId) throws Exception {
		String maxId = jedisClient.get(USER_MSG_COUNT+userId);
		if(StringUtils.isNotBlank(maxId)) {
			return Long.valueOf(maxId);
		}
		return 0;
	}

	@Override
	public void setMessageRedId(String userId, long messageId) throws Exception {
		jedisClient.set(USER_MSG_COUNT+userId, String.valueOf(messageId));
	}

	@Override
	public String getStringValue(AppConfig appConf,String key) throws Exception {
		return jedisClient.get(getStringKey(appConf,key));
	}

	@Override
	public void setObject(AppConfig appConf,String key, Object obj) throws Exception {
		jedisClient.set(getStringKey(appConf,key).getBytes(), null == obj?null:getSerializedObjectByte(obj));
	}

	@Override
	public List<UserInfo> batchGetUserInfo(Set<String> userSet)
			throws Exception {
		List<UserInfo> userList = new ArrayList<UserInfo>();
		for(String userId : userSet) {
			userList.add(getUser(userId));
		}
		return null;
	}

	@Override
	public UserInfo getUser(String userId) throws Exception {
		byte bytes[] = jedisClient.get((USER_ARE+userId).getBytes());
		return getDeserilizedUser(bytes);
	}

	@Override
	public void setUser(UserInfo user) throws Exception {
		jedisClient.set((USER_ARE+user.getUserId()).getBytes(), getSerializedUserByte(user));
	}

	@Override
	public Long incrKey(AppConfig appConf, String key) throws Exception {
		return jedisClient.incr(appConf.getAppId()+"_"+appConf.getTypeId()+"_"+COUNTER+key);
	}

	@Override
	public Long getIncrValue(AppConfig appConf, String key) throws Exception {
		return jedisClient.incrBy(appConf.getAppId()+"_"+appConf.getTypeId()+"_"+COUNTER+key, 0);
	}

	private byte[] getSerializedObjectByte(Object obj) {
		Kryo serizableUtil = new Kryo();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Output output = new Output(baos);
		serizableUtil.writeClassAndObject(output, obj);
		output.flush();
		return baos.toByteArray();
	}
	
	private Object getDeserilizedObject(byte [] bytes) {
		if(null == bytes) {
			return null;
		}
		Kryo serizableUtil = new Kryo();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		Input input = new Input(bais);
		return serizableUtil.readClassAndObject(input);
	}
	
	private UserInfo getDeserilizedUser(byte [] bytes) {
		if(null == bytes) {
			return null;
		}
		Kryo serizableUtil = new Kryo();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		Input input = new Input(bais);
		return serizableUtil.readObject(input, UserInfo.class);
	}
	
	private byte[] getSerializedUserByte(UserInfo userInfo) {
		Kryo serizableUtil = new Kryo();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Output output = new Output(baos);
		serizableUtil.writeClassAndObject(output, userInfo);
		output.flush();
		return baos.toByteArray();
	}
	
	@Override
	public Object getObject(AppConfig appConf,String key) throws Exception {
		byte[] bytes = jedisClient.get(getStringKey(appConf,key).getBytes());
		if(null != bytes) {
			return getDeserilizedObject(bytes);
		}
		return null;
	}

	
	public void setJedisClient(ShardedJedis jedisClient) {
		this.jedisClient = jedisClient;
	}
	
	private String getStringKey(AppConfig appConf,String key) {
		return appConf.getAppId()+"_"+appConf.getTypeId()+"_"+key;
	}
	
}
