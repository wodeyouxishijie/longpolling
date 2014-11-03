package com.doorcii.servlet;

import java.io.File;
import java.io.IOException;
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

import com.alibaba.fastjson.JSONObject;
import com.doorcii.beans.JSONReturnMsg;

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
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
				for(FileItem item : formItems) {
					if(!item.isFormField()) {
						String ext = FilenameUtils.getExtension(item.getName());
						String filePath = uploadPath + File.separator + UUID.randomUUID().toString()+ext;
						File storeFile = new File(filePath);
						item.write(storeFile);
						// 异步推送消息
					}
				}
			}
			JSONReturnMsg jr = new JSONReturnMsg();
			jr.setSuccess(true);
			response.getWriter().write(JSONObject.toJSONString(jr));
			return;
		}catch (Exception e){
			logger.error("upload failed!",e);
		}
	}
}
