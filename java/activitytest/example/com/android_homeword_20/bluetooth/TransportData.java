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
                    if (player_socket != null) {
                        player_socket.close();
                        player_socket = null;
                    }
                    if (player_server_socket != null) {
                        player_server_socket.close();/* 关闭服务器 */
                        player_server_socket = null;
                    }
                    if (ball_socket != null) {
                        ball_socket.close();
                        ball_socket = null;
                    }
                    if (ball_server_socket != null) {
                        ball_server_socket.close();/* 关闭服务器 */
                        ball_server_socket = null;
                    }

                    Log.d("BlueTest","服务器连接关闭");
                } catch (IOException e) {
                    Log.e("server", "服务器关闭异常", e);
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
                try {
                    if (player_socket != null) {
                        player_socket.close();
                        player_socket = null;
                    }
                    if (ball_socket != null) {
                        ball_socket.close();
                        ball_socket = null;
                    }

                    Log.d("BlueTest","客户端连接关闭");
                } catch (IOException e) {
                    Log.e("server", "客户端关闭异常", e);
                }

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
                //score_socket = device.createRfcommSocketToServiceRecord(UUID.fromString("54B32C11-45BD-44A2-87BD-4DA72CB8E3EB"));



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
               // score_socket.connect();

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
                //score_server_socket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
                //        UUID.fromString("54B32C11-45BD-44A2-87BD-4DA72CB8E3EB"));
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
                //score_socket = score_server_socket.accept();
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
        //写入球员dx数据
        String msgText;
        //if(BluetoothMsg.serviceOrCilent==BluetoothMsg.ServerOrCilent.CILENT)
        msgText = mPlayerDx/windowWidth + "," ;
        Log.d("BlueData","已发送:"+msgText);
        // else
        //    msgText = mHero.getmAngle() + "," + mHero.getmSpeed()+","+mHero.getScreenX()+","+mHero.getScreenY();
        try {
            player_os.write(msgText.getBytes());
            player_os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void receivePlayer()throws  Exception{
        byte[] buffer = new byte[128];
        int bytes;
        if ((bytes = player_is.read(buffer)) > 0) {
            byte[] buf_data = new byte[bytes];
            for (int i = 0; i < bytes; i++) {
                buf_data[i] = buffer[i];
            }
            String s = new String(buf_data);
            Log.d("BlueData","已接收:"+s);
            String[] z = s.split(",");
            //if(BluetoothMsg.serviceOrCilent==BluetoothMsg.ServerOrCilent.CILENT)
            if (z.length > 0 && isDouble(z[0]))
                PlayerDx=(float) (Double.parseDouble(z[0]))*windowWidth;
        }
    }

    void sendBall(){
        //写入球数据
        String msgText;
        //if(BluetoothMsg.serviceOrCilent==BluetoothMsg.ServerOrCilent.CILENT)
        msgText = ball.x/windowWidth + "," + ball.y/windowHeight+",";
        //Log.d("BlueData","已发送:"+msgText);
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
            //Log.d("BlueData","已接收:"+s);
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

                while(true){

                    sendPlayer();
                    receivePlayer();
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


}