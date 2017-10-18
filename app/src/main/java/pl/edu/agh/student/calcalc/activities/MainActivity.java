package pl.edu.agh.student.calcalc.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import pl.edu.agh.student.calcalc.controls.AnimatedFloatingActionButton;
import pl.edu.agh.student.calcalc.helpers.ActivityHelper;
import pl.edu.agh.student.calcalc.utilities.Timer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txvTimer;
    Timer tmrActivityDuration;
    AnimatedFloatingActionButton fabRun;
    AnimatedFloatingActionButton fabPause;
    NavigationView navSideMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txvTimer = (TextView) findViewById(R.id.txvTimer);
        tmrActivityDuration = new Timer(txvTimer, this);
        fabRun = (AnimatedFloatingActionButton) findViewById(R.id.fabRun);
        fabPause = (AnimatedFloatingActionButton) findViewById(R.id.fabPause);

        initializeListeners();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navSideMenu = (NavigationView) findViewById(R.id.nav_view);
        navSideMenu.setNavigationItemSelectedListener(this);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dmi_home) {

        } else if (id == R.id.dmi_map) {
            ActivityHelper.findOrCreateActivity(this,MapActivity.class);
        } else if (id == R.id.dmi_properties) {
            ActivityHelper.findOrCreateActivity(this,PropertiesActivity.class);
        } else if (id == R.id.dmi_settings) {
            ActivityHelper.findOrCreateActivity(this,SettingsActivity.class);
        } else if (id == R.id.dmi_share) {

        } else if (id == R.id.dmi_send) {

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
                    tmrActivityDuration.stop();
                    Snackbar.make(view, R.string.tracking_finished, Snackbar.LENGTH_SHORT).show();
                    fabRun.setImageResource(R.drawable.ic_icon_start);
                    fabPause.setImageResource(R.drawable.ic_icon_pause);
                    txvTimer.setText(R.string.default_timer_value);
                    fabPause.setVisibility(View.INVISIBLE);
                }
                else{
                    tmrActivityDuration.start();
                    Snackbar.make(view, R.string.tracking_started, Snackbar.LENGTH_SHORT).show();
                    fabRun.setImageResource(R.drawable.ic_icon_stop);
                    fabPause.setVisibility(View.VISIBLE);
                }
            }

        });

        fabPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tmrActivityDuration.isStarted()) {
                    if(tmrActivityDuration.isPaused()) {
                        tmrActivityDuration.resume();
                        Snackbar.make(view, R.string.tracking_resumed, Snackbar.LENGTH_SHORT).show();
                        fabPause.setImageResource(R.drawable.ic_icon_pause);
                    }
                    else {
                        tmrActivityDuration.pause();
                        Snackbar.make(view, R.string.tracking_paused, Snackbar.LENGTH_SHORT).show();
                        fabPause.setImageResource(R.drawable.ic_icon_start);
                    }
                }
            }
        });
    }
}
