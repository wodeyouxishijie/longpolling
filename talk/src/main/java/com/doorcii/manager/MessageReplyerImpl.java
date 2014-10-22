package com.doorcii.manager;

import org.eclipse.jetty.continuation.Continuation;

import com.alibaba.fastjson.JSONObject;

public class MessageReplyerImpl implements MessageReplyer {

	@Override
	public void writeMessage(Continuation con, Object msg) throws Exception {
		con.getServletResponse().getWriter().write(JSONObject.toJSONString(msg));
		con.getServletResponse().getWriter().flush();
	}

}
