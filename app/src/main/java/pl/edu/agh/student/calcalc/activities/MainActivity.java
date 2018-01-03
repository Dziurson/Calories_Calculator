package pl.edu.agh.student.calcalc.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.controls.CustomScrollView;
import pl.edu.agh.student.calcalc.controls.CustomSupportMapFragment;
import pl.edu.agh.student.calcalc.enums.ActivityType;
import pl.edu.agh.student.calcalc.enums.InterpolationState;
import pl.edu.agh.student.calcalc.enums.OutputFileFormat;
import pl.edu.agh.student.calcalc.enums.UserGender;
import pl.edu.agh.student.calcalc.enums.VelocityUnit;
import pl.edu.agh.student.calcalc.helpers.DateHelper;
import pl.edu.agh.student.calcalc.helpers.LocationHelper;
import pl.edu.agh.student.calcalc.helpers.StringHelper;
import pl.edu.agh.student.calcalc.types.PropertyInteger;
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
import pl.edu.agh.student.calcalc.utilities.CalorieCalculator;
import pl.edu.agh.student.calcalc.utilities.FileSerializer;
import pl.edu.agh.student.calcalc.utilities.SplineInterpolation;
import pl.edu.agh.student.calcalc.utilities.Timer;

import static android.os.Environment.getExternalStorageDirectory;
import static pl.edu.agh.student.calcalc.enums.VelocityUnit.VELOCITY_IN_KPH;
import static pl.edu.agh.student.calcalc.enums.VelocityUnit.VELOCITY_IN_MPS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback, OnLocationChangeCommand {

    private TextView gpsStateTextView;
    private Timer durationTimer;
    private CustomFloatingActionButton startActivityButton;
    private CustomFloatingActionButton pauseActivityButton;
    private NavigationView navSideMenu;
    private ApplicationLocationListener locationListener;
    private FileSerializer gpxSerializer;
    private FileSerializer interpolatedSerializer;
    private TextView timerTextView;
    private TextView longitudeTextView;
    private TextView latitudeTextView;
    private TextView altitudeTextView;
    private TextView velocityTextView;
    private TextView averageVelocityTextView;
    private TextView caloriesBurnedTextView;
    private CheckBox showMapCheckBox;
    private CustomSupportMapFragment googleMapFragment;
    private GoogleMap googleMap;
    private Location lastLocation;
    private CustomScrollView mainActivityScrollView;
    private int pointToDrawMarker = 0;
    private Toolbar toolbar;
    private CalorieCalculator calorieCalculator;
    private double totalCalories = 0;
    private SplineInterpolation interpolationX;
    private SplineInterpolation interpolationY;
    private InterpolationState currentActivityInterpolationState;
    private double totalDistance = 0;
    private double totalTime= 0;
    private String filename;
    DrawerLayout drawer;

    public static boolean isTrackingActive = false;

    private static final int SHARE_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restoreSavedState();

        //Sets global reference to MainActivity
        Properties.mainActivity = this;

        //Initialization of Layout
        InitializeLayout();

        //Initialization of Map
        initializeMap();

        //Initialization of Fields
        initializeFields();

        setSupportActionBar(toolbar);

        //Initialization of Listeners
        initializeListeners();

        setDefaults();

        durationTimer.setTextViewToUpdate(timerTextView);
        mainActivityScrollView.enableTouchForView(googleMapFragment.getView());
        showMapCheckBox.setChecked(UserSettings.isMapVisibleOnStartup);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    protected void onResume() {
        navSideMenu.setCheckedItem(R.id.dmi_home);
        super.onResume();
        if(googleMapFragment != null && showMapCheckBox != null) {
            googleMapFragment.reloadDimensions(showMapCheckBox.isChecked());
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
            ActivityHelper.findOrCreateActivity(this,UserPropertiesActivity.class);
        }
        else if (id == R.id.dmi_settings) {
            ActivityHelper.findOrCreateActivity(this,SettingsActivity.class);
        }
        else if (id == R.id.dmi_share) {
            double averageVelocity;
            filename = getExternalStorageDirectory().getPath() + UserSettings.testDir + ((Long)new Date().getTime()).toString() + ".png";
            if(totalTime == 0) {
                averageVelocity = 0;
            }
            else {
                averageVelocity = (UserSettings.usedVelocity == VelocityUnit.VELOCITY_IN_KPH) ? totalDistance/totalTime*3.6 : totalDistance/totalTime;
            }
            View shareImageLayout = ((LayoutInflater) getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.share_image_layout, null);
            String activityType = "";
            if(calorieCalculator!= null) {
                switch (calorieCalculator.getCurrentActivity()) {
                    case WALKING:
                        activityType = getString(R.string.activity_type_walking);
                        break;
                    case RUNNING:
                        activityType = getString(R.string.activity_type_running);
                        break;
                }
            }
            ((TextView)shareImageLayout.findViewById(R.id.share_image_layout_activity_type)).setText(activityType);
            ((TextView)shareImageLayout.findViewById(R.id.share_image_layout_total_distance_value)).setText(String.format(Locale.getDefault(),"%.2f m",totalDistance));
            ((TextView)shareImageLayout.findViewById(R.id.share_image_layout_average_velocity_value)).setText(String.format(Locale.getDefault(),"%.2f %s",averageVelocity,UserSettings.usedVelocity.getString(this)));
            ((TextView)shareImageLayout.findViewById(R.id.share_image_layout_calories_burned_value)).setText(String.format(Locale.getDefault(),"%.2f %s",totalCalories,getString(R.string.kcal)));
            shareImageLayout.setDrawingCacheEnabled(true);
            shareImageLayout.buildDrawingCache();
            Bitmap shareImage = Bitmap.createBitmap(800,400,Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(shareImage);
            shareImageLayout.measure(800,400);
            shareImageLayout.layout(0,0,800,400);
            shareImageLayout.draw(canvas);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filename);
                shareImage.compress(Bitmap.CompressFormat.PNG,100,fos);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                }
                catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
            File file = new File(filename);
            try {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                shareIntent.setType("image/png");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(shareIntent, "send"),SHARE_REQUEST_CODE);
            } catch (Exception e) {
                Toast.makeText(this,this.getString(R.string.no_sharing_app_found),Toast.LENGTH_SHORT).show();
            }
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
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View markerLayout = ((LayoutInflater) getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
                String[] parts = marker.getSnippet().split(";");
                ((TextView)markerLayout.findViewById(R.id.custom_map_marker_longitude)).setText(parts[0]);
                ((TextView)markerLayout.findViewById(R.id.custom_map_marker_latitude)).setText(parts[1]);
                ((TextView)markerLayout.findViewById(R.id.custom_map_marker_altitude_value)).setText(StringHelper.concat(parts[2]," ",MainActivity.this.getString(R.string.m_a_s_l)));
                ((TextView)markerLayout.findViewById(R.id.custom_map_marker_burned_calories_value)).setText(StringHelper.concat(parts[4]," ",MainActivity.this.getString(R.string.kcal)));
                return markerLayout;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Tuple<String, String> formattedLocationTuple = LocationHelper.format(location);
        totalDistance += (lastLocation != null) ? lastLocation.distanceTo(location) : 0;
        totalTime += (lastLocation != null) ? DateHelper.getInterval(new Date(location.getTime()),new Date(lastLocation.getTime()))/1000 : 1;
        if(isTrackingActive && calorieCalculator != null) {
            totalCalories += calorieCalculator.calculateCalories(location);
        }
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
        if(averageVelocityTextView != null) {
            switch (UserSettings.usedVelocity) {
                case VELOCITY_IN_KPH:
                    averageVelocityTextView.setText(String.format(Locale.getDefault(),"%d %s",(int)(totalDistance/totalTime*3.6),UserSettings.usedVelocity.getString(this)));
                    break;
                case VELOCITY_IN_MPS:
                    averageVelocityTextView.setText(String.format(Locale.getDefault(),"%d %s",(int)(totalDistance/totalTime),UserSettings.usedVelocity.getString(this)));
                    break;
            }
        }
        if(googleMap != null) {
            if(pointToDrawMarker <= 0 && UserSettings.delayBetweenPoints.getValue() != 0) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).snippet(formattedLocationTuple.first + ";" + formattedLocationTuple.second + ";" + ((Double)location.getAltitude()).toString() + ";" + ((Double)location.getExtras().getDouble(VelocityUnit.VELOCITY_IN_KPH.toString())).toString() + ";" + String.format(Locale.getDefault(),"%.2f",totalCalories)).icon(BitmapDescriptorFactory.fromResource(R.drawable.circle))); //TODO: REPLACE TEST WITH CALORIES FROM START
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
        if(caloriesBurnedTextView != null) {
            caloriesBurnedTextView.setText(String.format(Locale.getDefault(),"%.2f " + getString(R.string.kcal),totalCalories));
        }
        lastLocation = location;
        pointToDrawMarker--;
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
        calorieCalculator = new CalorieCalculator(UserSettings.activityType);
        currentActivityInterpolationState = UserSettings.interpolationState;
        totalTime = 0;
        totalDistance = 0;
        totalCalories = 0;
        gpxSerializer = new FileSerializer(this);
        interpolatedSerializer = new FileSerializer(this);
        if (gpxSerializer.start(FileHelper.getExportFileName(), UserSettings.exportFileFormat)) {
            if(currentActivityInterpolationState == InterpolationState.ENABLED) {
                interpolatedSerializer.start("Interpolated" + FileHelper.getExportFileName(), UserSettings.exportFileFormat);
            }
            durationTimer.start();
            Snackbar.make(view, R.string.tracking_started, Snackbar.LENGTH_SHORT).show();
            startActivityButton.setImageResource(R.drawable.ic_icon_stop);
            pauseActivityButton.setVisibility(View.VISIBLE);
            isTrackingActive = true;
        }
}

    private void stopTracking(View view) {
        durationTimer.stop();
        Snackbar.make(view, R.string.tracking_finished, Snackbar.LENGTH_SHORT).show();
        startActivityButton.setImageResource(R.drawable.ic_icon_start);
        pauseActivityButton.setImageResource(R.drawable.ic_icon_pause);
        pauseActivityButton.setVisibility(View.INVISIBLE);
        gpxSerializer.stop();
        if(interpolatedSerializer != null) {
            if(interpolatedSerializer.isStarted()) {
                interpolatedSerializer.stop();
            }
        }
        isTrackingActive = false;
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

    private void initializeFields() {
        durationTimer = new Timer(null, this);
        locationListener = ApplicationLocationListener.getInstance();
        interpolationX = new SplineInterpolation();
        interpolationY = new SplineInterpolation();
    }

    private void InitializeLayout() {
        gpsStateTextView = (TextView) findViewById(R.id.main_activity_gps_state);
        timerTextView = (TextView) findViewById(R.id.main_activity_timer);
        longitudeTextView = (TextView) findViewById(R.id.main_activity_longitude);
        latitudeTextView = (TextView) findViewById(R.id.main_activity_latitude);
        altitudeTextView = (TextView) findViewById(R.id.main_activity_altitude);
        velocityTextView = (TextView) findViewById(R.id.main_activity_velocity);
        averageVelocityTextView = (TextView) findViewById(R.id.main_activity_average_velocity);
        caloriesBurnedTextView = (TextView) findViewById(R.id.main_activity_calories);
        startActivityButton = (CustomFloatingActionButton) findViewById(R.id.fabRun);
        pauseActivityButton = (CustomFloatingActionButton) findViewById(R.id.fabPause);
        mainActivityScrollView = (CustomScrollView) findViewById(R.id.main_activity_scroll_view);
        showMapCheckBox = (CheckBox) findViewById(R.id.main_activity_check_box);
        googleMapFragment = (CustomSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_activity_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navSideMenu = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void initializeMap() {
        googleMapFragment.setContext(this);
        googleMapFragment.getMapAsync(this);
    }

    private void initializeListeners() {
        locationListener.addOnLocationChangedCommand(this);
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
                    if(currentActivityInterpolationState == InterpolationState.ENABLED) {
                        if(interpolatedSerializer != null) {
                            if (interpolatedSerializer.isStarted()) {
                                ArrayList<Double> latList = interpolationX.calculateNewSpline(location.getLatitude());
                                ArrayList<Double> lonList = interpolationY.calculateNewSpline(location.getLongitude());
                                if (latList != null && lonList != null) {
                                    for(int i=0; i<10;i++) {
                                        interpolatedSerializer.addNewPoint(latList.get(i),lonList.get(i), location.getAltitude());
                                    }
                                }
                            }
                        }
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

        showMapCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserSettings.isMapVisibleOnStartup = isChecked;
                if(googleMapFragment != null && showMapCheckBox != null) {
                    googleMapFragment.reloadDimensions(showMapCheckBox.isChecked());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SHARE_REQUEST_CODE) {
            if(filename != null) {
                File file = new File(filename);
                if(file.exists() && !file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }

    private void restoreSavedState() {
        File statefile = new File(Properties.stateFile);
        if(statefile.exists() && !statefile.isDirectory()) {
            try {
                FileReader freader = new FileReader(statefile);
                BufferedReader reader = new BufferedReader(freader);
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    switch (parts[0]) {
                        case "velocityUnit":
                            switch (parts[1]) {
                                case "VELOCITY_IN_MPS":
                                    UserSettings.usedVelocity = VELOCITY_IN_MPS;
                                    break;
                                case "VELOCITY_IN_KPH":
                                    UserSettings.usedVelocity = VELOCITY_IN_KPH;
                                    break;
                            }
                            break;
                        case "exportFileFormat":
                            switch (parts[1]) {
                                case "GPX":
                                    UserSettings.exportFileFormat = OutputFileFormat.GPX;
                                    break;
                                case "KML":
                                    UserSettings.exportFileFormat = OutputFileFormat.KML;
                                    break;
                            }
                            break;
                        case "isMapVisible":
                            UserSettings.isMapVisibleOnStartup = (parts[1].compareToIgnoreCase("true") == 0);
                            break;
                        case "delayBetweenPoints":
                            UserSettings.delayBetweenPoints = new PropertyInteger(Integer.parseInt(parts[1]),R.string.no_points);
                            break;
                        case "userWeight":
                            UserSettings.userWeight = new PropertyInteger(Integer.parseInt(parts[1]),R.string.zero);
                            break;
                        case "userHeight":
                            UserSettings.userHeight = new PropertyInteger(Integer.parseInt(parts[1]),R.string.zero);
                            break;
                        case "userAge":
                            UserSettings.userAge = new PropertyInteger(Integer.parseInt(parts[1]),R.string.zero);
                            break;
                        case "userGender":
                            switch (parts[1]) {
                                case "GENDER_MALE":
                                    UserSettings.userGender = UserGender.GENDER_MALE;
                                    break;
                                case "GENDER_FEMALE":
                                    UserSettings.userGender = UserGender.GENDER_FEMALE;
                                    break;
                            }
                            break;
                        case "interpolationState":
                            switch (parts[1]) {
                                case "ENABLED":
                                    UserSettings.interpolationState = InterpolationState.ENABLED;
                                    break;
                                case "DISABLED":
                                    UserSettings.interpolationState = InterpolationState.DISABLED;
                                    break;
                            }
                            break;
                        case "activityType":
                            switch (parts[1]) {
                                case "WALKING":
                                    UserSettings.activityType = ActivityType.WALKING;
                                    break;
                                case "RUNNING":
                                    UserSettings.activityType = ActivityType.RUNNING;
                                    break;
                            }
                            break;
                    }
                }
                reader.close();
                freader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityHelper.savePropertiesState(Properties.stateFile);
    }
}
