package com.doorcii.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在线聊天相关业务
 * @author Administrator
 */
public interface ChatManager {
	
	public String handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception;
	
	/**
	 * 消息发送
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String sendMessage(HttpServletRequest request,HttpServletResponse response) throws Exception;
	
}
