package pl.edu.agh.student.calcalc.listeners;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.ArrayList;

import pl.edu.agh.student.calcalc.globals.Properties;
import pl.edu.agh.student.calcalc.reflection.LocationCommand;

public class ApplicationLocationListener implements LocationListener {

    ArrayList<LocationCommand> onLocationChangedListeners = new ArrayList<>();
    LocationManager locManager;
    static ApplicationLocationListener instance = null;

    private ApplicationLocationListener() {
        locManager = (LocationManager) Properties.mainActivity.getSystemService(Context.LOCATION_SERVICE);
    }

    public static ApplicationLocationListener getInstance() {
        if(instance == null)
            instance = new ApplicationLocationListener();
        return instance;
    }

    @Override
    public void onLocationChanged(Location location) {
        executeLocationEvents(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void executeLocationEvents(Location location) {
        for(LocationCommand command : onLocationChangedListeners){
            command.execute(location);
        }
    }

    public void addOnLocationChangedCommand(LocationCommand command){
        onLocationChangedListeners.add(command);
    }

    public void requestLocationData() {
        try {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        } catch (SecurityException ex) {
            Toast.makeText(Properties.mainActivity, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
