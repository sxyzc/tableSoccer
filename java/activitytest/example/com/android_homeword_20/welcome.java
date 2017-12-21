package activitytest.example.com.android_homeword_20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import activitytest.example.com.android_homeword_20.R;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        handler.sendEmptyMessageDelayed(0,3000);
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            getHome();
            super.handleMessage(msg);
        }
        public void getHome(){
            Intent intent = new Intent(welcome.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
