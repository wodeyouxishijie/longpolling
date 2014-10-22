package com.doorcii.test;

import java.util.HashMap;
import java.util.Map;

import org.cometd.client.BayeuxClient;
import org.eclipse.jetty.client.HttpClient;

public class PublishTest {

	public static void main(String[] args) throws Exception  {
		HttpClient httpClient = new HttpClient();
		httpClient.start();
		BayeuxClient client = new BayeuxClient(httpClient, "http://localhost:8080/talk/cometd");
		client.start();
		int i=20;
		while(i-- > 0) { 
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("key","测试消息推送"+i);
			System.out.println(data.get("key"));
			client.publish("/chat/message", data, null);
			Thread.sleep(1000L);
		}
		Thread.sleep(3000L);
	}
}
