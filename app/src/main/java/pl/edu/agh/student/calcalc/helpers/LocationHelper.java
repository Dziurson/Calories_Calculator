package pl.edu.agh.student.calcalc.helpers;

import android.location.Location;

import pl.edu.agh.student.calcalc.containers.Tuple;

public class LocationHelper {
    public static Tuple<String,String> format(Location location) {
        int latSeconds = (int)(location.getLatitude() * 3600);
        int lonSeconds = (int)(location.getLongitude() * 3600);
        int lonDegrees = lonSeconds/3600;
        int latDegrees = latSeconds/3600;
        latSeconds = latSeconds % 3600;
        lonSeconds = lonSeconds % 3600;
        int latMinutes = latSeconds/60;
        int lonMinutes = lonSeconds/60;
        latSeconds = latSeconds % 60;
        lonSeconds = lonSeconds % 60;
        String latFormatted = Math.abs(latDegrees) + "° " + Math.abs(latMinutes) + "' " + Math.abs(latSeconds) + "\" " + ((latSeconds >= 0) ? "N" : "S");
        String lonFormatted = Math.abs(lonDegrees)+ "° " + Math.abs(lonMinutes) + "' " + Math.abs(lonSeconds) + "\" " + ((lonSeconds >= 0) ? "E" : "W");
        return new Tuple<>(latFormatted,lonFormatted);
    }
}
