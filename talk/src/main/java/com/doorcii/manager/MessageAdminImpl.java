package com.doorcii.manager;

import java.util.List;

import com.doorcii.beans.Message;

public class MessageAdminImpl implements MessageAdmin {

	@Override
	public boolean persist(Message message) throws Exception {
		return false;
	}

	@Override
	public Message getById(String messageId) throws Exception {
		return null;
	}

	@Override
	public List<Message> batchGetMsg(String userId, int pageNo, int pageSize)
			throws Exception {
		return null;
	}

}
