package com.doorcii.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.eclipse.jetty.continuation.Continuation;

import com.doorcii.beans.AppConfig;
import com.doorcii.utils.AppIdCenter;

/**
 * 还是犹豫了一下
 * sessionPool干脆就简单管理
 * 一下session的池
 * @author Administrator
 */
public class UserSessionPool implements SessionPool {
	
	private static final Logger logger = Logger.getLogger(CacheManagerImpl.class);
	
	private Map<Long,Map<Integer,Map<String,Map<String,Continuation>>>> contiMap = new HashMap<Long, Map<Integer,Map<String,Map<String,Continuation>>>>(100);
	
	private Timer timer = null;
	
	public void init() {
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Map<Integer, Map<String, Map<String, Continuation>>> appMap = contiMap.get(AppIdCenter.FIRST_APP.getAppId());
				if(null != appMap) {
					Map<String, Map<String, Continuation>> typeMap = appMap.get(1);
					if(null != typeMap) {
						for(Entry<String, Map<String, Continuation>> entry : typeMap.entrySet()) {
							logger.info("***uniqueId="+entry.getKey()+"****count="+(entry.getValue()==null?0:entry.getValue().size())+"*****");
						}
					}
				}
			}
			
		}, 1000,5000);
	}
	
	/**
	 * continuation 新增
	 * @param session
	 * @param continuation
	 * @return
	 */
	public Continuation setContinuation(AppConfig appConfig,String sessionId,Continuation continuation) {
		Map<Integer,Map<String,Map<String,Continuation>>> appMap = contiMap.get(appConfig.getAppId().getAppId());
		if(null == appMap) {
			Map<Integer,Map<String,Map<String,Continuation>>> typeMap = new HashMap<Integer,Map<String,Map<String,Continuation>>>(100);
			Map<String,Map<String,Continuation>> idMap = new HashMap<String,Map<String,Continuation>>();
			Map<String,Continuation> sessionIdMap = new ConcurrentHashMap<String, Continuation>(50000);
			sessionIdMap.put(sessionId, continuation);
			idMap.put(appConfig.getUniqueId(), sessionIdMap);
			typeMap.put(appConfig.getTypeId(), idMap);
			appMap = typeMap;
			contiMap.put(appConfig.getAppId().getAppId(), typeMap);
			return null;
		}
		Map<String, Map<String, Continuation>> tpMap = appMap.get(appConfig.getTypeId());
		if(tpMap == null) {
			Map<String,Map<String,Continuation>> idMap = new HashMap<String,Map<String,Continuation>>();
			Map<String,Continuation> sessionIdMap = new ConcurrentHashMap<String, Continuation>(50000);
			sessionIdMap.put(sessionId, continuation);
			idMap.put(appConfig.getUniqueId(), sessionIdMap);
			tpMap = idMap;
			appMap.put(appConfig.getTypeId(), idMap);
			return null;
		}
		Map<String, Continuation> sessMap = tpMap.get(appConfig.getUniqueId());
		if(sessMap == null) {
			Map<String,Continuation> sessionIdMap = new ConcurrentHashMap<String, Continuation>(50000);
			sessionIdMap.put(sessionId, continuation);
			sessMap = sessionIdMap;
			tpMap.put(appConfig.getUniqueId(), sessionIdMap);
			return null;
		}
		
		return sessMap.put(sessionId, continuation);
	}
	
	/**
	 * 获取链接
	 * @param session
	 * @return
	 */
	public Continuation getContinuation(AppConfig appConfig,String sessionId) {
		return contiMap.get(appConfig.getAppId().getAppId()).get(appConfig.getTypeId()).get(appConfig.getUniqueId()).get(sessionId);
	}
	
	public Map<String,Continuation> getSessionMap(AppConfig appConfig) {
		if(null != contiMap.get(appConfig.getAppId().getAppId()) && null != contiMap.get(appConfig.getAppId().getAppId()).get(appConfig.getTypeId()))
			return contiMap.get(appConfig.getAppId().getAppId()).get(appConfig.getTypeId()).get(appConfig.getUniqueId());
		else 
			return null;
	}
	
	/**
	 * 获取整个连接池
	 * @return
	 */
	public Map<Long,Map<Integer,Map<String,Map<String,Continuation>>>> getContinuationMap() {
		return contiMap;
	}
	
}
