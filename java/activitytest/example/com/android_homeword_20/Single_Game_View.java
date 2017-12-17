package activitytest.example.com.android_homeword_20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Single_Game_View extends AppCompatActivity {

    public static int windowHeight ;
    public static int windowWidth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //创建GroupView类
        All_ViewGroup myGroupView = new All_ViewGroup(this);


        //创建游戏View
        GameView gameView = new GameView(this);
        myGroupView.addView(gameView, 0);

        //左侧
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View left_viewGroup = (View) layoutInflater.inflate(R.layout.left_layout, null);

        /*TextView textView = (TextView)findViewById(R.id.tv_1);
        textView.setText("ininini");
        */
        /*
        *
        * 上面的方法没办法实现修改
        * 考虑是否可以直接用照片的形式替换
        *
        * 然后再考虑如何显示时间的问题
        * 实在不行就按照一局多少个球赢
        *
        * */
        myGroupView.addView(left_viewGroup, 1);

        //上方View
        ImageView imageView1 = new ImageView(this);
        imageView1.setBackgroundColor(555555555);
        myGroupView.addView(imageView1, 2);

        //下方View
        ImageView imageView2 = new ImageView(this);
        imageView2.setBackgroundColor(555555555);
        myGroupView.addView(imageView2, 3);

        setContentView(myGroupView);
    }
}
