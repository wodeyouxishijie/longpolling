package com.doorcii.manager;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.continuation.Continuation;

import com.doorcii.beans.AppConfig;
import com.doorcii.beans.BaseMsg;
import com.doorcii.beans.ChatMsg;
import com.doorcii.beans.JSONReturnMsg;
import com.doorcii.utils.MessageReplyUtil;

public class SessionManagerImpl implements SessionManager {

	
	private SessionPool sessionPool = new UserSessionPool();
	
	private MessageReplyer messageReplyer = new MessageReplyerImpl();
	
	/**
	 * 创建新连接
	 */
	@Override
	public void newConnection(HttpServletRequest request,Continuation continuation, BaseMsg msg) throws Exception {
		Continuation oldContinuation = sessionPool.setContinuation((AppConfig)continuation.getAttribute(AppConfig.APPCONFIG),
				request.getSession().getId(), continuation);
		if(null != oldContinuation) {
			if(oldContinuation.isSuspended()) {
				resume(oldContinuation,msg);
			} else {
				oldContinuation = null;
			}
		}
	}

	@Override
	public void releaseOneId(AppConfig appConf, String userId, ChatMsg message,HttpServletRequest request)
			throws Exception {
		Map<String,Continuation> sessionMap = sessionPool.getSessionMap(appConf);
		if(null != sessionMap) {
			Iterator<Entry<String, Continuation>> iter = sessionMap.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<String, Continuation> entry = iter.next();
				resume(entry.getValue(),message);
			}
		}
	}

	@Override
	public void releaseOneConnection(HttpServletRequest request, BaseMsg msg)
			throws Exception {
		
	}

	@Override
	public void releaseOneTimeConnection(HttpServletRequest request, BaseMsg msg)
			throws Exception {
		Continuation continuation = sessionPool.getContinuation(AppConfig.buildAndCheck(request),request.getSession().getId());
		if(null != continuation ) {
			timeout(continuation,msg);
		}
	}
	
	private void timeout(Continuation continuation,BaseMsg msg) {
		MessageReplyUtil.sendReplaceMsg(messageReplyer, continuation,
				JSONReturnMsg.createJSONReturnMsg(continuation, false, msg));
	}
	
	private void resume(Continuation continuation,BaseMsg msg) {
		MessageReplyUtil.sendReplaceMsg(messageReplyer, continuation,
				JSONReturnMsg.createJSONReturnMsg(continuation, true, msg));
		continuation.resume();
	}

	public void setSessionPool(SessionPool sessionPool) {
		this.sessionPool = sessionPool;
	}

	public void setMessageReplyer(MessageReplyer messageReplyer) {
		this.messageReplyer = messageReplyer;
	}
	
}
