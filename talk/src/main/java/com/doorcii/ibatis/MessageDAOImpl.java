package com.doorcii.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.doorcii.beans.Message;
import com.doorcii.sequence.GroupSequence;

public class MessageDAOImpl extends SqlMapClientDaoSupport implements MessageDAO {

	private GroupSequence messageSeq;
	
	@Override
	public int saveMessage(Message message) throws Exception {
		long id = messageSeq.nextValue();
		message.setMessageId(id);
		this.getSqlMapClientTemplate().insert("talk.insertMessage",message);
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getMessageRange(long currentId,long appId,int typeId) throws Exception {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("currentId", currentId);
		param.put("appId", appId);
		param.put("typeId", typeId);
		return this.getSqlMapClientTemplate().queryForList("talk.queryMessage", param);
	}

	@Override
	public Message getMessageById(long messageId) throws Exception {
		return null;
	}

	public void setMessageSeq(GroupSequence messageSeq) {
		this.messageSeq = messageSeq;
	}
	
}
