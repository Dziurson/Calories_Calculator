package pl.edu.agh.student.calcalc.controls;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jakub on 18.10.2017.
 */

public class AnimatedFloatingActionButton extends FloatingActionButton {
    public AnimatedFloatingActionButton(Context context) {
        super(context);
    }

    public AnimatedFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onVisibilityChanged(@NonNull final View changedView, final int visibility) {
        if(visibility == VISIBLE) {
            this.animate().alpha(1.0f).setDuration(500).setListener(new AnimatorListenerAdapter(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    AnimatedFloatingActionButton.super.onVisibilityChanged(changedView, visibility);
                }
            });
        }
        else AnimatedFloatingActionButton.super.onVisibilityChanged(changedView,visibility);
    }

    @Override
    public void setVisibility(final int visibility) {
        if(visibility == INVISIBLE) {
            this.animate().alpha(0.0f).setDuration(500).setListener(new AnimatorListenerAdapter(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    AnimatedFloatingActionButton.super.setVisibility(visibility);
                }
            });
        }
        else
            super.setVisibility(visibility);
    }

    public void setVisibility(final int visibility, boolean useBaseFunction){
        if(useBaseFunction)
            super.setVisibility(visibility);
        else
            setVisibility(visibility);
    }
}
