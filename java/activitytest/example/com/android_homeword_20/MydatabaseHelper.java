package activitytest.example.com.android_homeword_20;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class MydatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_RECORDS = "create table Record("
            + "max_point_in_one_stage integer," //单场最高得分
            + "total_point integer,"//总进球数
            + "num_of_stage integer,"//总场次
            + "num_of_win_stage integer,"//总胜利场次
            + "num_of_loose_stage integer,"//总失败场次
            + "num_of_equal_stage integer)";//总平局场次

    public  static final String CREATE_SETTING = "create table setting("
            + "diffcult integer,"//难度选择
            + "music_bool integer,"//音乐打开
            + "music_select integer,"//音乐选择
            + "yinxiao integer,"//音效打开
            + "time integer)";//比赛时间选择
    private Context mContext;

    public MydatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_RECORDS);
        db.execSQL(CREATE_SETTING);
        Toast.makeText(mContext,"用户数据创建成功",Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists Record");
        db.execSQL("drop table if exists setting");
        onCreate(db);
    }
}
