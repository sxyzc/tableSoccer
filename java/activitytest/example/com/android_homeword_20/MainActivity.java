package activitytest.example.com.android_homeword_20;

import android.app.Activity;;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import activitytest.example.com.android_homeword_20.R;
import activitytest.example.com.android_homeword_20.bluetooth.TransportData;

public class MainActivity extends Activity {

    public static int windowHeight ;
    public static int windowWidth ;
    private MydatabaseHelper dbHelper;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获得手机屏幕的高宽
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        windowHeight = displayMetrics.heightPixels;
        windowWidth = displayMetrics.widthPixels;

        //用于设置全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //button = findViewById(R.idh.start_game);

        initDataBase();

    /*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("请选择难度")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[] {"简单","普通","困难" },
                                0, new AlertDialog.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which){
                                        dialog.dismiss();
                                        Toast.makeText(MainActivity.this,"你选择了 :"+ which,Toast.LENGTH_LONG).show();

                                        //看选择了哪个，然后设置难度

                                        Intent intent = new Intent(MainActivity.this,Single_Game_View.class);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("取消",null).show();
            }
        });*/
    }

    public void initDataBase(){
        //下面这一块到时候提取为databaseinit
        //创建数据库
        dbHelper = new MydatabaseHelper(this,"GameRecord.db",null,3);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("max_point_in_one_stage",0);
        values.put("total_point",0);
        values.put("num_of_stage",0);
        values.put("num_of_win_stage",0);
        values.put("num_of_loose_stage",0);
        values.put("num_of_equal_stage",0);
        db.insert("Record",null,values);
        values.clear();

        values.put("diffcult",1);
        values.put("music_bool",1);
        values.put("music_select",1);
        values.put("yinxiao",1);
        values.put("time",2);
        db.insert("setting",null,values);
        values.clear();
    }


    //转跳到单机游戏界面
    public void start_game(View a){
        Intent intent = new Intent(MainActivity.this,Single_Game_View.class);
        startActivity(intent);
    }

    //转跳到双人对战界面
    public void bluetooth_game(View c){
        Intent intent= new Intent(MainActivity.this,SearchDeviceActivity.class);
        startActivity(intent);
    }

    //转跳到个人数据界面
    public void data_(View a){
        Intent intent= new Intent(MainActivity.this,show_records.class);
        startActivity(intent);

    }

    //转跳到设置界面
    public void setting(View a){
        Intent intent= new Intent(MainActivity.this,setting.class);
        startActivity(intent);
    }

    protected void onDestroy() {
        Log.v("MainActivity", "onDestroy");
        /*if (BluetoothMsg.serverOrCilent == BluetoothMsg.ServerOrCilent.CILENT)TD.shutdownClient();
        else TD.shutdownServer();
        TD.shutdownServer();
        TD.shutdownClient();*/
        super.onDestroy();
    }
}
