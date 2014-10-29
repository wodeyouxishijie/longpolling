package com.doorcii.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

	public static final String REMEMBER = "_remember_me";
	
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
				writeCookie(ServletRequestUtils.getStringParameter(request,REMEMBER)!=null ,resp,username,password);
				resp.sendRedirect("/cometd.html");
				return;
			} else {
				jr.setSuccess(false);
				jr.setMessage("用户名或者密码错误！");
				resp.getWriter().write(JSONObject.toJSONString(jr));
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
	private void writeCookie(boolean write,HttpServletResponse resp,String username,String password) {
		int expire = write?30*24*60*60:-1;
		Cookie remeberCookie = new Cookie("_rm_",write?"1":null);
		remeberCookie.setMaxAge(expire);
		resp.addCookie(remeberCookie);
		Cookie userCookie = new Cookie("_un_",write?username:null);
		resp.addCookie(userCookie);
		userCookie.setMaxAge(expire);
		Cookie pwdCookie = new Cookie("_pw_",write?password:null);
		resp.addCookie(pwdCookie);
		pwdCookie.setMaxAge(expire);
	}
	
}
