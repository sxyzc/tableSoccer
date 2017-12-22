
package activitytest.example.com.android_homeword_20;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.Toast;

public class setting extends Activity {
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
        //初始化设置按钮
        dbHelper = new MydatabaseHelper(this,"GameRecord.db",null,3);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("setting",null,null,null,null,null,null);
        cursor.moveToFirst();
        int diff=cursor.getInt(cursor.getColumnIndex("diffcult"));
        int bgm_st =cursor.getInt(cursor.getColumnIndex("music_bool"));
        int bgm_sl=cursor.getInt(cursor.getColumnIndex("music_select"));
        int se=cursor.getInt(cursor.getColumnIndex("yinxiao"));
        int time=cursor.getInt(cursor.getColumnIndex("time"));
        if(diff==1){diff_l.setChecked(true);}
        if(diff==2){diff_m.setChecked(true);}
        if(diff==3){diff_h.setChecked(true);}
        if(bgm_st==1){bgm_on.setChecked(true);}
        if(bgm_st==0){bgm_off.setChecked(true);}
        if(bgm_sl==1){bgm_1.setChecked(true);}
        if(bgm_sl==2){bgm_2.setChecked(true);}
        if(bgm_sl==3){bgm_3.setChecked(true);}
        if(se==1){se_on.setChecked(true);}
        if(se==0){se_off.setChecked(true);}
        if(time==1){t_30.setChecked(true);}
        if(time==2){t_60.setChecked(true);}
        if(time==3){t_120.setChecked(true);}
        cursor.close();
        //设置一大堆监听器
        diff_l.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

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

                    dbHelper = new MydatabaseHelper(setting.this,"GameRecord.db",null,3);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("music_select",3);
                    db.update("setting",values,null,null);
                    values.clear();
                }

            }
        });
        se_on.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

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
        diff_l.setChecked(true);
        bgm_on.setChecked(true);
        bgm_1.setChecked(true);
        se_on.setChecked(true);
        t_60.setChecked(true);

    }
    public void back_to_main(View a){
        this.finish();
    }
}
