package com.doorcii.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.doorcii.manager.ChatManager;
import com.doorcii.utils.SingleChatManger;

/**
 * 发送消息
 * @author Administrator
 */
public class SendServlet extends HttpServlet {
	private static final long serialVersionUID = 5220212875428031949L;

	private ChatManager chatManager = SingleChatManger.getSingleChatManager();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			chatManager.sendMessage(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}
	
}
