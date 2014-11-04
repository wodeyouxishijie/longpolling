package com.doorcii.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

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
	
	/**
	 * 发送图片消息
	 * @param request
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public String sendImgMessage(List<FileItem> formItems,HttpServletRequest request,HttpServletResponse response,String filePath) throws Exception;
	
}
