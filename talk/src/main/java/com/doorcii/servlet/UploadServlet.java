package com.doorcii.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.doorcii.beans.AppConfig;
import com.doorcii.beans.JSONReturnMsg;
import com.doorcii.beans.UserInfo;
import com.doorcii.manager.ChatManager;

/**
 * 图片上传
 * @author Administrator
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = -732455622102462563L;
	private static final Logger logger = Logger.getLogger(UploadServlet.class);
	
	private static final String UPLOAD_DIRECTORY="upload";
	private static final String TEMP_DIR = "temp";
	private static final int MEMORY_THRESHOLD = 1024*1024*3;
	private static final int MAX_FILE_SIZE = 1024*1024*5;
	private static final int MAX_REQUEST_SIZE = 1024*1024*50;
	private static final String[] extList = new String[]{"jpg","jpeg","png","bmp","gif"};
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json");
		UserInfo userInfo = (UserInfo)request.getSession().getAttribute(AppConfig.USER_KEY);
		if(null == userInfo) {
			JSONReturnMsg jr = new JSONReturnMsg();
			jr.setSuccess(false);
			jr.setMessage("NOT_LOGIN");
			response.getWriter().write(JSONObject.toJSONString(jr));
			return;
		}
		if(!ServletFileUpload.isMultipartContent(request)){
			JSONReturnMsg jr = new JSONReturnMsg();
			jr.setSuccess(false);
			jr.setMessage("Mutipart content need.");
			response.getWriter().write(JSONObject.toJSONString(jr));
			return;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		factory.setRepository(new File(request.getServletContext().getRealPath("")+File.separator+TEMP_DIR));
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(MAX_REQUEST_SIZE);
		upload.setFileSizeMax(MAX_FILE_SIZE);
		
		String uploadPath = getServletContext().getRealPath("")+File.separator +UPLOAD_DIRECTORY;
		File uploadDir = new File(uploadPath);
		if(!uploadDir.exists()){
			uploadDir.mkdir();
		}
		try{
			List<FileItem> formItems = upload.parseRequest(request);
			if(formItems != null && formItems.size() > 0) {
				List<String> fileList = new ArrayList<String>();
				for(FileItem item : formItems) {
					if(!item.isFormField()) {
						String ext = FilenameUtils.getExtension(item.getName());
						if(!contains(ext)) {
							return;
						}
						String filePath = uploadPath + File.separator + UUID.randomUUID().toString()+"."+ext;
						File storeFile = new File(filePath);
						item.write(storeFile);
						fileList.add(UPLOAD_DIRECTORY+ File.separator+storeFile.getName());
					}
				}
				
				//!TODO 做成异步推送消息
				WebApplicationContext ctx = WebApplicationContextUtils  
		                .getWebApplicationContext(this.getServletContext());  
				for(String name: fileList) 
					((ChatManager)ctx.getBean("chatManager")).sendImgMessage(formItems,request, response,name);
			}
			response.setContentType("text/html; charset=utf-8");
			response.setHeader("Cache-Control", "no-cache"); 
			JSONReturnMsg jr = new JSONReturnMsg();
			jr.setSuccess(true);
			response.getWriter().write(JSONObject.toJSONString(jr));
			return;
		}catch (Exception e){
			logger.error("upload failed!",e);
		}
	}
	
	private boolean contains(String ext) {
		for(String e: extList) {
			if(e.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}
	
}
