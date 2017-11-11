package pl.edu.agh.student.calcalc.controls;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import pl.edu.agh.student.calcalc.R;

public class CustomButton extends android.support.v7.widget.AppCompatButton {

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setButtonSelected(boolean isSelected) {
        if(isSelected) {
            this.setTextColor(Color.WHITE);
            this.setBackgroundResource(R.color.colorLightBlue);
        }
        else {
            this.setTextColor(Color.BLACK);
            this.setBackgroundResource(R.color.colorLightGrey);
        }
    }
}
