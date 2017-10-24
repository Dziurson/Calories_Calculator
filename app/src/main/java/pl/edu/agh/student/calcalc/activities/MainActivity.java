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

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.containers.Tuple;
import pl.edu.agh.student.calcalc.controls.AnimatedFloatingActionButton;
import pl.edu.agh.student.calcalc.enums.GPSState;
import pl.edu.agh.student.calcalc.globals.Properties;
import pl.edu.agh.student.calcalc.helpers.ActivityHelper;
import pl.edu.agh.student.calcalc.helpers.LocationHelper;
import pl.edu.agh.student.calcalc.listeners.ApplicationLocationListener;
import pl.edu.agh.student.calcalc.reflection.LocationCommand;
import pl.edu.agh.student.calcalc.reflection.ProviderChangeCommand;
import pl.edu.agh.student.calcalc.utilities.Timer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    TextView txvTimer;
    TextView txvLatitude;
    TextView txvLongitude;
    TextView isGPSEnabledLabel;
    Timer tmrActivityDuration;
    FloatingActionButton fabRun;
    AnimatedFloatingActionButton fabPause;
    NavigationView navSideMenu;
    ApplicationLocationListener allLocationListener;
    Toolbar toolbar;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Properties.mainActivity = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txvTimer = (TextView) findViewById(R.id.txvTimer);
        tmrActivityDuration = new Timer(txvTimer, this);
        fabRun = (FloatingActionButton) findViewById(R.id.fabRun);
        fabPause = (AnimatedFloatingActionButton) findViewById(R.id.fabPause);
        navSideMenu = (NavigationView) findViewById(R.id.nav_view);
        txvLatitude = (TextView) findViewById(R.id.txvLatitude);
        txvLongitude = (TextView) findViewById(R.id.txvLongitude);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        allLocationListener = ApplicationLocationListener.getInstance();
        isGPSEnabledLabel = (TextView) findViewById(R.id.txvGPSEnabled);

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
        fabRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tmrActivityDuration.isStarted()){
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

        fabPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tmrActivityDuration.isStarted()) {
                    if(tmrActivityDuration.isPaused()) {
                        resumeTracking(view);
                    }
                    else {
                        pauseTracking(view);
                    }
                }
            }
        });

        allLocationListener.addOnLocationChangedCommand(new LocationCommand() {
            @Override
            public void execute(Location location) {
                Tuple<String,String> locFormatted = LocationHelper.format(location);
                txvLatitude.setText(locFormatted.first);
                txvLongitude.setText(locFormatted.second);
            }
        });

        allLocationListener.addOnProviderChangedCommand(new ProviderChangeCommand() {
            @Override
            public void execute(boolean isGPSEnabled) {
                if (isGPSEnabled) {
                    setGPSStateLabel(GPSState.GPS_STATE_ENABLED);
                }
                else {
                    setGPSStateLabel(GPSState.GPS_STATE_DISABLED);
                }
            }
        });

        navSideMenu.setNavigationItemSelectedListener(this);
        if(ActivityHelper.checkForPermissions(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
            if(allLocationListener.isGPSEnabled()) {
                allLocationListener.requestLocationData();
            }
        }
        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Properties.PERMISSION_TO_ACCESS_FINE_LOCATION);
        }


    }

    private void setDefaults(){
        if(allLocationListener.isGPSEnabled()) {
            setGPSStateLabel(GPSState.GPS_STATE_ENABLED);
        }
        else {
            setGPSStateLabel(GPSState.GPS_STATE_DISABLED);
        }
    }

    private void pauseTracking(View view) {
        tmrActivityDuration.pause();
        Snackbar.make(view, R.string.tracking_paused, Snackbar.LENGTH_SHORT).show();
        fabPause.setImageResource(R.drawable.ic_icon_start);
    }

    private void startTracking(View view) {
        tmrActivityDuration.start();
        Snackbar.make(view, R.string.tracking_started, Snackbar.LENGTH_SHORT).show();
        fabRun.setImageResource(R.drawable.ic_icon_stop);
        fabPause.setVisibility(View.VISIBLE);
    }

    private void stopTracking(View view) {
        tmrActivityDuration.stop();
        Snackbar.make(view, R.string.tracking_finished, Snackbar.LENGTH_SHORT).show();
        fabRun.setImageResource(R.drawable.ic_icon_start);
        fabPause.setImageResource(R.drawable.ic_icon_pause);
        txvTimer.setText(R.string.default_timer_value);
        fabPause.setVisibility(View.INVISIBLE);
    }

    private void resumeTracking(View view) {
        tmrActivityDuration.resume();
        Snackbar.make(view, R.string.tracking_resumed, Snackbar.LENGTH_SHORT).show();
        fabPause.setImageResource(R.drawable.ic_icon_pause);
    }

    private void setGPSStateLabel(GPSState state) {
        switch (state){
            case GPS_STATE_ENABLED:
                isGPSEnabledLabel.setText(R.string.gps_enabled);
                isGPSEnabledLabel.setTextColor(getResources().getColor(R.color.colorGreen));
                break;
            case GPS_STATE_DISABLED:
                isGPSEnabledLabel.setText(R.string.gps_disabled);
                isGPSEnabledLabel.setTextColor(getResources().getColor(R.color.colorRed));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Properties.PERMISSION_TO_ACCESS_FINE_LOCATION:
                if(allLocationListener.isGPSEnabled()) {
                    allLocationListener.requestLocationData();
                }
                break;
        }
    }
}
