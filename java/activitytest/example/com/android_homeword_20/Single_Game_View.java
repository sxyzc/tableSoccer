package activitytest.example.com.android_homeword_20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView bottom;

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

        left_viewGroup.addView(textView0,0);
        left_viewGroup.addView(textView1,1);
        left_viewGroup.addView(textView2,2);
        left_viewGroup.addView(textView3,3);
        left_viewGroup.addView(textView4,4);

        //上方View
        top = new ImageView(this);
        top.setBackgroundColor(555555555);

        bottom = new TextView(this);

        //填充4个子View
        myGroupView.addView(gameView, 0);
        myGroupView.addView(left_viewGroup, 1);
        myGroupView.addView(top, 2);
        myGroupView.addView(bottom,3);

        setContentView(myGroupView);

        gameView.setMyListener(new GameView.MyListener() {
            @Override
            public void notifyDataChage(int a) {
                textView1.setText(Integer.toString(gameView.getScore()));
            }
        });

        gameView.setListener(new GameView.MyListener() {
            @Override
            public void notifyDataChage(int a) {
                textView3.setText(Integer.toString(gameView.getScore()));
            }
        });
    }
}
