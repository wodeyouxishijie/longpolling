package com.doorcii.manager;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.continuation.Continuation;

import com.doorcii.beans.AppConfig;
import com.doorcii.beans.BaseMsg;
import com.doorcii.beans.ChatMsg;

/**
 * session管理器
 * @author Administrator
 *
 */
public interface SessionManager {
	
	public void newConnection(HttpServletRequest request,Continuation continuation,BaseMsg msg) throws Exception;
	
	public void releaseOneConnection(HttpServletRequest request,BaseMsg msg) throws Exception;
	
	public void releaseOneTimeConnection(HttpServletRequest request,BaseMsg msg) throws Exception;
	
	public void releaseOneId(AppConfig appConf,String userId,ChatMsg message,HttpServletRequest request) throws Exception;
	
}
