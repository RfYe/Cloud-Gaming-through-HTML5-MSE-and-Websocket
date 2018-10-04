import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

@ServerEndpoint(value="/websocket")
public class WebsocketTomcat {
    final static String url="C:/apache-tomcat-7.0.91/webapps/firstTest/WEB-INF/classes";
    private volatile static boolean inuse=false;
    private boolean selected=false;
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
    String gameName="C://apache-tomcat-7.0.91//webapps//firstTest//winmine.exe";
    Timer timer;
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

    public void openGame(){
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec("cmd.exe /C start "+gameName); } catch (IOException e) {
            System.out.println("somgthing went wrong when opening the game");
        }
    }
    public void closeGame(){
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec("cmd.exe /C start wmic process where name='winmine.exe' call terminate"); } catch (IOException e) {
            System.out.println("somgthing went wrong when closing the game");
        }
    }
    TimerTask task=new TimerTask() {
        @Override
        public void run() {
            try {
                session.close();
            }catch(IOException e){System.out.println("OnTimeout:The websocket has already been closed");}
        }
    };
    @OnMessage
    public void incoming(String message,Session session){
        //enable keyboard
//        if(message.startsWith("k")) {
//            int keyCode=Integer.parseInt(message.substring(1));
//            robot.keyPress(keyCode);
//            robot.keyRelease(keyCode);
//        }
        //enable mouse left key
         if(message.startsWith("ml")){
            String XY[]=message.substring(2).split(" ");
            int location_X=(int)Double.parseDouble(XY[0]);
            int location_Y=(int)Double.parseDouble(XY[1]);
            if(location_Y>=1000)
                return;
            robot.mouseMove(location_X,location_Y);
            robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
        }
         //enable mouse right key
//        else if(message.startsWith("mr")){
//             String XY[]=message.substring(2).split(" ");
//             int location_X=(int)Double.parseDouble(XY[0]);
//             int location_Y=(int)Double.parseDouble(XY[1]);
//             robot.mouseMove(location_X,location_Y);
//             robot.mousePress(KeyEvent.BUTTON3_DOWN_MASK);
//             robot.mouseRelease(KeyEvent.BUTTON3_DOWN_MASK);
//         }
    }

    @OnOpen
    public void start(Session session){
        //connections.add(session);
        System.out.println("Websocket connected");
        session.setMaxIdleTimeout(300000);
        if(inuse==true){
            return;
        }
        inuse=true;
        selected=true;
        this.session=session;
        openGame();
        timer=new Timer();
        timer.schedule(task,600000);
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
        if(streamingThread!=null&&selected==true){
            runningFlag=false;
            closeGame();
            inuse=false;
            System.out.println("The connection is closed");
        }
    }


    @OnError
    public void onError(Session session, Throwable error){
               System.out.println("Something went wrong");
               try{
                   session.close();
               }catch(IOException e){System.out.println("OnError:The websocket has already been closed");}
                error.printStackTrace();
             }
}


