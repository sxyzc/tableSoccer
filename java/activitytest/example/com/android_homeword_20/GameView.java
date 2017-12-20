package activitytest.example.com.android_homeword_20;

import android.util.DisplayMetrics;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import activitytest.example.com.android_homeword_20.bluetooth.BluetoothMsg;

// 自定义视图类
public class GameView extends View implements Runnable {

        //自定义监听
        interface MyListener{
                void notifyDataChage(int a);
        }
        private MyListener Listener;
        private MyListener myListener;
        public void setListener(MyListener listener){
                Listener = listener;
        }
        public void setMyListener(MyListener listener){
                myListener = listener;
        }

        //蓝牙传输数据用
        public static float mPlayerDx;
        public static float PlayerDx;


        //定义该视图的宽高
        private int windowHeight;
        private int windowWidth;

        private int halfPlayerHeight;
        private int halfPlayerWidth;

        //score为敌方得分，mScore为我方得分
        private int score=0,mScore=0;
        private int scored_time=0;
        private int scored_who=1;//0 for us, 1 for other
        public int getScore(){
                return score;
        }
        public int getmScore(){
                return mScore;
        }

        private RefreshHandler mRedrawHandler = null;
        public static Ball ball=new Ball();
        List<Player> mPlayerList;
        List<Player> PlayerList;

        private int getNextP(int i){
                if(i == 9)return (int)(Math.random()*2)==0?7:8;
                if(i == 7)return (int)(Math.random()*2)==0?3:4;
                if(i == 8)return (int)(Math.random()*2)==0?5:6;
                if(i == 3)return 0;
                if(i == 4)return (int)(Math.random()*2)==0?0:1;
                if(i == 5)return (int)(Math.random()*2)==0?1:2;
                if(i == 6)return 2;
                if(i>=10)return -2;
                return -1;
        }
        private void init(){
                ball.x = mPlayerList.get(4).getCenterX();
                ball.y = windowHeight/2;
                ball.init();
                if(scored_who==0)ball.vy*=-1;
                for (int i =0; i<mPlayerList.size();i++){
                        mPlayerList.get(i).dx=0;
                        PlayerList.get(i).dx=0;
                }
        }

        //计算两点距离
        private float getDis(float x1,float y1,float x2,float y2){
                return (float)Math.sqrt((double)((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)));
        }

        //找到离球最近的人     这个要改，改为球前最近的人
        private int findNearP(){
                if(ball.myShooted==1){
                        float mind = 999999;int mini = 0;
                        for(int i=0;i<PlayerList.size();i++){
                                Player player = PlayerList.get(i);
                                float dis = getDis(player.getCenterX(),player.getCenterY(),ball.x,ball.y);
                                if(dis<mind){mind=dis;mini=i;}
                        }
                        if(ball.y<windowHeight/12)return -1;
                        if(ball.y<windowHeight/5)return 9;
                        if(ball.y<windowHeight*13/30){
                                for(int i=7;i<9;i++){
                                        Player player = PlayerList.get(i);
                                        float dis = getDis(player.getCenterX(),player.getCenterY(),ball.x,ball.y);
                                        if(dis<mind){mind=dis;mini=i;}
                                }
                                return mini;
                        }
                        if(ball.y<windowHeight*2/3){
                                for(int i=3;i<7;i++){
                                        Player player = PlayerList.get(i);
                                        float dis = getDis(player.getCenterX(),player.getCenterY(),ball.x,ball.y);
                                        if(dis<mind){mind=dis;mini=i;}
                                }
                                return mini;
                        }
                        for(int i=0;i<3;i++){
                                Player player = PlayerList.get(i);
                                float dis = getDis(player.getCenterX(),player.getCenterY(),ball.x,ball.y);
                                if(dis<mind){mind=dis;mini=i;}
                        }
                        return mini;
                }

                float mind = 999999;int mini = 0;
                for(int i=0;i<PlayerList.size();i++){
                        Player player = PlayerList.get(i);
                        float dis = getDis(player.getCenterX(),player.getCenterY(),ball.x,ball.y);
                        if(dis<mind){mind=dis;mini=i;}
                }
                if(ball.y<windowHeight/12)return 9;
                if(ball.y<windowHeight/5){
                        for(int i=7;i<9;i++){
                                Player player = PlayerList.get(i);
                                float dis = getDis(player.getCenterX(),player.getCenterY(),ball.x,ball.y);
                                if(dis<mind){mind=dis;mini=i;}
                        }
                        return mini;
                }
                if(ball.y<windowHeight*13/30){
                        for(int i=3;i<7;i++){
                                Player player = PlayerList.get(i);
                                float dis = getDis(player.getCenterX(),player.getCenterY(),ball.x,ball.y);
                                if(dis<mind){mind=dis;mini=i;}
                        }
                        return mini;
                }
                //if(ball.y<windowHeight*2/3){
                for(int i=0;i<3;i++){
                        Player player = PlayerList.get(i);
                        float dis = getDis(player.getCenterX(),player.getCenterY(),ball.x,ball.y);
                        if(dis<mind){mind=dis;mini=i;}
                }
                return mini;
                //}
                //return -1;
        }

