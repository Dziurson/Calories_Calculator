package pl.edu.agh.student.calcalc.utilities;

import android.location.Location;

import pl.edu.agh.student.calcalc.enums.ActivityType;

public class CalorieCalculator {
    private ActivityType currentActivity;
    private Location lastLocation;

    public CalorieCalculator(ActivityType activityType) {
        currentActivity = activityType;
    }

    public double calculateCalories(Location location) {
        double caloriesBurned = currentActivity.getCalories(lastLocation,location);
        lastLocation = location;
        return caloriesBurned;
    }

    public ActivityType getCurrentActivity() {
        return currentActivity;
    }
}
