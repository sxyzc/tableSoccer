package activitytest.example.com.android_homeword_20;

import android.app.Activity;;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import activitytest.example.com.android_homeword_20.bluetooth.TransportData;

public class MainActivity extends Activity {

    /*
    int mposition = 1;//自己一方的位置，1表示左边是自己，-1表示右边是自己
    public static int pHeight = 10;//球员高
    public static int pWidth = 8;//球员宽
   */
    //感觉上面这一部分好像没有用到吧？

    public static int windowHeight ;
    public static int windowWidth ;

    public static TransportData TD;

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

        TD = new TransportData();
        TD.openBluetooth();
    }

    public void start_game(View c){
            Intent intent = new Intent(MainActivity.this, Single_Game_View.class);
            startActivity(intent);
    }
}