        private float logicDx=0;

        //玄学ai，各种速度调整都会影响到ai性能，很容易进入ai死区
        private void aiLogic(){
                int pos =findNearP();

                if(pos==-1)return;
                Player nearP = PlayerList.get(pos);
                int dir = 1;//1为右，-1为左

                logicDx = (int)PlayerList.get(9).dx;
                //if(pos>2&&pos<7)logicDx=(logicDx*20/13);
                //if(pos>6&&pos<9)logicDx=(logicDx*3/4);


                float xdis=Math.abs(nearP.x+nearP.dx-ball.x);
                float ydis=Math.abs(nearP.y-ball.y);

                float t=Math.abs(ydis/ball.vy);
                float balldestinedX = ball.x+t*ball.vx;
                float diss =nearP.x+nearP.dx-balldestinedX;
                if(Math.abs((double)diss)<3)dir=0;
                else if(diss<-ball.vx*2)dir=1;
                else dir=-1;
                if(logicDx>60)dir=-1;


                 /*   法一
                  float dis=nearP.x+nearP.dx-ball.x;
                 if(Math.abs((double)diss)<1e-3)dir=0;
                 else if(diss>0)dir=-1;
                 else dir=1;*/
                 /*
                  * 法二
                  * if(logicDx>0){
                         if(nearP.x+nearP.dx>ball.x-100)dir=-1;}
                 else if(nearP.x+nearP.dx>ball.x)dir=-1;*/
                    //Log.d("aitest", "ai: @@@ "+pos+" "+ball.x+","+ball.y+"  "+(nearP.x+nearP.dx));
                //Log.d("aitest", "ai: @@@ "+pos+" "+logicDx+","+5*dir);

                logicDx += 5*dir;
                float dx = logicDx;

                //相减
                if ((PlayerList.get(0).x + dx > 0) && (PlayerList.get(2).x + dx < MaxRight)) {
                        PlayerList.get(0).update(dx);
                        PlayerList.get(1).update(dx);
                        PlayerList.get(2).update(dx);
                }
                if ((PlayerList.get(3).x + dx*15/27 > 0)&&(PlayerList.get(6).x + dx*15/27 < MaxRight)){
                        PlayerList.get(3).update(dx*15/27);
                        PlayerList.get(4).update(dx*15/27);
                        PlayerList.get(5).update(dx*15/27);
                        PlayerList.get(6).update(dx*15/27);
                }
                if ((PlayerList.get(7).x + dx*43/27 > 0)&&(PlayerList.get(8).x + dx*43/27 < MaxRight)){
                        PlayerList.get(7).update(dx*43/27);
                        PlayerList.get(8).update(dx*43/27);
                }
                if((PlayerList.get(9).x + dx > windowWidth * 3/10)&&(PlayerList.get(9).x + dx < windowWidth * 51/80)){
                        PlayerList.get(9).update(dx);
                }
        }

