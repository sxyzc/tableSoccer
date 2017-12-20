package activitytest.example.com.android_homeword_20;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import static activitytest.example.com.android_homeword_20.MainActivity.windowWidth;
import static activitytest.example.com.android_homeword_20.MainActivity.windowHeight;


public class Left_ViewGroup extends ViewGroup {
    public Left_ViewGroup(Context context) {
        super(context);
    }

    public Left_ViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View view = (TextView) getChildAt(0);
        view.layout(0,0,windowWidth/5,windowHeight/10);

        view = getChildAt(1);
        view.layout(0,windowHeight/10, windowWidth/5, windowHeight*2/5);

        view = getChildAt(2);
        view.layout(0,windowHeight*2/5, windowWidth/5, windowHeight*3/5);

        view = getChildAt(3);
        view.layout(0,windowHeight*3/5, windowWidth/5, windowHeight*9/10);

        view = getChildAt(4);
        view.layout(0,windowHeight*9/10, windowWidth/5, windowHeight);
    }
}
