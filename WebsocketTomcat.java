import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.ByteBuffer;
@ServerEndpoint(value="/websocket")
public class WebsocketTomcat {
    final static String url="D:/apache-tomcat-7.0.91/webapps/firstTest/WEB-INF/classes";
    volatile boolean runningFlag;
    private Thread streamingThread;
    public Robot robot;
    static{
        System.load(url+"/include/msvcr100.dll");
        System.load(url+"/include/avutil-55.dll");
        System.load(url+"/include/swresample-2.dll");
        System.load(url+"/include/avcodec-57.dll");
        System.load(url+"/include/avformat-57.dll");
        System.load(url+"/include/d3d9.dll");
        System.load(url+"/include/pthreadVC2.dll");
        System.load(url+"/include/swscale-4.dll");
        System.load(url+"/include/ucrtbased.dll");
        System.load(url+"/include/vcruntime140d.dll");
        System.load(url+"/include/msvcp140d.dll");
        System.load(url+"/include/dxScreenShot.dll");
}
    Session session;
    public  native void streamingStart();

    public int webSocketTransmit(int size,byte[] data){
        ByteBuffer buffer=ByteBuffer.wrap(data,0,size);
        try {
            session.getBasicRemote().sendBinary(buffer);
        }catch(Exception e){
            //System.out.println("can not send the binary data");
        }
        return 1;
    }
    @OnMessage
    public void incoming(String message,Session session){
//        if(message.startsWith("k")) {
//            int keyCode=Integer.parseInt(message.substring(1));
//            robot.keyPress(keyCode);
//            robot.keyRelease(keyCode);
//        }
         if(message.startsWith("ml")){
            String XY[]=message.substring(2).split(" ");
            int location_X=(int)Double.parseDouble(XY[0]);
            int location_Y=(int)Double.parseDouble(XY[1]);
            robot.mouseMove(location_X,location_Y);
            robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
        }
        else if(message.startsWith("mr")){
             String XY[]=message.substring(2).split(" ");
             int location_X=(int)Double.parseDouble(XY[0]);
             int location_Y=(int)Double.parseDouble(XY[1]);
             robot.mouseMove(location_X,location_Y);
             robot.mousePress(KeyEvent.BUTTON3_DOWN_MASK);
             robot.mouseRelease(KeyEvent.BUTTON3_DOWN_MASK);
         }
    }

    @OnOpen
    public void start(Session session){
        //connections.add(session);
        System.out.println("Websocket connected");
        this.session=session;
        streamingThread=new Thread(new Runnable(){
            @Override
            public void run() {
                runningFlag=true;
                streamingStart();
            }
        },"Thread-streaming");
        streamingThread.start();
        try {
           robot = new Robot();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @OnClose
    public void end(){
        if(streamingThread!=null){
            runningFlag=false;
            System.out.println("The connection is closed");
        }
    }


    @OnError
    public void onError(Session session, Throwable error){
               System.out.println("Something went wrong");
                error.printStackTrace();
             }
}


