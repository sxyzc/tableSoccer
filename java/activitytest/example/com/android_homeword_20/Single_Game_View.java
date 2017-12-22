package activitytest.example.com.android_homeword_20;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import activitytest.example.com.android_homeword_20.R;
import activitytest.example.com.android_homeword_20.bluetooth.BluetoothMsg;
import activitytest.example.com.android_homeword_20.bluetooth.TransportData;

public class Single_Game_View extends AppCompatActivity {

    //初始化对象
    private All_ViewGroup myGroupView;
    private GameView gameView;
    private TextView top;
    private TextView mS;
    private TextView S;
    private TextView player;
    private TextView system;
    private View bottom;
    private Button button_music;
    private Button button_se;
    private Button button_pause;
    private Intent intent;
    private int button_on_off;//用于判断音乐开关的开闭
    private int button_on_off_se;
    private SoundPool soundPool;
    private Chronometer chronometer;
    private MydatabaseHelper dbHelper;

    public static Context sContext;
    public static TransportData TD;
    public static boolean ViewCreated = false;//标记View是否已经可见（避免蓝牙在View不可见时传数据）
    private LoadingThread loadingThread;//判断连接是否成功的线程
    private Handler loadingHandler;//改动setviewcontent的Handler

    private int diff;
    private int bgm_st;
    private int bgm_sl;
    private int se;
    private int time;

    private class LoadingThread extends Thread {
        @Override
        public void run() {
            Log.d("LoadingTest", "Loading start");
            Log.v("LoadingTest","flag1");
            while(!TD.connected2);
            Log.v("LoadingTest","flag2");
            //setContentView(mainView);

            loadingHandler.sendEmptyMessage(1);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取数据库
        initDB();

        //初始化总GroupView类
        myGroupView = new All_ViewGroup(this);
        myGroupView.setBackgroundResource(R.drawable.game_background);

        //创建游戏View
        gameView = new GameView(this);

        //初始化计时器
        chronometer = new Chronometer(this);
        chronometer.setTextSize(40);
        chronometer.setTextColor(Color.WHITE);
        chronometer.setGravity(Gravity.CENTER);
        chronometer.start();
        chronometer.setOnChronometerTickListener(new OnChronometerTickListenerImpl());

        //上方View
        top = new TextView(this);

        //下方View
        bottom = new TextView(this);

        //音乐开关、音效开关、暂停开关
        button_music = new Button(this);
        button_se = new Button(this);
        button_pause = new Button(this);

        mS = new TextView(this);
        S = new TextView(this);
        player = new TextView(this);
        system = new TextView(this);
        mS.setText("0");
        mS.setGravity(Gravity.CENTER);
        mS.setTextColor(Color.WHITE);
        mS.setTextSize(40);
        S.setText("0");
        S.setGravity(Gravity.CENTER);
        S.setTextColor(Color.WHITE);
        S.setTextSize(40);
        player.setText("Player");
        player.setGravity(Gravity.CENTER);
        player.setTextColor(Color.WHITE);
        player.setTextSize(40);
        system.setText("System");
        system.setGravity(Gravity.CENTER);
        system.setTextColor(Color.WHITE);
        system.setTextSize(40);

        //向总ViewGroup里添加各个子View
        myGroupView.addView(gameView, 0);
        myGroupView.addView(chronometer, 1);
        myGroupView.addView(button_music,2);
        myGroupView.addView(button_se,3);
        myGroupView.addView(button_pause,4);
        myGroupView.addView(S,5);
        myGroupView.addView(mS,6);
        myGroupView.addView(player,7);
        myGroupView.addView(system,8);




        sContext = this;
        ViewCreated = false;
        if(BluetoothMsg.serverOrCilent == BluetoothMsg.ServerOrCilent.NONE)
            setContentView(myGroupView);
        else{
            setContentView(R.layout.activity_loading);
            TD = new TransportData();

            TD.openBluetooth();
            loadingThread=new LoadingThread();
            loadingThread.start();
            loadingHandler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    if(msg.what == 1)  // handler接收到相关的消息后
                    {
                        setContentView(myGroupView); // 显示真正的应用界面
                        //ViewCreated=true;
                    }
                }
            };
        }

