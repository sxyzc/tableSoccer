package activitytest.example.com.android_homeword_20;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Player {
    float x,y;//位置
    float pHeight =  (MainActivity.windowHeight *4/5) /36;
    float pWidth = (MainActivity.windowWidth * 4/5) / 16;
    float dx;//偏移量

    public Player(float px, float py){  //设置默认值
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
        if(x+dxx>=0&&x+dxx<= MainActivity.windowWidth){
            dx=dxx;
        }
    }
}

