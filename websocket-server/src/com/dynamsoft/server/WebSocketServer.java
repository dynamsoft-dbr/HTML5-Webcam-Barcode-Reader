package com.dynamsoft.server;

import java.io.File;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

public class WebSocketServer {
	
    public static void main(String[] args) throws Exception {
        Server server = new Server(88);
        File f = new File("www");
	 	  
		ServletContextHandler context = new ServletContextHandler(
					ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.setBaseResource(Resource.newResource(f.toURI()));
		context.setWelcomeFiles(new String[] { "index.html" });

		ServletHolder holderPwd = new ServletHolder("default",
					DefaultServlet.class);
		holderPwd.setInitParameter("dirAllowed", "true");
		context.addServlet(holderPwd, "/");
		
		
		HandlerCollection handlerList = new HandlerCollection();
		handlerList.setHandlers(new Handler[]{new WSHandler(),context});
		server.setHandler(handlerList);
		
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(8443);

		HttpConfiguration https_config = new HttpConfiguration(http_config);
		https_config.addCustomizer(new SecureRequestCustomizer());

		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath("keystore.jks");
		sslContextFactory.setKeyStorePassword("password1");

		ServerConnector wsConnector = new ServerConnector(server);
		wsConnector.setHost(null);
		wsConnector.setPort(8080);
		server.addConnector(wsConnector);

		ServerConnector wssConnector = new ServerConnector(server,
				new SslConnectionFactory(sslContextFactory,
					HttpVersion.HTTP_1_1.asString()),
				new HttpConnectionFactory(https_config)); // THIS WAS MISSING

		wssConnector.setHost(null);
		wssConnector.setPort(8443);
		server.addConnector(wssConnector);
		
        server.setStopTimeout(0);
        server.start();
        server.join();
    }
}
