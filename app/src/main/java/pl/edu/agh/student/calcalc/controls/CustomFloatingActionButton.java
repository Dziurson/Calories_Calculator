package pl.edu.agh.student.calcalc.controls;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

import pl.edu.agh.student.calcalc.helpers.BooleanHelper;
import pl.edu.agh.student.calcalc.helpers.IntegerHelper;

/**
 * Created by jakub on 18.10.2017.
 */

public class CustomFloatingActionButton extends FloatingActionButton {

    private int animationDuration;
    private boolean animationEnabled;

    public CustomFloatingActionButton(Context context) {
        super(context);
    }

    public CustomFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeCustomAttributes(attrs);
    }

    public CustomFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeCustomAttributes(attrs);
    }

    private void initializeCustomAttributes(AttributeSet attrs){
        String controlNamespace = "http://schemas.android.com/apk/res-auto";
        animationEnabled = BooleanHelper.tryParse(attrs.getAttributeValue(controlNamespace,"animation_enabled"),false);
        animationDuration = (animationEnabled) ? IntegerHelper.tryParse(attrs.getAttributeValue(controlNamespace,"animation_duration"),500) : 0;
    }

    @Override
    protected void onVisibilityChanged(@NonNull final View changedView, final int visibility) {
        if(visibility == VISIBLE) {
            this.animate().alpha(1.0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    CustomFloatingActionButton.super.onVisibilityChanged(changedView, visibility);
                }
            });
        }
        else CustomFloatingActionButton.super.onVisibilityChanged(changedView,visibility);
    }

    @Override
    public void setVisibility(final int visibility) {
        if(visibility == INVISIBLE) {
            this.animate().alpha(0.0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    CustomFloatingActionButton.super.setVisibility(visibility);
                }
            });
        }
        else
            super.setVisibility(visibility);
    }
}
