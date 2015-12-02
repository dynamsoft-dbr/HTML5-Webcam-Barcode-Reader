package com.dynamsoft.server;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebSocket
public class WSHandler extends WebSocketHandler {
	
	@Override
	public void configure(WebSocketServletFactory factory) {
		// TODO Auto-generated method stub
		factory.setCreator(new BarcodeCreator());
		
		// configuration
		factory.getPolicy().setMaxBinaryMessageBufferSize(1024 * 1024);
		factory.getPolicy().setMaxTextMessageBufferSize(1024 * 1024);
		factory.getPolicy().setMaxTextMessageSize(1024 * 1024);
		factory.getPolicy().setMaxBinaryMessageSize(1024 * 1024);
	}
}