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

import com.dynamsoft.barcode.EnumBarCode;
import com.dynamsoft.barcode.JBarcode;
import com.dynamsoft.barcode.tagBarcodeResult;
import com.dynamsoft.barcode.tagBarcodeResultArray;

@WebSocket
public class BarcodeSocket {

	private Session mSession;
	private String mFile;
	
	public BarcodeSocket() {
		// TODO Auto-generated constructor stub
		mFile = this + ".png";
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
        if (message.contains("data:image/png;base64")) {
        	String base64Image = message.split(",")[1];
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            BufferedImage bi;
    		try {
    			bi = ImageIO.read(new ByteArrayInputStream(imageBytes));
    			File file = new File(mFile);
    			ImageIO.write(bi, "PNG", file);
    			
    			String r = readBarcode(mFile);
    			
    			if (r.equals("")) {
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
    }

    private String readBarcode(String fileName) {
    	// Set DBR license
		JBarcode.DBR_InitLicense("38B9B94D8B0E2B41DB1CC80A58946567");
		// Result array
		tagBarcodeResultArray paryResults = new tagBarcodeResultArray();
		// Read barcode
		int iret = JBarcode.DBR_DecodeFile(fileName, 100,
				EnumBarCode.OneD | EnumBarCode.QR_CODE
				| EnumBarCode.PDF417 | EnumBarCode.DATAMATRIX, paryResults);
		
		System.out.println("DBR_DecodeFile return value: " + iret);
		String r = "";
		for (int iIndex = 0; iIndex < paryResults.iBarcodeCount; iIndex++) {
			tagBarcodeResult result = paryResults.ppBarcodes[iIndex];
			int barcodeDataLen = result.iBarcodeDataLength;

			byte[] pszTemp1 = new byte[barcodeDataLen];
			for (int x = 0; x < barcodeDataLen; x++) {
				pszTemp1[x] = result.pBarcodeData[x];
			}

			r += "Value: " + new String(pszTemp1);
		}
		
		return r;
    }
}
