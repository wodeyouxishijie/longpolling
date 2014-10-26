package com.doorcii.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.doorcii.beans.AppConfig;
import com.doorcii.beans.JSONReturnMsg;
import com.doorcii.manager.ChatManager;

/**
 * 发送消息
 * @author Administrator
 */
public class SendServlet extends HttpServlet {
	private static final long serialVersionUID = 5220212875428031949L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JSONReturnMsg jr = new JSONReturnMsg();
		WebApplicationContext ctx = WebApplicationContextUtils  
                .getWebApplicationContext(this.getServletContext());  
		if(null == req.getSession().getAttribute(AppConfig.USER_KEY)) {
			jr.setSuccess(false);
			jr.setMessage("NOT_LOGIN");
			resp.getWriter().write(JSONObject.toJSONString(jr));
			return;
		}
		try {
			((ChatManager)ctx.getBean("chatManager")).sendMessage(req, resp);
			return;
		} catch (Exception e) {
			jr.setSuccess(false);
			jr.setMessage(e.getMessage());
			resp.getWriter().write(JSONObject.toJSONString(jr));
			return;
		}
	}
	
}
