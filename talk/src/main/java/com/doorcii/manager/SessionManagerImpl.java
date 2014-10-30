package com.doorcii.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.continuation.Continuation;
import org.springframework.web.bind.ServletRequestUtils;

import com.doorcii.beans.AppConfig;
import com.doorcii.beans.ChatMsg;
import com.doorcii.beans.JSONReturnMsg;
import com.doorcii.beans.Message;
import com.doorcii.beans.UserInfo;
import com.doorcii.ibatis.MessageDAO;
import com.doorcii.utils.MessageReplyUtil;

public class SessionManagerImpl implements SessionManager {

	
	private SessionPool sessionPool = null;
	
	private MessageReplyer messageReplyer = null;
	
	private MessageDAO messageDAO;
	
	private CacheManager cacheManager;
	
	/**
	 * 创建新连接
	 */
	@Override
	public void newConnection(HttpServletRequest request,Continuation continuation) throws Exception {
		Long versionId = ServletRequestUtils.getLongParameter(request, AppConfig.VERSION_ID,0L);
		AppConfig appConfig = (AppConfig)continuation.getAttribute(AppConfig.APPCONFIG);
		Long oldVersionId = cacheManager.getIncrValue(appConfig, AppConfig.VERSION_ID);
		if(oldVersionId <= versionId) {
			Continuation oldContinuation = sessionPool.setContinuation((AppConfig)continuation.getAttribute(AppConfig.APPCONFIG),
					request.getSession().getId(), continuation);
			if(null != oldContinuation) {
				if(oldContinuation.isSuspended()) {
					resume(oldContinuation);
				} else {
					oldContinuation = null;
				}
			}
		} else {
			Long maxMessageId = ServletRequestUtils.getLongParameter(request, AppConfig.MESSAGE_MAX_ID,0L);
			UserInfo user = (UserInfo)request.getSession().getAttribute(AppConfig.USER_KEY);
			List<Message> messageList = messageDAO.getMessageRange(maxMessageId,appConfig.getAppId().getAppId(),appConfig.getTypeId());
			continuation.setAttribute(AppConfig.MESSAGEKEY, message2ChatMsg(messageList,null==user?null:user.getUserId(),oldVersionId));
			resume(continuation);
		}
	}

	@Override
	public void releaseOneId(AppConfig appConf, String userId, ChatMsg message,HttpServletRequest request)
			throws Exception {
		Map<String,Continuation> sessionMap = sessionPool.getSessionMap(appConf);
		Message msg = buildMessageBean(message,appConf);
		/** 保存消息实体 **/
		int count = messageDAO.saveMessage(msg);
		if(count > 0) {
			Long versionId = cacheManager.incrKey(appConf, AppConfig.VERSION_ID);
			message.setId(versionId);
			message.setMessageId(msg.getMessageId());
			if(null != sessionMap) {
				Iterator<Entry<String, Continuation>> iter = sessionMap.entrySet().iterator();
				while(iter.hasNext()) {
					Entry<String, Continuation> entry = iter.next();
					Continuation continuation = entry.getValue();
					if(continuation.isSuspended()) {
						Long vId = (Long)continuation.getAttribute(AppConfig.VERSION_ID);
						String userIds = (String)continuation.getAttribute(AppConfig.USER_ID);
						/**
						 * 当当前版本号大于已有版本号立即返回
						 */
						if(null != vId && versionId > vId) {
							if(request.getSession().getId().equals(entry.getKey()) || (null != userIds && userIds.equals(userId))) {
								message.setSelf(true);
							} else {
								message.setSelf(false);
							}
							entry.getValue().setAttribute(AppConfig.MESSAGEKEY, Arrays.asList(message));
							resume(entry.getValue());
						}
					} else {
						iter.remove();
					}
				}
			}
		}
	}
	
	private Message buildMessageBean(ChatMsg message,AppConfig appConf) {
		Message msg  = new Message();
		msg.setCreatTime(message.getServerTime());
		msg.setMsg(message.getMsg());
		msg.setReceiverId(message.getTargetUserId());
		msg.setAppId(appConf.getAppId().getAppId());
		msg.setTypeId(appConf.getTypeId());
		if(StringUtils.isNotBlank(message.getTargetUserId())) {
			msg.setType(0);
		} else {
			msg.setType(1);
		}
		msg.setUserId(message.getUserId());
		return msg;
	}

	private List<ChatMsg> message2ChatMsg(List<Message> messageList,String currentUserId,Long versionId) {
		if(null != messageList && messageList.size() > 0) {
			List<ChatMsg> msgList = new ArrayList<ChatMsg>();
			Set<String> userSet = new HashSet<String>();
			for(Message message : messageList) {
				ChatMsg cm = new ChatMsg();
				cm.setMsg(message.getMsg());
				cm.setId(versionId);
				cm.setMessageId(message.getMessageId());
				cm.setUserId(message.getUserId());
				cm.setTargetUserId(message.getReceiverId());
				cm.setServerTime(message.getCreatTime());
				userSet.add(message.getUserId());
				msgList.add(cm);
			}
			Map<String,UserInfo> userMap = new HashMap<String,UserInfo>();
			try {
				List<UserInfo> userInfoList = cacheManager.batchGetUserInfo(userSet);
				if(null != userInfoList && userInfoList.size() > 0) {
					for(UserInfo userInfo : userInfoList) {
						userMap.put(userInfo.getUserId(), userInfo);
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			for(ChatMsg chatMsg: msgList) {
				UserInfo userInfo = userMap.get(chatMsg.getUserId());
				if(null != userInfo) {
					chatMsg.setAvatar(userInfo.getAvatar());
					chatMsg.setUserNick(userInfo.getNickName());
					chatMsg.setSelf(null==currentUserId?false:currentUserId.equals(userInfo.getUserId()));
				}
			}
			return msgList;
		}
		return new ArrayList<ChatMsg>(0);
	}
	
	
	
	@Override
	public void releaseOneConnection(HttpServletRequest request, ChatMsg msg)
			throws Exception {
		
	}

	@Override
	public void releaseOneTimeConnection(HttpServletRequest request, ChatMsg msg)
			throws Exception {
		Continuation continuation = sessionPool.getContinuation(AppConfig.buildAndCheck(request),request.getSession().getId());
		if(null != continuation ) {
			timeout(continuation,msg);
		}
	}
	
	private void timeout(Continuation continuation,ChatMsg msg) {
		MessageReplyUtil.sendReplaceMsg(messageReplyer, continuation,
				JSONReturnMsg.createJSONReturnMsg(continuation, false, Arrays.asList(msg)));
	}
	
	@SuppressWarnings("unchecked")
	private void resume(Continuation continuation) {
		if(continuation.isSuspended()) {
			MessageReplyUtil.sendReplaceMsg(messageReplyer, continuation,
					JSONReturnMsg.createJSONReturnMsg(continuation, true, (List<ChatMsg>)continuation.getAttribute(AppConfig.MESSAGEKEY)));
			continuation.resume();
		}
	}

	public void setSessionPool(SessionPool sessionPool) {
		this.sessionPool = sessionPool;
	}

	public void setMessageReplyer(MessageReplyer messageReplyer) {
		this.messageReplyer = messageReplyer;
	}

	public void setMessageDAO(MessageDAO messageDAO) {
		this.messageDAO = messageDAO;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	
}
