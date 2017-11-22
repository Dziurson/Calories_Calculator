package pl.edu.agh.student.calcalc.controls;

import android.content.Context;
import android.util.AttributeSet;

import pl.edu.agh.student.calcalc.R;

public class CustomSeekBar extends android.support.v7.widget.AppCompatSeekBar {

    public CustomSeekBar(Context context) {
        super(context);
        setDrawables(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawables(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDrawables(context);
    }

    private void setDrawables(Context context) {
        setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_style));
        setThumb(context.getDrawable(R.drawable.custom_progress_bar_selector_style));
    }
}