        //初始化intent
        intent = new Intent(this, activitytest.example.com.android_homeword_20.Service.MyService.class);
        intent.putExtra("number",bgm_sl);

        //音乐按钮初始化
        button_on_off = bgm_st;
        if(button_on_off == 1) {
            button_music.setBackgroundResource(R.drawable.music_on);
            startService(intent);
        }else {
            button_music.setBackgroundResource(R.drawable.music_off);
        }
        //音效按钮初始化
        button_on_off_se = se;
        if(button_on_off_se == 1){
            button_se.setBackgroundResource(R.drawable.se_on);
        }else {
            button_se.setBackgroundResource(R.drawable.se_off);
        }
        //暂停按钮初始化
        button_pause.setBackgroundResource(R.drawable.pause);

        //初始化背景音乐播放器
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
        final HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
        soundMap.put(1,soundPool.load(this,R.raw.cheer,2));
        soundMap.put(2,soundPool.load(this,R.raw.sigh,2));
        soundMap.put(3,soundPool.load(this,R.raw.kick,1));

        //对对方比分进行监听
        gameView.setMyListener(new GameView.MyListener() {
            @Override
            public void notifyDataChage(int a) {
                S.setText(Integer.toString(gameView.getScore()));
                if(se == 1){
                    soundPool.play(soundMap.get(2),1,1,0,0,1);
                }
            }
        });

        //对己方比分进行监听
        gameView.setListener(new GameView.MyListener() {
            @Override
            public void notifyDataChage(int a) {
                mS.setText(Integer.toString(gameView.getmScore()));
                if(se == 1) {
                    soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
                }
            }
        });

