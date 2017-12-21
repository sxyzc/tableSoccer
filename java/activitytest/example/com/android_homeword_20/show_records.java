package activitytest.example.com.android_homeword_20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.app.Activity;;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class show_records extends Activity {
    private MydatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_records);
        dbHelper = new MydatabaseHelper(this,"GameRecord.db",null,3);
       SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Record",null,null,null,null,null,null);
        cursor.moveToFirst();
        int max_point_in_one_stage = cursor.getInt(cursor.getColumnIndex("max_point_in_one_stage"));
        int total_point = cursor.getInt(cursor.getColumnIndex("total_point"));
        int num_of_stage =cursor.getInt(cursor.getColumnIndex("num_of_stage"));
        int num_of_win_stage = cursor.getInt(cursor.getColumnIndex("num_of_win_stage"));
        String i1=Integer.toString(max_point_in_one_stage);
        String i2=Integer.toString(total_point);
        String i3=Integer.toString(num_of_stage);
        String i4=Integer.toString(num_of_win_stage);
        String i5=Integer.toString(cursor.getInt(cursor.getColumnIndex("num_of_loose_stage")));
        String i6=Integer.toString(cursor.getInt(cursor.getColumnIndex("num_of_equal_stage")));
        cursor.close();
        TextView text1 = (TextView)findViewById(R.id.textView);
        TextView text2 = (TextView)findViewById(R.id.textView2);
        TextView text3 = (TextView)findViewById(R.id.textView3);
        TextView text4 = (TextView)findViewById(R.id.textView4);
        TextView text5 = (TextView)findViewById(R.id.textView5);
        TextView text6 = (TextView)findViewById(R.id.textView6);
        TextView text7 = (TextView)findViewById(R.id.textView12);
        ImageView achievement = (ImageView)findViewById(R.id.imageView);
        text1.setText("单场次最高分："+i1);
        text2.setText("总进球数："+i2);
        text3.setText("总游玩场次："+i3);
        text4.setText("总胜利场次："+i4);
        text5.setText("总失败场次："+i5);
        text6.setText("总平局场次："+i6);
        LevelListDrawable levelListDrawable = (LevelListDrawable)achievement.getDrawable();
        levelListDrawable.setLevel(total_point);
        if(total_point>=0||total_point<50){
            text7.setText("暂无称号");
        }else if(total_point>49||total_point<101){
            text7.setText("青铜射手");
        }else if(total_point>100||total_point<201){
            text7.setText("白银射手");
        }else {
            text7.setText("黄金射手");
        }


    }

    public void back(View v) {
        this.finish();
    }
    public void deletedata(View v){
        dbHelper = new MydatabaseHelper(this,"GameRecord.db",null,3);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("max_point_in_one_stage",0);
        values.put("total_point",0);
        values.put("num_of_stage",0);
        values.put("num_of_win_stage",0);
        values.put("num_of_loose_stage",0);
        values.put("num_of_equal_stage",0);
        db.update("Record",values,null,null);
        values.clear();
        Toast.makeText(getApplicationContext(),"用户数据删除",Toast.LENGTH_SHORT).show();
        this.finish();
    }

    public void tips(View v){
        Toast.makeText(getApplicationContext(),"50~100青铜射手；100~200白银射手；200以上黄金射手",Toast.LENGTH_SHORT).show();
    }
}

