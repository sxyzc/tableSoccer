package activitytest.example.com.android_homeword_20;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {
    public float vx,vy;//速度
    public float x,y;//位置
    float r;
    float eps = (float) 1e-6;
    int isCollided = 0;
    int myShooted = 0;
    Paint mPaint = new Paint();

    public Ball(){  //设置默认值
        vx = 20*2; vy = 10*2;
        x = 200; y = 200;
        r = 20;
        r=(MainActivity.windowHeight *4/5) /72;
    }

    void init(){
        vx=(MainActivity.windowHeight *4/5) /48;
        vy=(MainActivity.windowHeight *4/5) /96;
        //r=(windowHeight *4/5) /36;
    }

    public void myOnDraw(Canvas canvas, Paint paint){
        canvas.drawCircle(x, y, r ,paint);
    }
    public void myOnDraw(Canvas canvas){

        //mPaint.setColor(Color.BLUE);
        canvas.drawCircle(x, y, r ,mPaint);
    }

    private  float dis(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((double)((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
    }

    public boolean check(Player p){
        //float x[4];
        float[] px  = new float[4];
        float[] py  = new float[4];
        px[0] = p.x+p.dx; py[0] = p.y;
        px[1] = p.x+p.dx; py[1] = p.y + p.pHeight;
        px[2] = p.x+p.dx + p.pWidth; py[2] = p.y;
        px[3] = p.x+p.dx + p.pWidth; py[3] = p.y + p.pHeight;

        for (int i = 0;i < 4;i++ ) {    //判断是否与矩形4个角碰撞
            //if (Math.abs(dis(x, y, px[i], py[i]) - r) < eps)return true;
            if (dis(x, y, px[i], py[i]) <= r)return true;
        }

        //判断是否与矩形两侧小条碰撞
        if(x >= p.x+p.dx - r && x <= p.x+p.dx && y >= p.y && y <= p.y + p.pHeight)return true;
        if(x >= p.x+p.dx + p.pWidth && x <= p.x+p.dx + p.pWidth + r && y >= p.y && y <= p.y + p.pHeight)return true;

        //判断是否与中间一块大矩形碰撞
        if(x >= p.x+p.dx && x<= p.x+p.dx + p.pWidth && y >= p.y - r && y <= p.y + p.pHeight + r)return true;

        //否则，未碰撞
        return false;
    }

    public void changeV(Player p){
        if(isCollided!=0) return;
        isCollided = 1;
        float cx = p.getCenterX();
        float cy = p.getCenterY();
        float ddx = cx-x;//Math.abs(cx - x);//x的差
        float ddy = cy-y;//Math.abs(cy - y);//y的差
        float ddd = (float)Math.sqrt((double)((cx - x)*(cx - x) + (cy - y)*(cy - y)));//距离
        float vv = (float)Math.sqrt((double)(vx * vx + vy * vy));//速度

        vx = vv * (ddx / ddd);
        vy = vv * (ddy / ddd);

    }

    public void update(int h,int w){//参数为屏幕大小，进行位置更新，包括碰撞反弹
        if (y - r <= 0 || y + r >= h )
            vy *= -1;
        y += vy;
        if (x - r <= 0 || x + r >= w )
            vx *= -1;
        x += vx;

    }
}
