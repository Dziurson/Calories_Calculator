package pl.edu.agh.student.calcalc.activities;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.adapters.MainExpandableListAdapter;
import pl.edu.agh.student.calcalc.containers.Tuple;
import pl.edu.agh.student.calcalc.controls.CustomFloatingActionButton;
import pl.edu.agh.student.calcalc.controls.CustomExpandableListView;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewChild;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewGroup;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewParameter;
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
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView gpsStateTextView;
    private Timer durationTimer;
    private FloatingActionButton startActivityButton;
    private CustomFloatingActionButton pauseActivityButton;
    private NavigationView navSideMenu;
    private ApplicationLocationListener locationListener;
    private FileSerializer gpxSerializer;

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
        gpsStateTextView = (TextView) findViewById(R.id.main_activity_gps_state);

        initializeExpandableList();
        setSupportActionBar(toolbar);
        initializeListeners();
        setDefaults();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        navSideMenu.setCheckedItem(R.id.dmi_home);
        super.onResume();
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

    private void initializeExpandableList() {
        CustomExpandableListView mainExpandableListView = (CustomExpandableListView) findViewById(R.id.main_activity_expandable_list);

        List<Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>>> listMap = new ArrayList<>();

        List<ExpandableListViewChild> fileTypeChildren = new ArrayList<>();
        fileTypeChildren.add(ExpandableListViewChild.TIME);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> fileTypeEntry = new Tuple<>(ExpandableListViewGroup.TIME, fileTypeChildren);
        listMap.add(fileTypeEntry);

        List<ExpandableListViewChild> locationChildren = new ArrayList<>();
        locationChildren.add(ExpandableListViewChild.LOCATION);
        locationChildren.add(ExpandableListViewChild.MAP);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> locationEntry = new Tuple<>(ExpandableListViewGroup.LOCATION, locationChildren);
        listMap.add(locationEntry);

        List<ExpandableListViewChild> altitudeChildren = new ArrayList<>();
        altitudeChildren.add(ExpandableListViewChild.ALTITUDE);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> altitudeEntry = new Tuple<>(ExpandableListViewGroup.ALTITUDE, altitudeChildren);
        listMap.add(altitudeEntry);

        List<ExpandableListViewChild> velocityChildren = new ArrayList<>();
        velocityChildren.add(ExpandableListViewChild.VELOCITY);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> velocityEntry = new Tuple<>(ExpandableListViewGroup.VELOCITY, velocityChildren);
        listMap.add(velocityEntry);

        HashMap<ExpandableListViewParameter,Object> extras = new HashMap<>();
        extras.put(ExpandableListViewParameter.TIMER, durationTimer);
        extras.put(ExpandableListViewParameter.SENDER, mainExpandableListView);

        MainExpandableListAdapter mainExpandableListAdapter = new MainExpandableListAdapter(this, listMap, extras);

        mainExpandableListView.setAdapter(mainExpandableListAdapter);
    }
}
