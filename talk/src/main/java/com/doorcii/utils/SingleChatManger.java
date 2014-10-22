package com.doorcii.utils;

import com.doorcii.manager.ChatManager;
import com.doorcii.manager.ChatManagerImpl;

public class SingleChatManger {
	
	public static final ChatManager chatManager = new ChatManagerImpl();
	
	public static ChatManager getSingleChatManager() {
		return chatManager;
	}
	
}
