package com.doorcii.beans;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.doorcii.utils.AppIdCenter;

public class AppConfig {
	
	public static final String APPCONFIG = "appConfig";
	
	public static final String MESSAGEKEY = "messageKey";
	
	public static final String MAX_KEY = "MXID";
	
	public static final String VERSION_ID = "vId";
	
	public static final String USER_ID = "uId";
	
	public static final String MESSAGE_MAX_ID = "msgId";
	
	public static final String USER_KEY = "_user_";
	
	private AppIdCenter appId;
	
	private int typeId;
	
	private String uniqueId;

	public AppIdCenter getAppId() {
		return appId;
	}

	public void setAppId(AppIdCenter appId) {
		this.appId = appId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public static AppConfig buildAndCheck(final HttpServletRequest request) {
		String appId = request.getParameter("_appId");
		String typeId = request.getParameter("_typeId");
		String id = request.getParameter("_unqId");
		if(StringUtils.isBlank(appId) || !NumberUtils.isNumber(appId) || StringUtils.isBlank(typeId)
			|| !NumberUtils.isNumber(typeId) || StringUtils.isBlank(id)) {
			return null;
		}
		AppConfig appConfig = new AppConfig();
		AppIdCenter appConf = AppIdCenter.getByAppId(Long.valueOf(appId));
		if(null != appConf) {
			appConfig.setAppId(appConf);
			appConfig.setTypeId(Integer.valueOf(typeId));
			appConfig.setUniqueId(id);
			return appConfig;
		}
		return null;
	}
	
	public static AppConfig buildAndCheckMutiForm(List<FileItem> formItems) {
		AppConfig appConf = new AppConfig();
		try {
			for(FileItem item : formItems) {
				if(item.isFormField()) {
					String name = item.getFieldName();
					if(name.equals("_appId")) {
						appConf.setAppId(AppIdCenter.getByAppId(Long.valueOf(item.getString("utf-8"))));
					}
					if(name.equals("_typeId")) {
						appConf.setTypeId(Integer.valueOf(item.getString("utf-8")));
					}
					if(name.equals("_unqId")) {
						appConf.setUniqueId(item.getString("utf-8"));
					}
				}
			}
			if(null != appConf.getAppId() && appConf.getTypeId() > 0 
					&& StringUtils.isNotBlank(appConf.getUniqueId())) {
					return appConf;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
}
