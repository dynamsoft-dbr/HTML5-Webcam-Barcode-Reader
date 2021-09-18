# HTML5 Webcam Barcode Reader
The sample demonstrates how to implement a HTML5 Webcam Barcode Reader for both mobile and PC Web browsers with [Dynamsoft Barcode Reader SDK][1] and Java [WebSocket][2]. 

Prerequisites
-------------
* [Dynamsoft Barcode Reader SDK][3]
* [Jetty][4]

Get Started
-----------
1. Import the websocket-server project to eclipse.
2. Download the [java library](https://www.dynamsoft.com/barcode-reader/downloads/) of Dynamsoft Barcode Reader and add it to the build path.
3. Build and run the WebSocket server.
4. Visit <https://127.0.0.1:8443>.
5. Select a Webcam source. On mobile devices, you can toggle between front-facing camera and back-facing camera.
6. Click button to detect barcode.

Mobile Screenshots
--------------------
![select camera](http://www.codepool.biz/wp-content/uploads/2015/12/mobile_camera-576x1024.png)

Desktop Screenshots
--------------------
![html5 webcam](http://www.codepool.biz/wp-content/uploads/2015/12/desktop_barcode_result-1024x684.png)

Blog
-------
[How to Make HTML5 Barcode Reader with Desktop and Mobile Cameras][5]

[1]:http://www.dynamsoft.com/Products/Dynamic-Barcode-Reader.aspx
[2]:https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API
[3]:http://www.dynamsoft.com/Downloads/Dynamic-Barcode-Reader-Download.aspx
[4]:http://www.eclipse.org/jetty/
[5]:http://www.codepool.biz/html5-barcode-reader-desktop-mobile-camera.html
