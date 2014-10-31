package com.doorcii.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

public class FaceUtil {
	
	public static final String REG = "\\[\\w{1}\\-\\w+\\]";
	
	public static final String IMG_URL = "images/";
	
	/**
	 * 蛋黄表情
	 */
	@SuppressWarnings("serial")
	public static final Map<String,String> A_FACE_MAP = new HashMap<String,String>(){
		{
			this.put("[a-bb]", "mr/0.gif");
			this.put("[a-gl]", "mr/1.gif");
			this.put("[a-hs]", "mr/2.gif");
			this.put("[a-gx]", "mr/3.gif");
			this.put("[a-ht]", "mr/4.gif");
			this.put("[a-zb]", "mr/5.gif");
			this.put("[a-fn]", "mr/6.gif");
			this.put("[a-bn", "mr/7.gif");
			this.put("[a-ax]", "mr/8.gif");
			this.put("[a-kl]", "mr/9.gif");
			this.put("[a-hq]", "mr/10.gif");
			this.put("[a-dx]", "mr/11.gif");
			this.put("[a-gy]", "mr/12.gif");
			this.put("[a-hf]", "mr/13.gif");
			this.put("[a-lh]", "mr/14.gif");
			this.put("[a-j]", "mr/15.gif");
			this.put("[a-kbk]", "mr/16.gif");
			this.put("[a-mm]", "mr/17.gif");
			this.put("[a-ka]", "mr/18.gif");
			this.put("[a-m]", "mr/19.gif");
			this.put("[a-lz]", "mr/20.gif");
			this.put("[b-tx]", "bzmh/0.gif");
			this.put("[b-fn]", "bzmh/1.gif");
			this.put("[b-cn]", "bzmh/2.gif");
			this.put("[b-jj]", "bzmh/3.gif");
			this.put("[b-zh]", "bzmh/4.gif");
			this.put("[b-ch]", "bzmh/5.gif");
			this.put("[b-bs]", "bzmh/6.gif");
			this.put("[b-sx]", "bzmh/7.gif");
			this.put("[b-zd]", "bzmh/8.gif");
			this.put("[b-dj]", "bzmh/9.gif");
			this.put("[b-kq]", "bzmh/10.gif");
			this.put("[b-sk]", "bzmh/11.gif");
			this.put("[b-kx]", "bzmh/12.gif");
			this.put("[b-ts]", "bzmh/13.gif");
			this.put("[b-gl]", "bzmh/14.gif");
			this.put("[b-lg]", "bzmh/15.gif");
			this.put("[b-skz]","bzmh/16.gif");
			this.put("[b-pz]", "bzmh/17.gif");
			this.put("[b-gd]", "bzmh/18.gif");
			this.put("[b-nb]", "bzmh/19.gif");
			this.put("[b-pz]", "bzmh/20.gif");
			this.put("[b-ts]", "bzmh/21.gif");
		}
	};
	
	/**
	 * \\w{1}\\-\\w+
	 * @param message
	 * @return
	 */
	public static final String convertFaceImg(String message) {
		Pattern pattern = Pattern.compile(REG);
		Matcher matcher = pattern.matcher(message);
		Set<String> faceList = new HashSet<String>();
		while(matcher.find()) {
			faceList.add(matcher.group());
		}
		if(!CollectionUtils.isEmpty(faceList)) {
			for(String face : faceList) {
				String img = A_FACE_MAP.get(face);
				if(StringUtils.isNotBlank(img)) {
					message = message.replace(face, "<img src='"+IMG_URL+img+"' width='22px;'/>");
				}
			}
		}
		return message;
	}
	
	public static void main(String[] args) {
		System.out.println(FaceUtil.convertFaceImg("[a-fn]发生大幅[a-fn]"));
		System.out.println("发生大幅[a-fn]".replaceAll("\\[a-fn\\]", "123"));
	}
	
}
