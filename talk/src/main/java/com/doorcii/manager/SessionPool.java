package com.doorcii.manager;

import java.util.Map;

import org.eclipse.jetty.continuation.Continuation;

import com.doorcii.beans.AppConfig;

public interface SessionPool {
	
	public Continuation setContinuation(AppConfig appConfig,String sessionId,Continuation continuation);
	
	public Continuation getContinuation(AppConfig appConfig,String sessionId);
	
	public Map<String,Continuation> getSessionMap(AppConfig appConfig);
	
	public Map<Long,Map<Integer,Map<String,Map<String,Continuation>>>> getContinuationMap();
}
