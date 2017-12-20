package activitytest.example.com.android_homeword_20;

import android.graphics.Canvas;
import android.graphics.Paint;
import static activitytest.example.com.android_homeword_20.MainActivity.windowWidth;
import static activitytest.example.com.android_homeword_20.MainActivity.windowHeight;

public class Player {
    float x,y;//位置
    int pHeight =  (windowHeight *4/5) /36;
    int pWidth = (windowWidth * 4/5) / 16;
    float dx;//偏移量

    public Player(int px, int py){  //设置默认值
        x = px;
        y = py;
    }

    public float getCenterX(){
        return (x+dx+x+dx+pWidth)/2;
    }

    public float getCenterY(){
        return (y+y+pHeight)/2;
    }


    public void onDraw(Canvas canvas, Paint paint){
        //canvas.drawRect(x,y,pWidth,pHeight,paint);
        canvas.drawRect(x+dx,y,x+dx+pWidth,y+pHeight,paint);
        //canvas.drawCircle(x, y, 20 ,paint);
    }

    public void update(float dxx){  //更新球员位置
        if(x+dxx>=0&&x+dxx<=windowWidth){
            dx=dxx;
        }
    }
}