        // 构造方法
        public GameView(Context context) {
                super(context);

                //获得手机屏幕的高宽，初始化游戏界面的宽高
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                windowHeight = displayMetrics.heightPixels * 4 / 5;
                windowWidth = displayMetrics.widthPixels * 4 / 5;

                halfPlayerHeight = windowHeight/36/2;
                halfPlayerWidth = windowWidth/16/2;

                mScore = 0;
                score = 0;
            //mPaint.setColor(Color.BLACK);
                //定义己方球员和对方球员
                mPlayerList = new ArrayList<>();
                PlayerList = new ArrayList<>();
                Player t1,t2,t3,t4,t5;
               // Log.d("bbb", "MyView: @@@ "+getHeight()+" "+getWidth());
                //Log.d("bbb", "MyView: ### "+windowWidth+" "+windowHeight);

                //这里开始设置球员的布局
                //从上往下数第一行,三名进攻球员的位置
                t1 = new Player(windowWidth/5 - halfPlayerWidth, windowHeight/3 - halfPlayerHeight);
                t2 = new Player(windowWidth/2 - halfPlayerWidth, windowHeight/3 - halfPlayerHeight);
                t3 = new Player(windowWidth*4/5 - halfPlayerWidth, windowHeight/3 - halfPlayerHeight);
                mPlayerList.add(t1);
                mPlayerList.add(t2);
                mPlayerList.add(t3);

                //从上往下数第二行，四名中场防守球员
                t1 = new Player(windowWidth*1/8 - halfPlayerWidth, windowHeight*9/16 - halfPlayerHeight);
                t2 = new Player(windowWidth*3/8 - halfPlayerWidth, windowHeight*9/16 - halfPlayerHeight);
                t3 = new Player(windowWidth*5/8 - halfPlayerWidth, windowHeight*9/16 - halfPlayerHeight);
                t4 = new Player(windowWidth*7/8 - halfPlayerWidth, windowHeight*9/16 - halfPlayerHeight);
                mPlayerList.add(t1);
                mPlayerList.add(t2);
                mPlayerList.add(t3);
                mPlayerList.add(t4);

                //从上往下数第三行，两名防守球员
                t1 = new Player(windowWidth*3/10 - halfPlayerWidth, windowHeight*13/16 - halfPlayerHeight);
                t2 = new Player(windowWidth*7/10 - halfPlayerWidth, windowHeight*13/16 - halfPlayerHeight);
                mPlayerList.add(t1);
                mPlayerList.add(t2);

                //从上往下数第四行，一名守门员
                t1 = new Player(windowWidth/2 - halfPlayerWidth, windowHeight*15/16 - halfPlayerHeight);
                mPlayerList.add(t1);

                //球门对象
                t1 = new Player(windowWidth/2 - 5*halfPlayerWidth, windowHeight - halfPlayerHeight);
                t2 = new Player(windowWidth/2 - 3*halfPlayerWidth, windowHeight - halfPlayerHeight);
                t3 = new Player(windowWidth/2 - halfPlayerWidth, windowHeight - halfPlayerHeight);
                t4 = new Player(windowWidth/2 + halfPlayerWidth, windowHeight - halfPlayerHeight);
                t5 = new Player(windowWidth/2 + 3*halfPlayerWidth, windowHeight - halfPlayerHeight);
                mPlayerList.add(t1);
                mPlayerList.add(t2);
                mPlayerList.add(t3);
                mPlayerList.add(t4);
                mPlayerList.add(t5);

                //初始化对面的球员，方法同初始化自己的球员
                t1 = new Player(windowWidth/5 - halfPlayerWidth, windowHeight*2/3 - halfPlayerHeight);
                t2 = new Player(windowWidth/2 - halfPlayerWidth, windowHeight*2/3 - halfPlayerHeight);
                t3 = new Player(windowWidth*4/5 - halfPlayerWidth, windowHeight*2/3 - halfPlayerHeight);
                PlayerList.add(t1);
                PlayerList.add(t2);
                PlayerList.add(t3);
                t1 = new Player(windowWidth/8 - halfPlayerWidth, windowHeight*7/16 - halfPlayerHeight);
                t2 = new Player(windowWidth*3/8 - halfPlayerWidth, windowHeight*7/16 - halfPlayerHeight);
                t3 = new Player(windowWidth*5/8 - halfPlayerWidth, windowHeight*7/16 - halfPlayerHeight);
                t4 = new Player(windowWidth*7/8 - halfPlayerWidth, windowHeight*7/16 - halfPlayerHeight);
                PlayerList.add(t1);
                PlayerList.add(t2);
                PlayerList.add(t3);
                PlayerList.add(t4);
                t1 = new Player(windowWidth*3/10 - halfPlayerWidth, windowHeight*3/16 - halfPlayerHeight);
                t2 = new Player(windowWidth*7/10 - halfPlayerWidth, windowHeight*3/16 - halfPlayerHeight);
                PlayerList.add(t1);
                PlayerList.add(t2);
                t1 = new Player(windowWidth/2 - halfPlayerWidth, windowHeight*1/16 - halfPlayerHeight);
                PlayerList.add(t1);

                //初始化对面的球门对象
                t1 = new Player(windowWidth/2 - 5*halfPlayerWidth, -halfPlayerHeight);
                t2 = new Player(windowWidth/2 - 3*halfPlayerWidth, -halfPlayerHeight);
                t3 = new Player(windowWidth/2 - halfPlayerWidth, -halfPlayerHeight);
                t4 = new Player(windowWidth/2 + halfPlayerWidth, -halfPlayerHeight);
                t5 = new Player(windowWidth/2 + 3*halfPlayerWidth, -halfPlayerHeight);
                PlayerList.add(t1);
                PlayerList.add(t2);
                PlayerList.add(t3);
                PlayerList.add(t4);
                PlayerList.add(t5);

                //球场初始化
                init();

                // 获得焦点
                setFocusable(true);
                mRedrawHandler = new RefreshHandler();//放在主线程里面
                //       Player [] t =new Player[8];

                // 启动线程
                new Thread(this).start();
        }

