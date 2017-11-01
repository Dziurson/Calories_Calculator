package pl.edu.agh.student.calcalc.listeners;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import pl.edu.agh.student.calcalc.enums.VelocityType;
import pl.edu.agh.student.calcalc.globals.Properties;
import pl.edu.agh.student.calcalc.reflection.LocationCommand;
import pl.edu.agh.student.calcalc.reflection.ProviderChangeCommand;

public class ApplicationLocationListener implements LocationListener {

    private Location prevLocation;

    private ArrayList<LocationCommand> onLocationChangedListeners = new ArrayList<>();
    private ArrayList<ProviderChangeCommand> onGPSProviderChangedListeners = new ArrayList<>();
    private LocationManager locManager;
    private static ApplicationLocationListener instance = null;

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
        Bundle extras = location.getExtras();
        extras.putDouble(VelocityType.VELOCITY_IN_MPS.toString(),getVelocity(location));
        extras.putDouble(VelocityType.VELOCITY_IN_KPH.toString(),getVelocity(location)*3.6);
        location.setExtras(extras);
        executeLocationEvents(location);
        prevLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        switch(provider){
            case LocationManager.GPS_PROVIDER:
                executeOnProviderChangedEvents(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
                break;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        switch(provider){
            case LocationManager.GPS_PROVIDER:
                executeOnProviderChangedEvents(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
                break;
        }
    }

    private void executeLocationEvents(Location location) {
        if (onLocationChangedListeners.size() != 0) {
            for (LocationCommand command : onLocationChangedListeners) {
                command.execute(location);
            }
        }
    }

    public void addOnLocationChangedCommand(LocationCommand command){
        onLocationChangedListeners.add(command);
    }

    public void addOnProviderChangedCommand(ProviderChangeCommand command) {
        onGPSProviderChangedListeners.add(command);
    }

    private void executeOnProviderChangedEvents(boolean isProviderEnabled) {
        if (onGPSProviderChangedListeners.size() != 0) {
            for (ProviderChangeCommand command : onGPSProviderChangedListeners) {
                command.execute(isProviderEnabled);
            }
        }
    }

    public void requestLocationData() {
        try {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        } catch (SecurityException ex) {
            Toast.makeText(Properties.mainActivity, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean isGPSEnabled() {
        return locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private double getVelocity(Location location) {
        if(prevLocation == null) {
            prevLocation = location;
            return 0.0;
        }
        else {
            return location.distanceTo(prevLocation)/((location.getTime() - prevLocation.getTime())/1000);
        }
    }
}
