package com.doorcii.manager;

import java.util.List;

import com.doorcii.beans.Message;

public interface MessageAdmin {
	
	public boolean persist(Message message) throws Exception;
	
	public Message getById(String messageId) throws Exception;
	
	public List<Message> batchGetMsg(String userId,int pageNo,int pageSize) throws Exception;
	
}
