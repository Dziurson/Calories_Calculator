package pl.edu.agh.student.calcalc.enums;

import android.app.Activity;
import android.location.Location;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.globals.UserSettings;
import pl.edu.agh.student.calcalc.helpers.LocationHelper;
import pl.edu.agh.student.calcalc.interfaces.IPropertyWithResource;

/**
 * Created by jakub on 01.01.2018.
 */

public enum ActivityType implements IPropertyWithResource{
    WALKING(R.string.activity_type_walking, 0.1,1.8),
    RUNNING(R.string.activity_type_running, 0.2,0.9);

    private final double velocityCoeff;
    private final double slopeCoeff;
    private final int stringResourceId;

    private ActivityType(int stringResourceId, double velocityCoeff, double slopeCoeff) {
        this.stringResourceId = stringResourceId;
        this.velocityCoeff = velocityCoeff;
        this.slopeCoeff = slopeCoeff;
    }
    public double getCalories(Location from, Location to) {
        if(from != null && to != null) {
            double velocity = LocationHelper.getVelocity(from, to);
            double slope = LocationHelper.getSlope(from, to);
            return (((velocityCoeff * velocity) + (slopeCoeff * velocity * slope) + 3.5) / 12000.0 * (double) UserSettings.userWeight.getValue())*((to.getTime() - from.getTime())/1000.0);
        }
        else return 0;
    }

    @Override
    public String getString(Activity context) {
        return context.getString(stringResourceId);
    }
}
