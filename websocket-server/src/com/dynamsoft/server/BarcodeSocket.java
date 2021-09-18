package com.dynamsoft.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.dynamsoft.dbr.BarcodeReader;
import com.dynamsoft.dbr.BarcodeReaderException;
import com.dynamsoft.dbr.TextResult;

@WebSocket
public class BarcodeSocket {

	private Session mSession;
	private String mFile;
	private BarcodeReader dbr;
	
	public BarcodeSocket() {
		// TODO Auto-generated constructor stub
		mFile = this + ".png";
		try {
			dbr = new BarcodeReader();
		} catch (BarcodeReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
    	// Delete image cache
        File file = new File(mFile);
        boolean isDeleted = file.delete();
        System.out.println("File " + mFile + " deleted: " + isDeleted);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
    	mSession = session;
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
    }
    
    @OnWebSocketMessage
    public void onMessage(byte[] data, int off, int len) {
    	ByteArrayInputStream in = new ByteArrayInputStream(data);
    	BufferedImage bi;
		try {
			bi = ImageIO.read(in);
			File file = new File(mFile);
			// save client data to local image file
			ImageIO.write(bi, "PNG", file);
			// read barcode
			String r = readBarcode(mFile);
			
			if (r.equals("")) {
				System.out.println("No barcode detected.");
				mSession.getRemote().sendString("No barcode detected. " + new Date());
			}
			else {
				mSession.getRemote().sendString(r);
				System.out.println(r);
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    
    @OnWebSocketMessage
    public void onMessage(String message) {
    	System.out.println("Received message: "+message);
    }

    private String readBarcode(String fileName) {
    	StringBuilder sb = new StringBuilder();
		TextResult[] results = null;
		try {
			results = dbr.decodeFile(fileName, "");
		} catch (BarcodeReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (TextResult result : results) {
			sb.append("Value: ");
			sb.append(result.barcodeText);
		}
		return sb.toString();
    }
}
