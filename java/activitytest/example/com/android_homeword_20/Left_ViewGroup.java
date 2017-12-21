package activitytest.example.com.android_homeword_20;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
        view.layout(0,0, MainActivity.windowWidth/5, MainActivity.windowHeight/10);

        view = getChildAt(1);
        view.layout(0, MainActivity.windowHeight/10, MainActivity.windowWidth/5, MainActivity.windowHeight*2/5);

        view = getChildAt(2);
        view.layout(0, MainActivity.windowHeight*2/5, MainActivity.windowWidth/5, MainActivity.windowHeight*3/5);

        view = getChildAt(3);
        view.layout(0, MainActivity.windowHeight*3/5, MainActivity.windowWidth/5, MainActivity.windowHeight*9/10);

        view = getChildAt(4);
        view.layout(0, MainActivity.windowHeight*9/10, MainActivity.windowWidth/5, MainActivity.windowHeight);
    }
}
