
package activitytest.example.com.android_homeword_20;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.Toast;

import activitytest.example.com.android_homeword_20.R;

public class setting extends AppCompatActivity {
    private MydatabaseHelper dbHelper;
    private RadioButton diff_l,diff_m,diff_h;
    private RadioButton bgm_on,bgm_off;
    private RadioButton bgm_1,bgm_2,bgm_3;
    private RadioButton se_on,se_off;
    private RadioButton t_30,t_60,t_120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //初始化难度等参数
        diff_l=(RadioButton)findViewById(R.id.radioButton10);
        diff_m=(RadioButton)findViewById(R.id.radioButton11);
        diff_h=(RadioButton)findViewById(R.id.radioButton12);
        bgm_on=(RadioButton)findViewById(R.id.radioButton13);
        bgm_off=(RadioButton)findViewById(R.id.radioButton14);
        bgm_1=(RadioButton)findViewById(R.id.radioButton15);
        bgm_2=(RadioButton)findViewById(R.id.radioButton16);
        bgm_3=(RadioButton)findViewById(R.id.radioButton17);
        se_on=(RadioButton)findViewById(R.id.radioButton18);
        se_off=(RadioButton)findViewById(R.id.radioButton19);
        t_30=(RadioButton)findViewById(R.id.radioButton20);
        t_60=(RadioButton)findViewById(R.id.radioButton21);
        t_120=(RadioButton)findViewById(R.id.radioButton22);
        //设置一大堆监听器
        diff_l.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "低难度",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("diffcult",1);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        diff_m.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "中难度",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("diffcult",2);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        diff_h.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "高难度",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("diffcult",3);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        bgm_on.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "打开背景音乐",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("music_bool",1);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        bgm_off.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "关闭背景音乐",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("music_bool",0);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        bgm_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "第一首",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("music_select",1);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        bgm_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "第二首",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("music_select",2);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        bgm_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "第三首",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("music_select",1);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        se_on.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "打开音效",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("yinxiao",1);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        se_off.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "关闭音效",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("yinxiao",0);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        t_30.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "30秒游戏时间",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("time",1);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        t_60.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "1分钟游戏时间",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("time",2);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        t_120.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(setting.this, "2分钟游戏时间",Toast.LENGTH_SHORT).show();
                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("time",3);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });

    }
    public void default_setting(View a){
        Toast.makeText(setting.this, "回复默认设置",Toast.LENGTH_SHORT).show();
        dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("diffcult",1);
        values.put("music_bool",1);
        values.put("music_select",1);
        values.put("yinxiao",1);
        values.put("time",2);
        db.update("setting",values,null,null);
        values.clear();
        Intent intent = new Intent(setting.this, MainActivity.class);
        startActivity(intent);
    }
    public void back_to_main(View a){
        Intent intent = new Intent(setting.this, MainActivity.class);
        startActivity(intent);
    }
}
