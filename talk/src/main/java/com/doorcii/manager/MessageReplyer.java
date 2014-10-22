package com.doorcii.manager;

import org.eclipse.jetty.continuation.Continuation;

public interface MessageReplyer {
	
	public void writeMessage(Continuation con,Object msg) throws Exception;
	
}
