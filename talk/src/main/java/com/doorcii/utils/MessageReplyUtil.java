package com.doorcii.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.jetty.continuation.Continuation;

import com.doorcii.beans.AppConfig;
import com.doorcii.manager.MessageReplyer;

public class MessageReplyUtil {
	
	/**
	 * 发送session被替换的结果消息
	 * @param replyer
	 * @param con
	 */
	public static void sendReplaceMsg(MessageReplyer replyer,Continuation con,Object msg) {
		try {
			replyer.writeMessage(con,msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String genKey(AppConfig appConf,String sessionId) {
		return appConf.getAppId().getAppId() +"_"+appConf.getTypeId()+"_"+ sessionId+"_"+appConf.getUniqueId();
	}
	
	public static String genKey(HttpServletRequest request) {
		String appId = request.getParameter("_appId");
		String typeId = request.getParameter("_typeId");
		String id = request.getParameter("_unqId");
		if(StringUtils.isBlank(appId) || !NumberUtils.isNumber(appId) || StringUtils.isBlank(typeId)
			|| !NumberUtils.isNumber(typeId) || StringUtils.isBlank(id)) {
			return null;
		}
		AppIdCenter appConf = AppIdCenter.getByAppId(Long.valueOf(appId));
		if(null != appConf) {
			return appId.trim() +"_"+typeId.trim()+"_"+ request.getSession().getId() +"_"+id.trim();
		}
		return null;
	}
	
}
