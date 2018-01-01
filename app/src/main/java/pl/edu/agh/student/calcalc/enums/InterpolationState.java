package pl.edu.agh.student.calcalc.enums;

import android.app.Activity;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.interfaces.IPropertyWithResource;

/**
 * Created by jakub on 01.01.2018.
 */

public enum InterpolationState implements IPropertyWithResource {
    ENABLED(R.string.enabled),
    DISABLED(R.string.disabled);

    private final int stringResourceId;

    InterpolationState(int stringResourceId) {
        this.stringResourceId = stringResourceId;
    }

    @Override
    public String getString(Activity context) {
        return context.getString(stringResourceId);
    }
}
