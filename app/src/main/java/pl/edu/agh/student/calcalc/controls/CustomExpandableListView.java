package pl.edu.agh.student.calcalc.controls;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakub on 16.11.2017.
 */

public class CustomExpandableListView extends ExpandableListView {
    List<View> touchEnabledViews = new ArrayList<>();

    public CustomExpandableListView(Context context) {
        super(context);
    }

    public CustomExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomExpandableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public <T extends View> void enableTouchForView(T view) {
        touchEnabledViews.add(view);
    }

    public <T extends View> boolean disableTouchForView(T view) {
        if(touchEnabledViews.contains(view)) {
            touchEnabledViews.remove(view);
            return true;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (touchEnabledViews.size() > 0) {
            int x = (int) event.getX();
            int y = (int) event.getY() + getScrollY();
            Rect bounds = new Rect();

            for (View view : touchEnabledViews) {
                view.getHitRect(bounds);
                if (bounds.contains(x, y)) {
                    return false;
                }
            }
        }
        return super.onInterceptTouchEvent(event);
    }
}
