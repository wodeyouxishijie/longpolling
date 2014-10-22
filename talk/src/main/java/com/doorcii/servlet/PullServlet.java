package com.doorcii.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.doorcii.manager.ChatManager;
import com.doorcii.utils.SingleChatManger;

/**
 * web端数据推送
 * @author Administrator
 */
public class PullServlet extends HttpServlet {
	private static final long serialVersionUID = -2476668785877361092L;
	
	private ChatManager chatManager = SingleChatManger.getSingleChatManager();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try {
			String errorMsg = chatManager.handleRequest(req, resp);
			if(StringUtils.isNotBlank(errorMsg)) {
				resp.getWriter().write(errorMsg);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
