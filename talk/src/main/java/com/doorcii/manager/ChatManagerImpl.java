package com.doorcii.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.springframework.web.bind.ServletRequestUtils;

import com.alibaba.fastjson.JSONObject;
import com.doorcii.beans.AppConfig;
import com.doorcii.beans.ChatMsg;
import com.doorcii.beans.JSONReturnMsg;
import com.doorcii.beans.UserInfo;

public class ChatManagerImpl implements ChatManager {

	private SessionManager sessionManager = null;
	
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
			
			/** 组装基本continuation 参数**/
			assembleContinuation(continuation,appConf,response,request);
			
			System.out.println("connection suspend..");
			/** hold住连接**/
			sessionManager.newConnection(request,continuation);
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
		UserInfo userInfo = (UserInfo)request.getSession().getAttribute(AppConfig.USER_KEY);
		/** 点对点发送时设置该参数，暂时先不支持，内存查找方案还没想好  **/
		String message = request.getParameter("_message");
		ChatMsg cm = new ChatMsg();
		cm.setMsg(message);
		cm.setUserId(userInfo.getUserId());
		cm.setUserNick(userInfo.getNickName());
		cm.setAvatar(userInfo.getAvatar());
		
		sessionManager.releaseOneId(appConf, userInfo.getUserId(), cm, request);
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
	
	
	private void assembleContinuation(final Continuation continuation,AppConfig appConf,
			HttpServletResponse response,HttpServletRequest request) throws Exception {
		continuation.setTimeout(EXPIRED_TIME);
		continuation.suspend(response);
		continuation.setAttribute(AppConfig.APPCONFIG, appConf);
		continuation.setAttribute(AppConfig.VERSION_ID, ServletRequestUtils.getLongParameter(request,AppConfig.VERSION_ID,0L));
		UserInfo userInfo = (UserInfo)request.getSession().getAttribute(AppConfig.USER_KEY);
		continuation.setAttribute(AppConfig.USER_ID, null ==userInfo?null:userInfo.getUserId());
	}

	private ChatMsg getTimeoutMsg(HttpServletRequest request) {
		ChatMsg bm = new ChatMsg();
		bm.setErrorMsg("timeout");
		return bm;
	}
	
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
}
