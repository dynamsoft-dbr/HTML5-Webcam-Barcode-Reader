# HTML5 Webcam Barcode Reader
The sample demonstrates how to implement a HTML5 Webcam Barcode Reader for both mobile and PC Web browsers with [Dynamsoft Barcode Reader SDK][1] and Java [WebSocket][2]. 

Prerequisites
-------------
* [Dynamsoft Barcode SDK 4.0][3]
* [Jetty][4]

Get Started
-----------
1. Deploy the client to a Web server: Apache, IIS, Nginx or others. E.g. **http://192.168.8.84:8000**.
2. Import the websocket-server project to eclipse.
3. Copy **DynamsoftBarcodeJNIx64.dll** and **DynamsoftBarcodeReaderx64.dll** from **%Dynamsoft Barcode Reader%\Samples\Java\BarcodeReaderDemo\src** to the project root directory.
4. Import Jetty WebSocket libraries.
5. Build and run the WebSocket server.
6. Open **video.js** and change the WebSocket IP address and port. E.g.

    ```JavaScript
    var ws = new WebSocket("ws://192.168.8.84:88");
    ```
7. Visit **http://192.168.8.84:8000** in Chrome.
8. Select a Webcam source. On mobile devices, you can toggle between front-facing camera and back-facing camera.
9. Click button to detect barcode.

Mobile Screenshots
--------------------
![select camera](http://www.codepool.biz/wp-content/uploads/2015/12/mobile_camera-576x1024.png)
![mobile barcode reader](http://www.codepool.biz/wp-content/uploads/2015/12/mobile_barcode_result.png)

Desktop Screenshots
--------------------
![html5 webcam](http://www.codepool.biz/wp-content/uploads/2015/12/desktop_barcode_result-1024x684.png)

[1]:http://www.dynamsoft.com/Products/Dynamic-Barcode-Reader.aspx
[2]:https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API
[3]:http://www.dynamsoft.com/Downloads/Dynamic-Barcode-Reader-Download.aspx
[4]:http://www.eclipse.org/jetty/
