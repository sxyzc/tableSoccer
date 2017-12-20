package activitytest.example.com.android_homeword_20.bluetooth;
/**
 * Created by samsung on 2017/5/5.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import activitytest.example.com.android_homeword_20.Ball;
import activitytest.example.com.android_homeword_20.bluetooth.*;
//import activitytest.example.com.android_homeword_20.bluetooth.BluetoothMsg.ServerOrCilent;

//import static activitytest.example.com.android_homeword_20.bluetooth.BluetoothMsg.serverOrCilent;


import static activitytest.example.com.android_homeword_20.GameView.mPlayerDx;
import static activitytest.example.com.android_homeword_20.GameView.PlayerDx;
import static activitytest.example.com.android_homeword_20.GameView.ball;

import static activitytest.example.com.android_homeword_20.MainActivity.windowWidth;
import static activitytest.example.com.android_homeword_20.MainActivity.windowHeight;

//import static activitytest.example.com.android_homeword_20.ViewCreated;

//import cn.user0308.scutkicking.MainView;

public class TransportData {

    /* 一些常量，代表服务器的名称 */
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    static int sendcnt = 0;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    //private MainView myview;
    private List<String> msgList = new ArrayList<String>();
    private BluetoothServerSocket mserverSocket = null;
    private BluetoothServerSocket oserverSocket = null;
    private BluetoothServerSocket stocSocket = null;
    private ServerThread startServerThread = null;
    private clientThread clientConnectThread = null;
    private BluetoothSocket socket = null;
    private BluetoothSocket osocket = null;
    private BluetoothSocket cgetsocket = null;

    private BluetoothSocket ball_socket = null;
    private BluetoothSocket player_socket = null;
    private BluetoothSocket score_socket = null;

    private BluetoothServerSocket ball_server_socket = null;
    private BluetoothServerSocket player_server_socket = null;
    private BluetoothServerSocket score_server_socket = null;

    private BluetoothDevice device = null;
    private readThread mreadThread = null;

    private boolean transfering=false;
    private boolean shutFlag=false;

    public boolean connected2 = false;//标记连接是否通

    public TransportData(){
        //myview = (MainView) findViewById(R.id.myview);
    }

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Handler LinkDetectedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_SHORT).show();
           /* if (msg.what == 1) {
                msgList.add((String) msg.obj);
            } else {
                msgList.add((String) msg.obj);
            }
           // mAdapter.notifyDataSetChanged();
            mListView.setSelection(msgList.size() - 1);*/
        }
    };

    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException ex) {
        }
        return false;
    }



    private void disconnectBluetooth() {

        /*  断开蓝牙过程
        disconnectButton = (Button) findViewById(R.id.btn_disconnect);
        disconnectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.CILENT) {
                    shutdownClient();
                } else if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.SERVICE) {
                    shutdownServer();
                }
                BluetoothMsg.isOpen = false;
                BluetoothMsg.serviceOrCilent = BluetoothMsg.ServerOrCilent.NONE;
                Toast.makeText(mContext, "已断开连接！", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }


    //开启服务连接

    public void openBluetooth() {

        if (BluetoothMsg.isOpen) {
            //Toast.makeText(mContext, "连接已经打开，可以通信。如果要再建立连接，请先断开！", Toast.LENGTH_SHORT).show();
            Log.d("BlueTest","连接已经打开，可以通信。如果要再建立连接，请先断开！");
            return;
        }

        if (BluetoothMsg.serverOrCilent == BluetoothMsg.ServerOrCilent.CILENT) {
            String address = BluetoothMsg.BlueToothAddress;
            if (!address.equals("null")) {
                device = mBluetoothAdapter.getRemoteDevice(address);
                clientConnectThread = new clientThread();
                clientConnectThread.start();
                BluetoothMsg.isOpen = true;
            } else {
                //Toast.makeText(mContext, "address is null !", Toast.LENGTH_SHORT).show();
                Log.d("TransportData","address is null");
            }
        } else if (BluetoothMsg.serverOrCilent == BluetoothMsg.ServerOrCilent.SERVICE) {
            startServerThread = new ServerThread();
            startServerThread.start();
            BluetoothMsg.isOpen = true;
        }

    }


    /* 停止服务器 */
    public void shutdownServer() {
        new Thread() {
            @Override
            public void run() {
                shutFlag=true;
                while(transfering);
                if (startServerThread != null) {
                    startServerThread.interrupt();
                    startServerThread = null;
                }
                if (mreadThread != null) {
                    mreadThread.interrupt();
                    mreadThread = null;
                }
                try {
                    if (socket != null) {
                        socket.close();
                        socket = null;
                    }
                    if (mserverSocket != null) {
                        mserverSocket.close();/* 关闭服务器 */
                        mserverSocket = null;
                    }
                    if (osocket != null) {
                        osocket.close();
                        osocket = null;
                    }
                    if (oserverSocket != null) {
                        oserverSocket.close();/* 关闭服务器 */
                        oserverSocket = null;
                    }
                    if (cgetsocket != null) {
                        cgetsocket.close();
                        cgetsocket = null;
                    }
                    if (stocSocket != null) {
                        stocSocket.close();/* 关闭服务器 */
                        stocSocket = null;
                    }

                    if (ball_socket != null) {
                        ball_socket.close();
                        ball_socket = null;
                    }
                    if (player_socket != null) {
                        player_socket.close();
                        player_socket = null;
                    }
                    if (score_socket != null) {
                        score_socket.close();
                        score_socket = null;
                    }
                    Log.v("LoadingTest","close sever ");
                } catch (IOException e) {
                    Log.e("server", "mserverSocket.close()", e);
                }

            }

            ;
        }.start();
    }

    ;

    /* 停止客户端连接 */
    public void shutdownClient() {
        new Thread() {
            @Override
            public void run() {
                shutFlag=true;
                while(transfering);
                if (clientConnectThread != null) {
                    clientConnectThread.interrupt();
                    clientConnectThread = null;
                }
                if (mreadThread != null) {
                    mreadThread.interrupt();
                    mreadThread = null;
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    socket = null;
                }
                if (cgetsocket != null) {
                    try {
                        cgetsocket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    cgetsocket = null;
                }
                if (osocket != null) {
                    try {
                        osocket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    osocket = null;
                }


                try {
                    if (ball_socket != null) {
                        ball_socket.close();
                        ball_socket = null;
                    }
                    if (player_socket != null) {
                        player_socket.close();
                        player_socket = null;
                    }
                    if (score_socket != null) {
                        score_socket.close();
                        score_socket = null;
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                Log.v("LoadingTest","close client ");

            }

            ;
        }.start();
    }

    //开启客户端
    private class clientThread extends Thread {
        @Override
        public void run() {
            try {
                //创建一个Socket连接：只需要服务器在注册时的UUID号
                // socket = device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);


                player_socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                ball_socket = device.createRfcommSocketToServiceRecord(UUID.fromString("C83DA007-3A9F-4249-9A96-18CACE25F84D"));
                score_socket = device.createRfcommSocketToServiceRecord(UUID.fromString("54B32C11-45BD-44A2-87BD-4DA72CB8E3EB"));



                //连接
                Message msg2 = new Message();
                msg2.obj = "请稍候，正在连接服务器:" + BluetoothMsg.BlueToothAddress;
                msg2.what = 0;
                LinkDetectedHandler.sendMessage(msg2);


                Message msg = new Message();
                msg.obj = "已经连接上服务端！可以发送信息。";
                msg.what = 0;
                LinkDetectedHandler.sendMessage(msg);

                Log.d("BlueTest","客户端准备连接");

                player_socket.connect();
                ball_socket.connect();
                score_socket.connect();

                connected2=true;//通
                Log.d("BlueTest","客户端已连接");

                //启动接受数据
                mreadThread = new readThread();
                mreadThread.start();
                Log.v("connect", "   readfinish");
            } catch (IOException e) {
                Log.e("connect", "", e);
                Message msg = new Message();
                msg.obj = "连接服务端异常！断开连接重新试一试。";
                msg.what = 0;
                LinkDetectedHandler.sendMessage(msg);
            }
        }
    }

    //开启服务器
    private class ServerThread extends Thread {
        @Override
        public void run() {

            try {
                    /* 创建一个蓝牙服务器
                     * 参数分别：服务器名称、UUID   */
                player_server_socket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                ball_server_socket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
                        UUID.fromString("C83DA007-3A9F-4249-9A96-18CACE25F84D"));
                score_server_socket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
                        UUID.fromString("54B32C11-45BD-44A2-87BD-4DA72CB8E3EB"));
                //  00001101-0000-1000-8000-00805F9B34FB   C83DA007-3A9F-4249-9A96-18CACE25F84D 54B32C11-45BD-44A2-87BD-4DA72CB8E3EB
                Log.d("server", "wait cilent connect...");

                Message msg = new Message();
                msg.obj = "请稍候，正在等待客户端的连接...";
                msg.what = 0;
                LinkDetectedHandler.sendMessage(msg);

                Log.d("BlueTest","服务器准备连接");
                    /* 接受客户端的连接请求 */
                player_socket = player_server_socket.accept();
                ball_socket = ball_server_socket.accept();
                score_socket = score_server_socket.accept();
                Log.d("BlueTest","服务器已连接");

                Message msg2 = new Message();
                String info = "客户端已经连接上！可以发送信息。";
                msg2.obj = info;
                msg.what = 0;
                LinkDetectedHandler.sendMessage(msg2);
                //启动接受数据
                mreadThread = new readThread();
                mreadThread.start();
                connected2=true;//通
                Log.d("BlueTest","flag5");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private InputStream player_is;
    private InputStream ball_is;
    private InputStream score_is;

    private  OutputStream player_os;
    private  OutputStream ball_os;
    private  OutputStream score_os;


    void sendPlayer(){

    }

    float receivePlayer(){
        float dx=0;
        return dx;
    }

    void sendBall(){
        //写入球数据
        String msgText;
        //if(BluetoothMsg.serviceOrCilent==BluetoothMsg.ServerOrCilent.CILENT)
        msgText = ball.x/windowWidth + "," + ball.y/windowHeight+",";
        Log.d("BlueData","已发送:"+msgText);
        // else
        //    msgText = mHero.getmAngle() + "," + mHero.getmSpeed()+","+mHero.getScreenX()+","+mHero.getScreenY();
        try {
            ball_os.write(msgText.getBytes());
            ball_os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void receiveBall()throws Exception{
        Ball res = new Ball();
        // Read from the InputStream
        byte[] buffer = new byte[128];
        int bytes;
        if ((bytes = ball_is.read(buffer)) > 0) {
            byte[] buf_data = new byte[bytes];
            for (int i = 0; i < bytes; i++) {
                buf_data[i] = buffer[i];
            }
            String s = new String(buf_data);
            Log.d("BlueData","已接收:"+s);
            String[] z = s.split(",");
            //if(BluetoothMsg.serviceOrCilent==BluetoothMsg.ServerOrCilent.CILENT)
            if (z.length > 0 && isDouble(z[0]))
                ball.x=(float) (Double.parseDouble(z[0]))*windowWidth;
            if (z.length > 1 && isDouble(z[1]))
                ball.y=(float)(Double.parseDouble(z[1]))*windowHeight;
        }

        //return res;
    }

    //读取数据
    private class readThread extends Thread {
        @Override
        public void run() {
            Log.d("BlueTest","读取线程启动");

            byte[] buffer = new byte[1024];
            int bytes;

            try {
                ball_is = ball_socket.getInputStream();
                ball_os = ball_socket.getOutputStream();
                player_is = player_socket.getInputStream();
                player_os = player_socket.getOutputStream();
                score_is = score_socket.getInputStream();
                score_os = score_socket.getOutputStream();

                while(true){

                    sendPlayer();
                    PlayerDx = receivePlayer();
                    if(BluetoothMsg.serverOrCilent == BluetoothMsg.ServerOrCilent.CILENT)receiveBall();
                    else if (BluetoothMsg.serverOrCilent == BluetoothMsg.ServerOrCilent.SERVICE)sendBall();


                }


            }catch (Exception e){

            }


            while (true) {
                if(shutFlag)break;
                transfering=true;
                /*try {
                    Thread.sleep(50);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }*/
                boolean ViewCreated = false;

                transfering=false;
            }
        }
    }





//    //读取数据
//    private class readThread extends Thread {
//        @Override
//        public void run() {
//
//            byte[] buffer = new byte[1024];
//            int bytes;
//            InputStream mmInStream = null;
//
//            try {
//                //if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.CILENT)
//                //    mmInStream = osocket.getInputStream();
//                // else
//                mmInStream = socket.getInputStream();
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
//            while (true) {
//                if(shutFlag)break;
//                transfering=true;
//                /*try {
//                    Thread.sleep(50);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }*/
//                boolean ViewCreated = false;
//                if(ViewCreated)
//                    try {
//
//                        //服务器发送ball数据
//                        //socket没数据时才写入
//
//                        Log.v("BTTest","availa    :   "+  cgetsocket.getInputStream().available() );
//                        if ( BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.SERVICE &&cgetsocket.getInputStream().available() != 0) {
//
//                            sendcnt++;
//                            Log.v("MainView", "send  " + sendcnt + "  avail: " + cgetsocket.getInputStream().available());
//
//                            Log.v("BTTest","flag1");
//
//                            //获取要传输球的信息开始
//                            String balltext = "";
//                            //int ballnum = mBallList.size();
//                            //balltext+=ballnum;
//                            /*
//                            for (Ball ball :
//                                    mBallList) {
//                                int balltype=1;//1 for BubbleBall 2 for ThornBall
//                                if(ball instanceof ThornBall)balltype=2;
//                                balltext += "," + ball.getX() + "," + ball.getY() + "," + ball.getAngle()+","+balltype;
//                            }
//                            */
//                            int bytecnt = balltext.getBytes().length;
//                            //if(bytecnt==0)balltext+="00000000000000000000000000000000";
//                            balltext = bytecnt + "" + balltext;
//                            //获取传输球相关的信息结束
//
//                            Log.v("LoadingTest","send  +   "+balltext);
//
//                            try {
//                                OutputStream os;
//
//                                os = cgetsocket.getOutputStream();
//                                //os.write(bytecnt);
//                                os.write(balltext.getBytes());
//                                os.flush();
//                                Log.v("MainView", " write and avai  " + cgetsocket.getInputStream().available());
//                                //Log.v("MainView","output avai  " + cgetsocket.getOutputStream())
//                                Log.v("BTTest", "string  output  " + balltext);
//                                String tb = "";
//                                for (int i = 0; i < balltext.getBytes().length; i++)
//                                    tb += balltext.getBytes()[i] + " ";
//                                Log.v("BTTest", "bytes   " + tb);
//
//
//                                //cgetsocket.getInputStream().reset();
//                                InputStream myinput = cgetsocket.getInputStream();
//                                byte[] myt = new byte[5000];
//                                int k = myinput.read(myt);
//                                Log.v("MainView", "read     " + k);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        //客户端接收ball数据
//                        if (BluetoothMsg.serviceOrCilent == BluetoothMsg.ServerOrCilent.CILENT) {
//                            InputStream stocStream = null;
//                            byte[] ballbuffer = new byte[409600];
//
//                            try {
//                                stocStream = cgetsocket.getInputStream();
//                            } catch (IOException e1) {
//                                // TODO Auto-generated catch block
//                                e1.printStackTrace();
//                            }
//
//
//                            try {
//                                //读一定长度的数据，能包括首位字节长度，30是估计值
//                                if (stocStream.available() > 30) {
//
//                                    byte[] t0 = new byte[30];
//                                    stocStream.read(t0);
//                                    if(!new String(t0).startsWith("0")) {
//                                        String tt = new String(t0);
//                                        Log.v("BTTest", "t1   " + tt);
//                                        int ff = tt.indexOf(",");
//                                        String t2 = tt.substring(0, ff);
//                                        Log.v("BTTest", "t2   " + t2);
//                                        bytes = Integer.parseInt(t2)-1;
//                                        Log.v("BTTest", "bytes   " + bytes);
//                                        String t3 = tt.substring(ff + 1, tt.length());
//                                        Log.v("BTTest", "t3   " + t3);
//                                        //bytes += t2.length();
//                                        //bytes = new DataInputStream(stocStream).readInt();
//                                        byte[] buf_data = new byte[bytes];
//                                        byte[] t4 = t3.getBytes();
//
//                                        // bytes-=t4.length;
//                                        for (int i = 0; i < t4.length && i < bytes; i++)
//                                            buf_data[i] = t4[i];
//                                        //ballbuffer[i]=
//                                        Log.v("BTTest","recive  bufdata    "+ new String(buf_data));
//                                        Log.v("BTTest","recive  t4    "+ new String(t4));
//                                        int readCount = t4.length ; // 已经成功读取的字节的个数
//                                        int finread=readCount;
//                                        Log.v("BTTest", " no wrong  " + readCount + "  " + bytes);
//                                        while (readCount < bytes) {
//                                            int ttt=stocStream.read(ballbuffer, readCount, bytes - readCount);
//                                            readCount += ttt;
//                                            Log.v("BTTest","ttt is   "+ttt);
//                                            Log.v("BTTest", "readcnt:  " + readCount + "   needs  " + bytes);
//                                            //if(readCount==bytes)break;
//                                        }
//                                        Log.v("BTTest", "read ok   " + ballbuffer.length);
//                                        String tr = "";
//                                        for (int i = 0; i < bytes; i++)
//                                            tr += ballbuffer[i] + " ";
//                                        Log.v("BTTest", "recive   " + tr);
//                                        Log.v("BTTest","recive      "+ new String(ballbuffer));
//                                        Log.v("BTTest", "rds ok   ");
//                                        for (int i = finread; i < bytes; i++) {
//                                            buf_data[i] = ballbuffer[i];
//                                        }
//                                        Log.v("BTTest","recive final  bufdata   "+ new String(buf_data));
//                                        String s = new String(buf_data);
//                                        String[] z = s.split(",");
//                                        int len = z.length / 4, pos = 0;
//                                        //mBallList.clear();
//                                         /*List<Ball> temList = new ArrayList<>();
//
//                                        for (int i = 0; i < len; i++) {
//                                            if (pos + 3 < z.length && isDouble(z[pos]) && isDouble(z[pos + 1]) && isDouble(z[pos + 2])&&z[pos+3].equals("1")) {
//                                                BubbleBall tem = new BubbleBall(Float.parseFloat(z[pos]), Float.parseFloat(z[pos + 1]), Float.parseFloat(z[pos + 2]));
//                                                temList.add(tem);
//                                                pos += 4;
//                                            } else if (pos + 3 < z.length && isDouble(z[pos]) && isDouble(z[pos + 1]) && isDouble(z[pos + 2])&&z[pos+3].equals("2")) {
//                                                ThornBall tem = new ThornBall(Float.parseFloat(z[pos]), Float.parseFloat(z[pos + 1]), Float.parseFloat(z[pos + 2]));
//                                                temList.add(tem);
//                                                pos += 4;
//                                            }else pos++;
//                                        }
//                                        mBallList=temList;*/
//
//
//                                        if (!connected2) connected2 = true;
//                                    }
//                                    else{
//                                        byte [] temm=new byte[102400];
//                                        stocStream.read(temm);
//                                    }
//                                    //发送数据给服务器，说明可以接收数据
//                                    try {
//                                        OutputStream os = cgetsocket.getOutputStream();
//                                        os.write("ok".getBytes());
//                                        os.flush();
//                                    } catch (IOException e) {
//                                        // Log.e("connect", "", e);
//                                    }
//                                }
//                            } catch (IOException e1) {
//                                // TODO Auto-generated catch block
//                                Log.v("MainView","  wrong");e1.printStackTrace();
//
//                            }
//
//
//                        }
//
//
//
////                        //写入角色数据
////                        String msgText;
////                        //if(BluetoothMsg.serviceOrCilent==BluetoothMsg.ServerOrCilent.CILENT)
////                        msgText = mHero.getmAngle() + "," + mHero.getmSpeed()+","+mHero.getScreenX()+","+mHero.getScreenY();
////                        // else
////                        //    msgText = mHero.getmAngle() + "," + mHero.getmSpeed()+","+mHero.getScreenX()+","+mHero.getScreenY();
////                        try {
////                            OutputStream os;
////                            os = socket.getOutputStream();
////                            os.write(msgText.getBytes());
////                            os.flush();
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////                        // Read from the InputStream
////                        if ((bytes = mmInStream.read(buffer)) > 0) {
////                            byte[] buf_data = new byte[bytes];
////                            for (int i = 0; i < bytes; i++) {
////                                buf_data[i] = buffer[i];
////                            }
////                            String s = new String(buf_data);
////                            String[] z = s.split(",");
////                            //if(BluetoothMsg.serviceOrCilent==BluetoothMsg.ServerOrCilent.CILENT)
////                            if (z.length>0&&isDouble(z[0]))
////                                oHero.setAngle(Double.parseDouble(z[0]));
////                            if (z.length>1&&isDouble(z[1]))
////                                oHero.setmSpeed(Double.parseDouble(z[1]));
////                            if (z.length>2&&isDouble(z[2]))
////                                oHero.setScreenX((int) Double.parseDouble(z[2]));
////                            if (z.length>3&&isDouble(z[3]))
////                                oHero.setScreenY((int) Double.parseDouble(z[3]));
////
////                        }
//                    } catch (IOException e) {
//                        try {
//                            mmInStream.close();
//                        } catch (IOException e1) {
//                            // TODO Auto-generated catch block
//                            e1.printStackTrace();
//                        }
//                        Log.v("BTTest","wwwwwwwwwwwwwwwwww");
//                        break;
//                    }
//                transfering=false;
//            }
//        }
//    }


}