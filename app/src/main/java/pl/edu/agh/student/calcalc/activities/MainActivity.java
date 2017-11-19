package pl.edu.agh.student.calcalc.activities;

import android.Manifest;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Locale;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.controls.CustomScrollView;
import pl.edu.agh.student.calcalc.helpers.LocationHelper;
import pl.edu.agh.student.calcalc.types.Tuple;
import pl.edu.agh.student.calcalc.controls.CustomFloatingActionButton;
import pl.edu.agh.student.calcalc.enums.LocationServiceState;
import pl.edu.agh.student.calcalc.globals.Properties;
import pl.edu.agh.student.calcalc.globals.UserSettings;
import pl.edu.agh.student.calcalc.helpers.ActivityHelper;
import pl.edu.agh.student.calcalc.helpers.FileHelper;
import pl.edu.agh.student.calcalc.listeners.ApplicationLocationListener;
import pl.edu.agh.student.calcalc.commands.OnLocationChangeCommand;
import pl.edu.agh.student.calcalc.commands.OnProviderChangeCommand;
import pl.edu.agh.student.calcalc.utilities.FileSerializer;
import pl.edu.agh.student.calcalc.utilities.Timer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback, OnLocationChangeCommand {

    private TextView gpsStateTextView;
    private Timer durationTimer;
    private FloatingActionButton startActivityButton;
    private CustomFloatingActionButton pauseActivityButton;
    private NavigationView navSideMenu;
    private ApplicationLocationListener locationListener;
    private FileSerializer gpxSerializer;
    private TextView timerTextView;
    private TextView longitudeTextView;
    private TextView latitudeTextView;
    private TextView altitudeTextView;
    private TextView velocityTextView;
    private SupportMapFragment googleMapFragment;
    private GoogleMap googleMap;
    private Location lastLocation;
    private CustomScrollView mainActivityScrollView;
    private int pointToDrawMarker = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Properties.mainActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        durationTimer = new Timer(null, this);
        startActivityButton = (FloatingActionButton) findViewById(R.id.fabRun);
        pauseActivityButton = (CustomFloatingActionButton) findViewById(R.id.fabPause);
        navSideMenu = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        locationListener = ApplicationLocationListener.getInstance();
        locationListener.addOnLocationChangedCommand(this);
        mainActivityScrollView = (CustomScrollView) findViewById(R.id.main_activity_scroll_view);

        setSupportActionBar(toolbar);
        initializeMap();
        initializeTextViews();
        initializeListeners();
        setDefaults();

        durationTimer.setTextViewToUpdate(timerTextView);
        mainActivityScrollView.enableTouchForView(googleMapFragment.getView());



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        navSideMenu.setCheckedItem(R.id.dmi_home);
        super.onResume();
        onRotate();
    }

    private void onRotate() {
        if(googleMapFragment != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            View mapView = googleMapFragment.getView();
            ViewGroup.LayoutParams mapParams = mapView.getLayoutParams();
            switch (getResources().getConfiguration().orientation) {
                case Configuration.ORIENTATION_LANDSCAPE:
                    if (mapParams != null) {
                        mapParams.height = (int) Math.ceil(150 * metrics.density);
                        mapView.setLayoutParams(mapParams);
                    }
                    break;
                case Configuration.ORIENTATION_PORTRAIT:
                    if (mapParams != null) {
                        mapParams.height = (int) Math.ceil(300 * metrics.density);
                        mapView.setLayoutParams(mapParams);
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.dmi_map) {
            ActivityHelper.findOrCreateActivity(this,MapActivity.class);
        }
        else if (id == R.id.dmi_properties) {
            ActivityHelper.findOrCreateActivity(this,PropertiesActivity.class);
        }
        else if (id == R.id.dmi_settings) {
            ActivityHelper.findOrCreateActivity(this,SettingsActivity.class);
        }
        else if (id == R.id.dmi_share) {

        }
        else if (id == R.id.dmi_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Properties.PERMISSION_TO_ACCESS_FINE_LOCATION:
                if(locationListener.isGPSEnabled()) {
                    locationListener.requestLocationData();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onLocationChanged(Location location) {
        Tuple<String, String> formattedLocationTuple = LocationHelper.format(location);
        if(latitudeTextView != null && longitudeTextView != null) {
            latitudeTextView.setText(formattedLocationTuple.first);
            longitudeTextView.setText(formattedLocationTuple.second);
        }
        if(altitudeTextView != null) {
            altitudeTextView.setText(String.format(Locale.getDefault(),"%d %s",(int)location.getAltitude(),getString(R.string.m_a_s_l)));
        }
        if(velocityTextView != null) {
            velocityTextView.setText(String.format(Locale.getDefault(),"%d %s",(int)location.getExtras().getDouble(UserSettings.usedVelocity.toString()),UserSettings.usedVelocity.getString(this)));
        }
        if(googleMap != null) {
            if(pointToDrawMarker <= 0 && UserSettings.delayBetweenPoints.getValue() != 0) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("TEST").icon(BitmapDescriptorFactory.fromResource(R.drawable.circle))); //TODO: REPLACE TEST WITH CALORIES FROM START
                pointToDrawMarker = UserSettings.delayBetweenPoints.getValue();
            }
            if(lastLocation != null) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()))
                        .add(new LatLng(location.getLatitude(),location.getLongitude()))
                        .color(getResources().getColor(R.color.colorLightBlue)));
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16)); //TODO: ADD ZOOM CHANGE DUE TO SPEED
        }
        lastLocation = location;
        pointToDrawMarker--;
    }

    private void initializeListeners() {
        startActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (durationTimer.isStarted()){
                    stopTracking(view);
                }
                else{
                    if (ActivityHelper.checkForPermissions(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        startTracking(view);
                    }
                    else {
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},Properties.PERMISSION_TO_WRITE_EXTERNAL_STORAGE);
                    }
                }
            }

        });

        pauseActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(durationTimer.isStarted()) {
                    if(durationTimer.isPaused()) {
                        resumeTracking(view);
                    }
                    else {
                        pauseTracking(view);
                    }
                }
            }
        });

        locationListener.addOnLocationChangedCommand(new OnLocationChangeCommand() {
            @Override
            public void onLocationChanged(Location location) {
                if(gpxSerializer != null) {
                    if(gpxSerializer.isStarted()) {
                        gpxSerializer.addNewPoint(location);
                    }
                }
            }
        });

        locationListener.addOnProviderChangedCommand(new OnProviderChangeCommand() {
            @Override
            public void execute(boolean isGPSEnabled) {
                if (isGPSEnabled) {
                    setGPSStateLabel(LocationServiceState.GPS_STATE_ENABLED);
                }
                else {
                    setGPSStateLabel(LocationServiceState.GPS_STATE_DISABLED);
                }
            }
        });

        navSideMenu.setNavigationItemSelectedListener(this);
        if(ActivityHelper.checkForPermissions(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
            if(locationListener.isGPSEnabled()) {
                locationListener.requestLocationData();
            }
        }
        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Properties.PERMISSION_TO_ACCESS_FINE_LOCATION);
        }


    }

    private void setDefaults(){
        if(locationListener.isGPSEnabled()) {
            setGPSStateLabel(LocationServiceState.GPS_STATE_ENABLED);
        }
        else {
            setGPSStateLabel(LocationServiceState.GPS_STATE_DISABLED);
        }
    }

    private void pauseTracking(View view) {
        durationTimer.pause();
        Snackbar.make(view, R.string.tracking_paused, Snackbar.LENGTH_SHORT).show();
        pauseActivityButton.setImageResource(R.drawable.ic_icon_start);
    }

    private void startTracking(View view) {
        gpxSerializer = new FileSerializer(this);
        if (gpxSerializer.start(FileHelper.getExportFileName(), UserSettings.exportFileFormat)) {
            durationTimer.start();
            Snackbar.make(view, R.string.tracking_started, Snackbar.LENGTH_SHORT).show();
            startActivityButton.setImageResource(R.drawable.ic_icon_stop);
            pauseActivityButton.setVisibility(View.VISIBLE);
        }
}

    private void stopTracking(View view) {
        durationTimer.stop();
        Snackbar.make(view, R.string.tracking_finished, Snackbar.LENGTH_SHORT).show();
        startActivityButton.setImageResource(R.drawable.ic_icon_start);
        pauseActivityButton.setImageResource(R.drawable.ic_icon_pause);
        pauseActivityButton.setVisibility(View.INVISIBLE);
        gpxSerializer.stop();
    }

    private void resumeTracking(View view) {
        durationTimer.resume();
        Snackbar.make(view, R.string.tracking_resumed, Snackbar.LENGTH_SHORT).show();
        pauseActivityButton.setImageResource(R.drawable.ic_icon_pause);
    }

    private void setGPSStateLabel(LocationServiceState state) {
        switch (state){
            case GPS_STATE_ENABLED:
                gpsStateTextView.setText(R.string.gps_enabled);
                gpsStateTextView.setTextColor(getResources().getColor(R.color.colorGreen));
                break;
            case GPS_STATE_DISABLED:
                gpsStateTextView.setText(R.string.gps_disabled);
                gpsStateTextView.setTextColor(getResources().getColor(R.color.colorRed));
                break;
        }
    }

    private void initializeTextViews() {
        gpsStateTextView = (TextView) findViewById(R.id.main_activity_gps_state);
        timerTextView = (TextView) findViewById(R.id.main_activity_timer);
        longitudeTextView = (TextView) findViewById(R.id.main_activity_longitude);
        latitudeTextView = (TextView) findViewById(R.id.main_activity_latitude);
        altitudeTextView = (TextView) findViewById(R.id.main_activity_altitude);
        velocityTextView = (TextView) findViewById(R.id.main_activity_velocity);
    }

    private void initializeMap() {
        googleMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_activity_map);
        googleMapFragment.getMapAsync(this);
    }
}
