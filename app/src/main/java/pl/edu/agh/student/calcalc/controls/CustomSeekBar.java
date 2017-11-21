package pl.edu.agh.student.calcalc.controls;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

import pl.edu.agh.student.calcalc.R;

/**
 * Created by jakub on 21.11.2017.
 */

public class CustomSeekBar extends android.support.v7.widget.AppCompatSeekBar {

    public CustomSeekBar(Context context) {
        super(context);
        setFields(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFields(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFields(context);
    }

    private void setFields(Context context) {
        setProgressDrawable(context.getDrawable(R.drawable.custom_progress_bar_style));
        setThumb(context.getDrawable(R.drawable.custom_progress_bar_selector_style));
    }
}
