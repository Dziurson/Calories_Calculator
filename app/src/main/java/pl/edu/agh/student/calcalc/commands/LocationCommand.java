package pl.edu.agh.student.calcalc.commands;

import android.location.Location;

public interface LocationCommand {
    void onLocationChanged(Location location);
}
