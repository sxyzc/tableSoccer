package activitytest.example.com.android_homeword_20;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import static activitytest.example.com.android_homeword_20.MainActivity.windowWidth;
import static activitytest.example.com.android_homeword_20.MainActivity.windowHeight;

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
        temp.layout(windowWidth/5,windowHeight/10, windowWidth, windowHeight*9/10);

        //左边的比分View
        temp = getChildAt(1);
        temp.layout(0,0,windowWidth/5,windowHeight);

        //上面的背景View
        temp = getChildAt(2);
        temp.layout(windowWidth/5,0,windowWidth,windowHeight/10);

        //下面的背景View
        temp = getChildAt(3);
        temp.layout(windowWidth/5,windowHeight*9/10,windowWidth,windowHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
