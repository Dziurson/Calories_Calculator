package pl.edu.agh.student.calcalc.controls;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by jakub on 21.11.2017.
 */

public class CustomSupportMapFragment extends SupportMapFragment {

    private Activity context;
    private DisplayMetrics metrics;

    public void setContext(Activity context) {
        this.context = context;
        metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    public void reloadDimensions(boolean isVisible) {
        final View mapView = getView();
        ViewGroup.LayoutParams mapParams = mapView.getLayoutParams();
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                if (mapParams != null) {
                    mapParams.height = isVisible ? (int) Math.ceil(150 * metrics.density) : 0;
                    mapView.setLayoutParams(mapParams);
                    mapView.animate();
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                if (mapParams != null) {
                    mapParams.height = isVisible ? (int) Math.ceil(300 * metrics.density) : 0;
                    mapView.setLayoutParams(mapParams);
                    mapView.animate();
                }
                break;
        }
    }
}
