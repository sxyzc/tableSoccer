package activitytest.example.com.android_homeword_20.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import activitytest.example.com.android_homeword_20.R;

public class MyService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //服务创建时调用
    @Override
    public void onCreate() {
        super.onCreate();
    }

    //服务启动时调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int temp = intent.getIntExtra("number",1);
        if(mediaPlayer == null){
            if(temp == 1){
                mediaPlayer = MediaPlayer.create(this, R.raw.background_music1);
            }else if(temp == 2){
                mediaPlayer = MediaPlayer.create(this, R.raw.background_music2);
            }else {
                mediaPlayer = MediaPlayer.create(this, R.raw.background_music3);
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //服务销毁时调用
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