        //对音乐按钮进行监听
        button_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button_on_off == 1){
                    stopService(intent);
                    button_on_off = 0;
                    button_music.setBackgroundResource(R.drawable.music_off);
                }else{
                    startService(intent);
                    button_on_off = 1;
                    button_music.setBackgroundResource(R.drawable.music_on);
                }
            }
        });

        //对音效按钮进行监听
        button_se.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button_on_off_se == 1){
                    button_on_off_se = 0;
                    button_se.setBackgroundResource(R.drawable.se_off);
                    gameView.setMusic_se(0);
                }else{
                    button_on_off_se = 1;
                    button_se.setBackgroundResource(R.drawable.se_on);
                    gameView.setMusic_se(1);
                }
            }
        });

        //对暂停按钮进行监听
        button_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.isRun = false;
                chronometer.stop();
                stopService(intent);
                button_pause.setBackgroundResource(R.drawable.pause_);
                new AlertDialog.Builder(Single_Game_View.this)
                        .setTitle("游戏暂停")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("继续游戏", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gameView.isRun = true;
                                gameView.reStart();
                                TD.reStart();
                                chronometer.setBase(convertStrTimeToLong(chronometer.getText().toString()));
                                chronometer.start();
                                if(button_on_off == 1){
                                    startService(intent);
                                }
                                button_pause.setBackgroundResource(R.drawable.pause);
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Single_Game_View.this.finish();
                            }
                        })
                        .show();
            }
        });

        //向gameView传入对应的背景音效、背景音乐、游戏难度的参数
        gameView.setMusic_se(button_on_off_se);
        gameView.setKickMusic(soundPool,soundMap);
        gameView.setDiff(diff);
    }

    //对计时器进行监听
    public class OnChronometerTickListenerImpl implements Chronometer.OnChronometerTickListener {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            String time_ = chronometer.getText().toString();
            int gameTime =  time;
            String gameTime_ ;
            if(gameTime == 1){
                gameTime_ = "00:30";
            }else if(gameTime == 2){
                gameTime_ = "01:00";
            }else {
                gameTime_ = "02:00";
            }
            if(gameTime_.equals(time_)){//判断什么时候比赛结束
                int mScore = gameView.getmScore();
                int Score = gameView.getScore();
                if (mScore > Score){
                    new AlertDialog.Builder(Single_Game_View.this)
                            .setTitle("游戏结束")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Single_Game_View.this.finish();
                                }
                            })
                            .setMessage("You win")
                            .show();
                }else if(mScore == Score){
                    new AlertDialog.Builder(Single_Game_View.this)
                            .setTitle("游戏结束")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Single_Game_View.this.finish();
                                }
                            })
                            .setMessage("Ended in a draw")
                            .show();
                }else {
                    new AlertDialog.Builder(Single_Game_View.this)
                            .setTitle("游戏结束")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Single_Game_View.this.finish();
                                }
                            })
                            .setMessage("You lost")
                            .show();
                }
                //比赛结束后，
                gameView.isRun =false;
                chronometer.stop();
                stopService(intent);
                statistic_data(mScore, Score);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(button_on_off == 1){
            startService(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(intent);
    }

    protected void onDestroy() {
        Log.v("MainActivity", "onDestroy");

        if (BluetoothMsg.serverOrCilent != BluetoothMsg.ServerOrCilent.NONE)
            TD.closeBluetooth();
            gameView.isRun = false;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            stopService(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    //获取数据库
    public void initDB(){
        dbHelper = new MydatabaseHelper(this,"GameRecord.db",null,3);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("setting",null,null,null,null,null,null);
        cursor.moveToFirst();
        diff=cursor.getInt(cursor.getColumnIndex("diffcult"));
        bgm_st =cursor.getInt(cursor.getColumnIndex("music_bool"));
        bgm_sl=cursor.getInt(cursor.getColumnIndex("music_select"));
        se=cursor.getInt(cursor.getColumnIndex("yinxiao"));
        time=cursor.getInt(cursor.getColumnIndex("time"));
        cursor.close();
    }

    public void statistic_data(int mScore,int Score){
        dbHelper = new MydatabaseHelper(Single_Game_View.this,"GameRecord.db",null,3);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Record",null,null,null,null,null,null);
        cursor.moveToFirst();
        int total_point = cursor.getInt(cursor.getColumnIndex("total_point"));
        int max_point_in_one_stage = cursor.getInt(cursor.getColumnIndex("max_point_in_one_stage"));
        if(mScore>max_point_in_one_stage){max_point_in_one_stage=mScore;}
        int num_of_stage =cursor.getInt(cursor.getColumnIndex("num_of_stage"));
        ContentValues values = new ContentValues();
        values.put("max_point_in_one_stage",max_point_in_one_stage);
        values.put("total_point",total_point+mScore);
        values.put("num_of_stage",num_of_stage+1);
        db.update("Record",values,null,null);
        values.clear();
        cursor.moveToFirst();
        if(mScore > Score){
            //加上进了几个球
            //丢了几个球
            //胜场数++
            int num_of_win_stage = cursor.getInt(cursor.getColumnIndex("num_of_win_stage"));
            values.put("num_of_win_stage",num_of_win_stage+1);
            db.update("Record",values,null,null);
            values.clear();
        }else if(mScore < Score){
            int num_of_loose_stage = cursor.getInt(cursor.getColumnIndex("num_of_loose_stage"));
            values.put("num_of_loose_stage",num_of_loose_stage+1);
            db.update("Record",values,null,null);
            values.clear();
        }else {
            int num_of_equal_stage = cursor.getInt(cursor.getColumnIndex("num_of_equal_stage"));
            values.put("num_of_equal_stage",num_of_equal_stage+1);
            db.update("Record",values,null,null);
            values.clear();
        }
        cursor.close();
        values.put("music_bool",button_on_off);
        values.put("yinxiao",button_on_off_se);
        db.update("setting",values,null,null);
        values.clear();
    }

    //用于暂停后恢复计时
    protected long convertStrTimeToLong(String strTime){
        String [] timeArry = strTime.split(":");
        long longtime = 0;
        if(timeArry.length == 2){
            longtime = Integer.parseInt(timeArry[0])*1000*60+Integer.parseInt(timeArry[1])*1000;
        }else if(timeArry.length == 3){
            longtime = Integer.parseInt(timeArry[0])*1000*60*60+Integer.parseInt(timeArry[1])*1000*60+Integer.parseInt(timeArry[0])*1000;
        }
        return SystemClock.elapsedRealtime() - longtime;
    }
}
