package pl.edu.agh.student.calcalc.controls;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class ScrollViewTouchEnabled extends ScrollView {
    List<View> alvTouchEnabledViews = new ArrayList<>();

    public ScrollViewTouchEnabled(Context context) {
        super(context);
    }

    public ScrollViewTouchEnabled(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewTouchEnabled(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollViewTouchEnabled(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public <T extends View> void enableTouchForView(T view) {
        alvTouchEnabledViews.add(view);
    }
    public <T extends View> boolean disableTouchForView(T view) {
        if(alvTouchEnabledViews.contains(view)) {
            alvTouchEnabledViews.remove(view);
            return true;
        }
        return false;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (alvTouchEnabledViews.size() > 0) {
            int x = (int) event.getX();
            int y = (int) event.getY() + getScrollY();
            Rect bounds = new Rect();

            for (View view : alvTouchEnabledViews) {
                view.getHitRect(bounds);
                if (bounds.contains(x, y)) {
                    return false;
                }
            }
        }
        return super.onInterceptTouchEvent(event);
    }
}
