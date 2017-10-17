package pl.edu.agh.student.calcalc.controls;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class MapScrollView extends ScrollView {
    List<View> alvListenedViews = new ArrayList<>();

    public MapScrollView(Context context) {
        super(context);
    }

    public MapScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MapScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public <T extends View> void listenForViewTouchEvent(T view) {
        alvListenedViews.add(view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (alvListenedViews.size() > 0) {
            int x = (int) event.getX();
            int y = (int) event.getY() + getScrollY();
            Rect bounds = new Rect();

            for (View view : alvListenedViews) {
                view.getHitRect(bounds);
                if (bounds.contains(x, y)) {
                    return false;
                }
            }
        }
        return super.onInterceptTouchEvent(event);
    }
}
