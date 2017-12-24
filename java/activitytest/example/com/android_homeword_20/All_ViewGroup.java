package activitytest.example.com.android_homeword_20;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class All_ViewGroup extends ViewGroup {

    public All_ViewGroup(Context context){
        super(context);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs){
        return new MarginLayoutParams(getContext(), attrs);
    }

    //用于控制子View的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //设置gameView的位置
        View temp = getChildAt(0);
        temp.layout(MainActivity.windowWidth/10, MainActivity.windowHeight*3/20, MainActivity.windowWidth*9/10, MainActivity.windowHeight*19/20);

        //时间
        temp = getChildAt(1);
        temp.layout(MainActivity.windowWidth*3/10,0, MainActivity.windowWidth*7/10, MainActivity.windowHeight*3/40);

        temp = getChildAt(2);
        temp.layout(MainActivity.windowWidth/20,0, MainActivity.windowWidth*3/20, MainActivity.windowHeight*3/40);

        temp = getChildAt(3);
        temp.layout(MainActivity.windowWidth/5,0, MainActivity.windowWidth*3/10, MainActivity.windowHeight*3/40);

        temp = getChildAt(4);
        temp.layout(MainActivity.windowWidth*4/5,0, MainActivity.windowWidth*9/10, MainActivity.windowHeight*3/40);

        temp = getChildAt(5);
        temp.layout(MainActivity.windowWidth*11/20,MainActivity.windowHeight*3/40, MainActivity.windowWidth*6/10, MainActivity.windowHeight*3/20);

        temp = getChildAt(6);
        temp.layout(MainActivity.windowWidth*4/10,MainActivity.windowHeight*3/40, MainActivity.windowWidth*9/20, MainActivity.windowHeight*3/20);

        temp = getChildAt(7);
        temp.layout(0,MainActivity.windowHeight*3/40, MainActivity.windowWidth*4/10, MainActivity.windowHeight*3/20);

        temp = getChildAt(8);
        temp.layout(MainActivity.windowWidth*6/10,MainActivity.windowHeight*3/40, MainActivity.windowWidth, MainActivity.windowHeight*3/20);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