        @Override
        public void run() {

                while (!Thread.currentThread().isInterrupted()) {
                        // 通过发送消息更新界面
                        Message m = new Message();
                        m.what = 0x101;
                        mRedrawHandler.sendMessage(m);
                        try {
                                Thread.sleep(100);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
        }

        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                // 实例化画笔
                Paint p = new Paint();
                //设置足球场背景
                p.setColor(Color.GREEN);
                p.setAlpha(150);
                canvas.drawRect(0,0,windowWidth/9,windowHeight,p);
                canvas.drawRect(windowWidth*2/9,0,windowWidth*3/9,windowHeight,p);
                canvas.drawRect(windowWidth*4/9,0,windowWidth*5/9,windowHeight,p);
                canvas.drawRect(windowWidth*6/9,0,windowWidth*7/9,windowHeight,p);
                canvas.drawRect(windowWidth*8/9,0,windowWidth,windowHeight,p);
                p.setAlpha(80);
                canvas.drawRect(windowWidth/9,0,windowWidth*2/9,windowHeight,p);
                canvas.drawRect(windowWidth*3/9,0,windowWidth*4/9,windowHeight,p);
                canvas.drawRect(windowWidth*5/9,0,windowWidth*6/9,windowHeight,p);
                canvas.drawRect(windowWidth*7/9,0,windowWidth*8/9,windowHeight,p);

                //画双方小禁区
                p.setAlpha(255);
                p.setColor(Color.WHITE);
                p.setStrokeWidth(10);
                canvas.drawLine(windowWidth*3/10,0,windowWidth*3/10,windowHeight*5/32,p);
                canvas.drawLine(windowWidth*7/10,0,windowWidth*7/10,windowHeight*5/32,p);
                canvas.drawLine(windowWidth*3/10,windowHeight*5/32,windowWidth*7/10,windowHeight*5/32,p);
                canvas.drawLine(windowWidth*3/10,windowHeight*27/32,windowWidth*3/10,windowHeight,p);
                canvas.drawLine(windowWidth*7/10,windowHeight*27/32,windowWidth*7/10,windowHeight,p);
                canvas.drawLine(windowWidth*3/10,windowHeight*27/32,windowWidth*7/10,windowHeight*27/32,p);

                //画中场线和中场圆
                canvas.drawLine(0,windowHeight/2,windowWidth,windowHeight/2,p);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(windowWidth/2,windowHeight/2,windowWidth/5,p);

                Paint ballp =new Paint();

                p.setStyle(Paint.Style.FILL);
                boolean colliged=false;
                p.setColor(Color.BLACK);
                //绘制球员
                for(int i = 0;i<mPlayerList.size();i++){
                        Player player = mPlayerList.get(i);
                        player.onDraw(canvas, p);
                        if(scored_time!=0)continue;
                        if(ball.check(player)){
                            colliged=true;
                            if(ball.isCollided!=0)continue;
                            int to =getNextP(i);
                            if(to==-1){
                                int topos =(int)(Math.random()*5)+10;
                                ball.changeV(PlayerList.get(topos));continue;
                            }else if (to==-2) {
                                Log.d("gameTest", "onDraw: 敌方进球了!");score++;
                                scored_who=1;
                                    scored_time=1;init();
                                myListener.notifyDataChage(score);
                                ball.isCollided=1;continue;
                            }
                                     /*int to=(int)(Math.random()*mPlayerList.size());
                                     while(mPlayerList.get(to)==player){
                                         to=(int)(Math.random()*mPlayerList.size());
                                     }*/
                            ball.changeV(mPlayerList.get(to));
                                        //ball.changeV(mPlayerList.get());
                    }
                }

                 /*for (int i=0;i<mPlayerList.size();i++){
                     mPlayerList.get(i).onDraw(canvas, p);
                     if(ball.check(mPlayerList.get(i))){
                         colliged=true;
                         int to=1-i;
                         if(ball.isCollided!=0)continue;
                         ball.changeV(mPlayerList.get(to));
                     }
                 }*/


                        p.setColor(Color.BLUE);
                 /*for (Player player :
                         PlayerList) {
                     player.onDraw(canvas, p);
                 }*/

                        for(int i = 0;i<PlayerList.size();i++){
                                Player player = PlayerList.get(i);
                                player.onDraw(canvas, p);
                                if(scored_time!=0)continue;
                                if(ball.check(player)){
                                        colliged=true;
                                        if(ball.isCollided!=0)continue;
                                        int to =getNextP(i);
                                        if(to==-1){
                                                int topos =(int)(Math.random()*5)+10;
                                                ball.changeV(mPlayerList.get(topos));continue;
                                        }else if (to==-2) {
                                            Log.d("gameTest", "onDraw: 我方进球了!");mScore++;
                                            scored_who = 0;
                                            scored_time=1;init();
                                            Listener.notifyDataChage(mScore);
                                            ball.isCollided=1;
                                             continue;
                                        }
                             /*int to=(int)(Math.random()*mPlayerList.size());
                             while(mPlayerList.get(to)==player){
                                 to=(int)(Math.random()*mPlayerList.size());
                             }*/
                                        ball.changeV(PlayerList.get(to));
                                        //ball.changeV(mPlayerList.get());
                                }
                        }

            if(colliged)ballp.setColor(Color.RED);
            else ballp.setColor(Color.YELLOW);

            // 画球
                        ball.myOnDraw(canvas, ballp);
        }

        //更新界面处理器
        class RefreshHandler extends Handler {
                @Override
                public void handleMessage(Message msg) {
                        if (msg.what == 0x101) {
                                if(GameView.this.ball.isCollided!=0) GameView.this.ball.isCollided++;//碰撞后的时间计数
                                if(GameView.this.ball.isCollided>5) GameView.this.ball.isCollided=0;//碰撞超过一定时间回到初始状态
                                if(scored_time==0){
                                        if(BluetoothMsg.serverOrCilent == BluetoothMsg.ServerOrCilent.SERVICE) {
                                                GameView.this.ball.update(windowHeight, windowWidth);
                                                aiLogic();
                                        }
                                        if(BluetoothMsg.isOpen){
                                                update_playerDx(PlayerList,PlayerDx);
                                        }
                                }else{
                                        scored_time++;
                                        if(scored_time==20){
                                                scored_time=0;
                                        }
                                }
                                GameView.this.invalidate();
                        }
                        super.handleMessage(msg);
                }
        };

        //控制移动
        private float lastX;
        private int MaxRight;
        private float eventX;
       // private int eventY;
        private float dx = 0;

        public void update_playerDx(List<Player> player_list,float dx){
                //相减
                if ((player_list.get(0).x + dx > 0) && (player_list.get(2).x + dx < MaxRight)) {
                        player_list.get(0).update(dx);
                        player_list.get(1).update(dx);
                        player_list.get(2).update(dx);
                }
                if ((player_list.get(3).x + dx*15/27 > 0)&&(player_list.get(6).x + dx*15/27 < MaxRight)){
                        player_list.get(3).update(dx*15/27);
                        player_list.get(4).update(dx*15/27);
                        player_list.get(5).update(dx*15/27);
                        player_list.get(6).update(dx*15/27);
                }
                //从上往下数第3排的限制条件
                if ((player_list.get(7).x + dx*43/27 > 0)&&(player_list.get(8).x + dx*43/27 < MaxRight)){
                        player_list.get(7).update(dx*43/27);
                        player_list.get(8).update(dx*43/27);
                }
                if((player_list.get(9).x + dx > windowWidth * 3/10)&&(player_list.get(9).x + dx < windowWidth * 51/80)) {
                        player_list.get(9).update(dx);
                }
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {

                eventX = event.getRawX();
                //eventY = (int)event.getRawY();

                switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN: {
                                lastX = eventX - dx;
                                //lastX = eventX;
                                //球员能到的最右边
                                MaxRight = windowWidth - halfPlayerWidth * 2;
                                //看看这里的右侧数值有没有问题
                               // Log.d("aaa", "onTouch: "+"  "+eventX+"  "+lastX);
                                break;
                        }
                        case MotionEvent.ACTION_MOVE:{
                                dx = (eventX - lastX)* 200/windowWidth ;
                                //Log.d("aaa", "onTouch: "+dx+"  "+eventX+"  "+lastX);
                                //float First_left = mPlayerList.get(0).dx + dx;

                                mPlayerDx = dx;
                                update_playerDx(mPlayerList,dx);

                                break;
                        }
                        default:break;
                }
                return true;
        }

}//线程结束