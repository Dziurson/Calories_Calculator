package pl.edu.agh.student.calcalc.enums;

import android.location.Location;

import pl.edu.agh.student.calcalc.globals.UserSettings;
import pl.edu.agh.student.calcalc.helpers.LocationHelper;

/**
 * Created by jakub on 01.01.2018.
 */

public enum ActivityType {
    WALKING(0.1,1.8),
    RUNNING(0.2,0.9);

    private final double velocityCoeff;
    private final double slopeCoeff;

    private ActivityType(double velocityCoeff, double slopeCoeff) {
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
}
