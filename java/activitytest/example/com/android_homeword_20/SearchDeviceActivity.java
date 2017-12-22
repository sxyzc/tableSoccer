package activitytest.example.com.android_homeword_20;


import activitytest.example.com.android_homeword_20.bluetooth.BluetoothMsg;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SearchDeviceActivity extends Activity implements OnItemClickListener{

    private BluetoothAdapter blueadapter=null;
    private DeviceReceiver mydevice=new DeviceReceiver();
    private List<String> deviceList=new ArrayList<String>();
    private ListView deviceListview;
    private Button btserch,tobeclient,tobeserver;
    private ArrayAdapter<String> adapter;
    private boolean hasregister=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BlueTest","create");
        //隐藏电池时间等
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏结束
        setContentView(R.layout.activity_search_device);
        setView();
        setBluetooth();

    }

    private void setView(){

        deviceListview=(ListView)findViewById(R.id.devicelist);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceList);
        //this one may be a null pointer exception
        if(deviceListview==null) Log.d("SearchDeviceActivity","deviceListView is null");
        if(adapter==null) Log.d("SearchDeviceActivity","adapter is null");
        deviceListview.setAdapter(adapter);
        deviceListview.setOnItemClickListener(this);
        btserch=(Button)findViewById(R.id.start_seach);
        btserch.setOnClickListener(new ClinckMonitor());
        tobeclient=(Button)findViewById(R.id.tobeclient);
        tobeserver=(Button)findViewById(R.id.tobeserver);
        tobeserver.setOnClickListener(new OnClickListener() {//将btn_cancel按钮绑定在点击监听器上
            @Override
            public void onClick(View v) {
                Log.d("SearchDeviceActivity","server click");
                //Toast.makeText(MainActivity.sContext,"waiting for client",Toast.LENGTH_SHORT).show();
                BluetoothMsg.serverOrCilent= BluetoothMsg.ServerOrCilent.SERVICE;
            }
        });
        tobeclient.setOnClickListener(new OnClickListener() {//将btn_cancel按钮绑定在点击监听器上
            @Override
            public void onClick(View v) {
                Log.d("SearchDeviceActivity","click click");
                //不能用toast,因为此时MainActivity还没初始化,sContext为null
                //Toast.makeText(MainActivity.sContext,"waiting for server",Toast.LENGTH_SHORT).show();
                BluetoothMsg.serverOrCilent=BluetoothMsg.ServerOrCilent.CILENT;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册蓝牙接收广播
        if(!hasregister){
            hasregister=true;
            IntentFilter filterStart=new IntentFilter(BluetoothDevice.ACTION_FOUND);
            IntentFilter filterEnd=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(mydevice, filterStart);
            registerReceiver(mydevice, filterEnd);
        }

    }

    @Override
    protected void onDestroy() {
        if(blueadapter!=null&&blueadapter.isDiscovering()){
            blueadapter.cancelDiscovery();
        }
        if(hasregister){
            hasregister=false;
            unregisterReceiver(mydevice);
        }
        super.onDestroy();
    }
    /**
     * Setting Up Bluetooth
     */
    private void setBluetooth(){
        blueadapter=BluetoothAdapter.getDefaultAdapter();

        if(blueadapter!=null){  //Device support Bluetooth
            //确认开启蓝牙
            if(!blueadapter.isEnabled()){
                //请求用户开启
                Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, RESULT_FIRST_USER);
                //使蓝牙设备可见，方便配对
                Intent in=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
                startActivity(in);
                //直接开启，不经过提示
                blueadapter.enable();
            }
        }
        else{   //Device does not support Bluetooth
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("没有蓝牙设备");
            dialog.setMessage("您的设备不支持蓝牙");

            dialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);//add by user0308,if user click cancel ,system exit
                        }
                    });
            dialog.show();
        }
    }

    /**
     * Finding Devices
     */
    private void findAvalibleDevice(){
        //获取可配对蓝牙设备
        Set<BluetoothDevice> device=blueadapter.getBondedDevices();

        if(blueadapter!=null&&blueadapter.isDiscovering()){
            deviceList.clear();
            adapter.notifyDataSetChanged();
        }
        if(device.size()>0){ //存在已经配对过的蓝牙设备
            for(Iterator<BluetoothDevice> it=device.iterator();it.hasNext();){
                BluetoothDevice btd=it.next();

                if(!deviceList.contains((String)(btd.getName()+'\n'+btd.getAddress())))
                    deviceList.add(btd.getName()+'\n'+btd.getAddress());
                adapter.notifyDataSetChanged();
            }
        }else{  //不存在已经配对过的蓝牙设备
            deviceList.add("没有匹配到蓝牙设备");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(resultCode){
            case RESULT_OK:
                findAvalibleDevice();
                break;
            case RESULT_CANCELED:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class ClinckMonitor implements OnClickListener{

        @Override
        public void onClick(View v) {
            if(blueadapter.isDiscovering()){
                blueadapter.cancelDiscovery();
                btserch.setText("重新搜索");
            }else{
                findAvalibleDevice();
                blueadapter.startDiscovery();
                btserch.setText("停止搜索");
            }
        }
    }
    /**
     * 蓝牙搜索状态广播监听
     * @author Andy
     *
     */
    private class DeviceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action =intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){    //搜索到新设备
                BluetoothDevice btd=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //搜索没有配过对的蓝牙设备
                if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if(deviceList.contains((String)(btd.getName()+'\n'+btd.getAddress()))) {
                        ;
                    }
                    else {
                        deviceList.add(btd.getName()+'\n'+btd.getAddress());
                        adapter.notifyDataSetChanged();
                    }

                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){   //搜索结束

                if (deviceListview.getCount() == 0) {
                    deviceList.clear();
                    deviceList.add("没有匹配到蓝牙设备");
                    adapter.notifyDataSetChanged();
                }
                btserch.setText("重新搜索");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

        Log.e("msgParent", "Parent= "+arg0);
        Log.e("msgView", "View= "+arg1);
        Log.e("msgChildView", "ChildView= "+arg0.getChildAt(pos-arg0.getFirstVisiblePosition()));

        final String msg = deviceList.get(pos);

        if(blueadapter!=null&&blueadapter.isDiscovering()){
            blueadapter.cancelDiscovery();
            btserch.setText("重新搜索");
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);// 定义一个弹出框对象
        dialog.setTitle("确定连接该设备？");
        dialog.setMessage(msg);
        dialog.setPositiveButton("连接",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BluetoothMsg.BlueToothAddress=msg.substring(msg.length()-17);

                        if(BluetoothMsg.lastblueToothAddress!=BluetoothMsg.BlueToothAddress){
                            BluetoothMsg.lastblueToothAddress=BluetoothMsg.BlueToothAddress;
                        }

                        //进入游戏活动界面
                        Intent in=new Intent(SearchDeviceActivity.this, Single_Game_View.class);
                        startActivity(in);
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BluetoothMsg.BlueToothAddress = null;
                    }
                });
        dialog.show();
    }

}