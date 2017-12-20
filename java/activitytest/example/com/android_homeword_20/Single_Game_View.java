package activitytest.example.com.android_homeword_20;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Single_Game_View extends AppCompatActivity {

    //初始化对象
    private All_ViewGroup myGroupView;
    private GameView gameView;
    private Left_ViewGroup left_viewGroup;
    private TextView textView0;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private ImageView top;
    private View bottom;
    private Button music;
    private Intent intent;
    private int button_on_off;//用于判断音乐开关的开闭
    private SoundPool soundPool;
    private Chronometer chronometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化总GroupView类
        myGroupView = new All_ViewGroup(this);
        //创建游戏View
        gameView = new GameView(this);

        //左侧代码：
        left_viewGroup = new Left_ViewGroup(this);
        textView0 = new TextView(this);
        textView1 = new TextView(this);
        textView2 = new TextView(this);
        textView3 = new TextView(this);
        textView4 = new TextView(this);

        chronometer = new Chronometer(this);
        chronometer.start();

        chronometer.setOnChronometerTickListener(new OnChronometerTickListenerImpl());

        left_viewGroup.addView(textView0,0);
        left_viewGroup.addView(textView1,1);
        left_viewGroup.addView(chronometer,2);
        left_viewGroup.addView(textView3,3);
        left_viewGroup.addView(textView4,4);

        //上方View
        top = new ImageView(this);
        top.setBackgroundColor(555555555);

        bottom = new TextView(this);

        music = new Button(this);


        //填充4个子View
        myGroupView.addView(gameView, 0);
        myGroupView.addView(left_viewGroup, 1);
        myGroupView.addView(top, 2);
        myGroupView.addView(bottom,3);
        myGroupView.addView(music,4);
        setContentView(myGroupView);

        intent = new Intent(this, MyService.class);
        startService(intent);
        button_on_off = 1;
        music.setBackgroundResource(R.drawable.on);

        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
        final HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
        soundMap.put(1,soundPool.load(this,R.raw.goal,1));
        soundMap.put(2,soundPool.load(this,R.raw.lost_ball,1));



        gameView.setMyListener(new GameView.MyListener() {
            @Override
            public void notifyDataChage(int a) {
                textView1.setText(Integer.toString(gameView.getScore()));
                soundPool.play(soundMap.get(2),1,1,0,0,1);
            }
        });

        gameView.setListener(new GameView.MyListener() {
            @Override
            public void notifyDataChage(int a) {
                textView3.setText(Integer.toString(gameView.getmScore()));
                soundPool.play(soundMap.get(1),1,1,0,0,1);
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button_on_off == 1){
                    stopService(intent);
                    button_on_off = 0;
                    music.setBackgroundResource(R.drawable.off);
                }else if(button_on_off == 0){
                    startService(intent);
                    button_on_off = 1;
                    music.setBackgroundResource(R.drawable.on);
                }
            }
        });

    }

    public class OnChronometerTickListenerImpl implements Chronometer.OnChronometerTickListener {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            String time = chronometer.getText().toString();
            if("00:05".equals(time)){//判断什么时候比赛结束
                new AlertDialog.Builder(Single_Game_View.this)
                        .setTitle("游戏结束")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定",null)
                        .setNegativeButton("ff",null)
                        .show();
                Log.d("fffffff","fjfifjidjfijfifjfif");
                //比赛结束后，
                int mScore = gameView.getmScore();
                int Score = gameView.getScore();
                if(mScore > Score){
                    //加上进了几个球
                    //丢了几个球
                    //胜场数++
                }else if(mScore < Score){

                }else {

                }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            stopService(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
