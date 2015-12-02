package com.dynamsoft.server;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class BarcodeCreator implements WebSocketCreator {

	@Override
	public Object createWebSocket(ServletUpgradeRequest arg0, ServletUpgradeResponse arg1) {
		// TODO Auto-generated method stub
		return new BarcodeSocket();
	}

}
