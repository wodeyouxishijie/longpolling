package com.doorcii.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

import com.alibaba.fastjson.JSONObject;
import com.doorcii.beans.AppConfig;
import com.doorcii.beans.BaseMsg;
import com.doorcii.beans.ChatMsg;
import com.doorcii.beans.JSONReturnMsg;

public class ChatManagerImpl implements ChatManager {

	private SessionManager sessionManager = new SessionManagerImpl();
	
	private static final Long EXPIRED_TIME = 30000L;
	
	@Override
	public String handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		final Continuation continuation = ContinuationSupport.getContinuation(request);
		response.setContentType("text/json");
		
		/** 初始化连接*/
		if(continuation.isInitial()) {
			AppConfig appConf = AppConfig.buildAndCheck(request);
			if(null == appConf) {
				return "PARAM_ERROR";
			}
			System.out.println("connection suspend..");
			/** 组装基本continuation 参数**/
			assembleContinuation(continuation,appConf,response);
			/** hold住连接**/
			sessionManager.newConnection(request,continuation, getOverwritedMsg(request));
			/** 连接过期**/
		} else if(continuation.isExpired()) {
			System.out.println("timeout release connection..");
			sessionManager.releaseOneTimeConnection(request, getTimeoutMsg(request));
		}
		return null;
	}

	@Override
	public String sendMessage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/json");
		AppConfig appConf = AppConfig.buildAndCheck(request);
		if(null == appConf) {
			return "PARAM_ERROR";
		}
		/** 点对点发送时设置该参数，暂时先不支持，内存查找方案还没想好  **/
		String targetUserId = request.getParameter("_tarUId");
		String message = request.getParameter("_message");
		ChatMsg cm = new ChatMsg();
		cm.setMsg(message);
		cm.setUserId("123456");
		cm.setUserNick("nickName");
		cm.setAvatar("images/avatar-female.jpg");
		sessionManager.releaseOneId(appConf, targetUserId, cm, request);
		responseOK(appConf,response);
		return null;
	}
	
	private void responseOK(AppConfig appConf,HttpServletResponse response) throws Exception {
		JSONReturnMsg jr = new JSONReturnMsg();
		jr.setSuccess(true);
		jr.setAppId(appConf.getAppId().getAppId());
		jr.setId(appConf.getUniqueId());
		jr.setTypeId(appConf.getTypeId());
		response.getWriter().write(JSONObject.toJSONString(jr));
		response.getWriter().flush();
	}
	
	private void assembleContinuation(final Continuation continuation,AppConfig appConf,HttpServletResponse response) {
		continuation.setTimeout(EXPIRED_TIME);
		continuation.suspend(response);
		continuation.setAttribute(AppConfig.APPCONFIG, appConf);
	}

	private BaseMsg getOverwritedMsg(HttpServletRequest request) {
		return null;
	}
	
	private BaseMsg getTimeoutMsg(HttpServletRequest request) {
		BaseMsg bm = new ChatMsg();
		bm.setErrorMsg("timeout");
		return bm;
	}
	
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
}
