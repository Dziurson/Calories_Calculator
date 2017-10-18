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

public class AnimatedFloatingActionButton extends FloatingActionButton {

    private int mAnimationDuration;
    private boolean mAnimationEnabled;

    public AnimatedFloatingActionButton(Context context) {
        super(context);
    }

    public AnimatedFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeCustomAttributes(attrs);
    }

    public AnimatedFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeCustomAttributes(attrs);
    }

    private void initializeCustomAttributes(AttributeSet attrs){
        String sNamespace = "http://schemas.android.com/apk/res-auto";
        mAnimationEnabled = BooleanHelper.tryParse(attrs.getAttributeValue(sNamespace,"animation_enabled"),false);
        mAnimationDuration = (mAnimationEnabled) ? IntegerHelper.tryParse(attrs.getAttributeValue(sNamespace,"animation_duration"),500) : 0;
    }

    @Override
    protected void onVisibilityChanged(@NonNull final View changedView, final int visibility) {
        if(visibility == VISIBLE) {
            this.animate().alpha(1.0f).setDuration(mAnimationDuration).setListener(new AnimatorListenerAdapter(){
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
            this.animate().alpha(0.0f).setDuration(mAnimationDuration).setListener(new AnimatorListenerAdapter(){
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
}
