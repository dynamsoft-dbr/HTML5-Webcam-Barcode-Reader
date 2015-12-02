package com.dynamsoft.server;

import org.eclipse.jetty.server.Server;

public class WebSocketServer {
	
    public static void main(String[] args) throws Exception {
        Server server = new Server(88);
        server.setHandler(new WSHandler());
        server.setStopTimeout(0);
        server.start();
        server.join();
    }
}
