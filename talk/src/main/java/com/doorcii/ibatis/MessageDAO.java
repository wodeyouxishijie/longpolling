package com.doorcii.ibatis;

import java.util.List;

import com.doorcii.beans.Message;

public interface MessageDAO {
	
	public int saveMessage(Message message) throws Exception;
	
	public List<Message> getMessageRange(long currentId,long appId,int typeId) throws Exception;
	
	public Message getMessageById(long messageId) throws Exception;
}
