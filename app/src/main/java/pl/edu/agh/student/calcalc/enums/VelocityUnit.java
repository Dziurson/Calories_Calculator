package pl.edu.agh.student.calcalc.enums;

import android.app.Activity;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.interfaces.IPropertyWithResource;

public enum VelocityUnit implements IPropertyWithResource {
    VELOCITY_IN_MPS(R.string.meters_per_second),
    VELOCITY_IN_KPH(R.string.kilometers_per_hour);

    private final int stringResourceId;

    VelocityUnit(int stringResourceId) {
        this.stringResourceId = stringResourceId;
    }

    @Override
    public String getString(Activity context) {
        return context.getString(stringResourceId);
    }

}
