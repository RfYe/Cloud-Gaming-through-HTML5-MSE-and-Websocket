# Cloud-Gaming-through-HTML5-MSE-and-Websocket
Enable cloud gaming on mainstream browsers using HTML5 MSE and Websocket. This combination is plugin free, which ensure the cross platform(mobile browser&amp;desktop browser) compatibility

BRIEF INTRODUCTION
=====
This is a project intented for proving cloud gaming based on web browser,which could tansforming the future gaming industry, is feasible. The overall performance of the system, varying from different network enviroment and machines, is around 30fps and a delay around 600ms without any optimization.

The whole system can be separated into three parts:

1.Video capturing(raw ARGB32) using DirectX, encoding(YUV420 to H.264 stream) and packaging(H.264 stream to fMp4) with FFMPEG.

2.Backend:webSocket (with Tomcat 7.0.90 ) for transmitting packaged fMp4 data to client browser and also listen to the control signals from client browser. 

3.Frontend web page:enable websocket for receiving media data from Tomcat server, feeding the received media data to the media source extension for demuxing and decoding, meanwhile listening to the user input(keyboard&mouse) signal and sent ot back to the backend.

FILE DESCRIPTION
=====
The 

