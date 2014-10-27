package com.doorcii.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.doorcii.beans.AppConfig;
import com.doorcii.beans.JSONReturnMsg;
import com.doorcii.beans.UserInfo;
import com.doorcii.ibatis.UserDAO;
import com.doorcii.manager.CacheManager;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 4452738004780618441L;
	
	public static final String USER_NAME = "_user_name";
	
	public static final String PASSWORD = "_password";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = ServletRequestUtils.getStringParameter(request, USER_NAME);
		String password = ServletRequestUtils.getStringParameter(request, PASSWORD);
		JSONReturnMsg jr = new JSONReturnMsg();
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			jr.setSuccess(false);
			jr.setMessage("PARAM_ERROR");
			resp.getWriter().write(JSONObject.toJSONString(jr));
			return;
		}
		WebApplicationContext ctx = WebApplicationContextUtils  
                .getWebApplicationContext(this.getServletContext());  
		UserDAO userDAO = (UserDAO)ctx.getBean("userDAO");
		String md5Pwd = password;//DigestUtils.md5Hex(password);
		try {
			UserInfo userInfo = userDAO.getUserById(username, md5Pwd);
			if(null != userInfo) {
				CacheManager cacheManger = (CacheManager)ctx.getBean("cacheManager");
				cacheManger.setUser(userInfo);
				request.getSession().setAttribute(AppConfig.USER_KEY, userInfo);
				resp.sendRedirect("/cometd.html");
				return;
			} else {
				jr.setSuccess(false);
				jr.setMessage("用户名或者密码错误！");
				resp.getWriter().write(JSONObject.toJSONString(jr));
			}
		} catch (Exception e) {
			jr.setSuccess(false);
			jr.setMessage(e.getMessage());
			resp.getWriter().write(JSONObject.toJSONString(jr));
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	
}
