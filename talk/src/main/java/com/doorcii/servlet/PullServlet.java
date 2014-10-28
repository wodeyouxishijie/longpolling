package com.doorcii.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.doorcii.beans.JSONReturnMsg;
import com.doorcii.manager.ChatManager;

/**
 * web端数据推送
 * @author Administrator
 */
public class PullServlet extends HttpServlet {
	private static final long serialVersionUID = -2476668785877361092L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JSONReturnMsg jr = new JSONReturnMsg();
		WebApplicationContext ctx = WebApplicationContextUtils  
                .getWebApplicationContext(this.getServletContext());  
		try {
			String errorMsg = ((ChatManager)ctx.getBean("chatManager")).handleRequest(req, resp);
			if(StringUtils.isNotBlank(errorMsg)) {
				jr.setSuccess(false);
				jr.setMessage(errorMsg);
				resp.getWriter().write(JSONObject.toJSONString(jr));
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			jr.setSuccess(false);
			jr.setMessage(e.getMessage());
			resp.getWriter().write(JSONObject.toJSONString(jr));
			return;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
