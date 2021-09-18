/**
 *  HTML5 Webcam Barcode Reader with Dynamsoft Barcode Reader SDK.
 *  Support PC & Mobile.
 */
var videoElement = document.querySelector('video');
var canvas = document.getElementById('canvas');
var ctx = canvas.getContext('2d');
var videoSelect = document.querySelector('select#videoSource');
var videoOption = document.getElementById('videoOption');
var buttonGo = document.getElementById('go');
var barcode_result = document.getElementById('dbr');
var isConnected = false;
var isPaused = false;
var intervalId = 0;
var videoWidth = 640, videoHeight = 480;

// create websocket
var ws = new WebSocket("wss://"+location.host);

ws.onopen = function() {
  isConnected = true;
  ws.send("Hi");
};

ws.onmessage = function (evt) {
  if (isPaused) {
    return;
  }

  var result = evt.data;
  barcode_result.textContent = result;
  // display barcode result
  if (result.indexOf("No barcode") == -1) {
    isPaused = true;
    window.clearInterval(intervalId);
    console.log("Get result, clean id: " + intervalId);
    buttonGo.disabled = false;
    canvas.style.display  = 'block';
  }
};

ws.onclose = function() {
  isConnected = false;
  console.log("Closed");
};

ws.onerror = function(err) {
    console.log("Error: " + err);
};

// initialize camera infos for Chrome
function initCameraSource(sourceInfos) {
  for (var i = 0; i < sourceInfos.length; i++) {
    var sourceInfo = sourceInfos[i];
    var option = document.createElement('option');
    option.value = sourceInfo.id;
    if (sourceInfo.kind === 'video') {
      option.text = sourceInfo.label || "Camera " + (videoSelect.length + 1);
      videoSelect.appendChild(option);
    } else {
      console.log("Source info: " + sourceInfo);
    }
  }
  toggleCamera();
}

// stackoverflow: http://stackoverflow.com/questions/4998908/convert-data-uri-to-file-then-append-to-formdata/5100158
function dataURItoBlob(dataURI) {
    // convert base64/URLEncoded data component to raw binary data held in a string
    var byteString;
    if (dataURI.split(',')[0].indexOf('base64') >= 0)
        byteString = atob(dataURI.split(',')[1]);
    else
        byteString = unescape(dataURI.split(',')[1]);

    // separate out the mime component
    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

    // write the bytes of the string to a typed array
    var ia = new Uint8Array(byteString.length);
    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    return new Blob([ia], {type:mimeString});
}

function successCallback(stream) {
  window.stream = stream;
  try {
    videoElement.srcObject = stream;
  } 
  catch (error) {
    videoElement.src = window.URL.createObjectURL(stream);
  }
  videoElement.play();
}

function errorCallback(error) {
  console.log("Error: " + error);
}

function startCamera(constraints) {
  if (navigator.getUserMedia) {
     navigator.getUserMedia(constraints, successCallback, errorCallback);
  }
  else {
     console.log("getUserMedia not supported");
  }
}

function toggleCamera() {
  var videoSource = videoSelect.value;
  var constraints = {
    video: {
      optional: [{
        sourceId: videoSource
      }]
    }
  };

  startCamera(constraints);
}

// add button event
buttonGo.onclick = function() {
  window.clearInterval(intervalId);
  console.log("clean id: " + intervalId);

  canvas.style.display = 'none';

  isPaused = false;
  scanBarcode();
  buttonGo.disabled = true;
};

// query supported Web browsers
if (navigator.getUserMedia || navigator.mozGetUserMedia) {
  navigator.getUserMedia = navigator.getUserMedia || navigator.mozGetUserMedia;
  startCamera({
    video: {
      // mandatory: {
      //   maxWidth: 640,
      //   maxHeight: 480
      // }
    }
  });
}
else if (navigator.webkitGetUserMedia) {
  videoOption.style.display  = 'block';
  navigator.getUserMedia = navigator.webkitGetUserMedia;
  MediaStreamTrack.getSources(initCameraSource);
  videoSelect.onchange = toggleCamera;
}

// scan barcode
function scanBarcode() {
  intervalId = window.setInterval(function() {
    if (!isConnected || isPaused) {
        return;
    }

    var data = null, newblob = null;

    canvas.width = videoElement.videoWidth;
    canvas.height = videoElement.videoHeight;
    ctx.drawImage(videoElement, 0, 0, videoElement.videoWidth, videoElement.videoHeight);
    // convert canvas to base64
    data = canvas.toDataURL('image/png', 1.0);
    // convert base64 to binary
    newblob = dataURItoBlob(data);
    ws.send(newblob);

  }, 200);
  console.log("create id: " + intervalId);
}
