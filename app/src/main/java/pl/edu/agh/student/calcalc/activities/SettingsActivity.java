package pl.edu.agh.student.calcalc.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.adapters.SettingsExpandableListAdapter;
import pl.edu.agh.student.calcalc.globals.Properties;
import pl.edu.agh.student.calcalc.globals.UserSettings;
import pl.edu.agh.student.calcalc.helpers.StringHelper;
import pl.edu.agh.student.calcalc.types.Tuple;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewChild;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewGroup;
import pl.edu.agh.student.calcalc.helpers.ActivityHelper;

import static android.os.Environment.getExternalStorageDirectory;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navSideMenu;
    SettingsExpandableListAdapter listAdapter;
    ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navSideMenu = (NavigationView) findViewById(R.id.nav_view);
        navSideMenu.setNavigationItemSelectedListener(this);

        MenuItem item = navSideMenu.getMenu().getItem(4);
        item.setVisible(false);
        prepareExpandableList();
    }

    @Override
    protected void onResume() {
        navSideMenu.setCheckedItem(R.id.dmi_settings);
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
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dmi_home) {
            ActivityHelper.findOrCreateActivity(this,MainActivity.class);
        } else if (id == R.id.dmi_map) {
            ActivityHelper.findOrCreateActivity(this,MapActivity.class);
        } else if (id == R.id.dmi_properties) {
            ActivityHelper.findOrCreateActivity(this,UserPropertiesActivity.class);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareExpandableList() {
        expListView = (ExpandableListView) findViewById(R.id.settings_activity_expandable_list);

        List<Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>>> listMap = new ArrayList<>();

        List<ExpandableListViewChild> fileTypeChildren = new ArrayList<>();
        fileTypeChildren.add(ExpandableListViewChild.FILE_TYPE);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> fileTypeEntry = new Tuple<>(ExpandableListViewGroup.EXPORT_FILE_TYPE, fileTypeChildren);
        listMap.add(fileTypeEntry);

        List<ExpandableListViewChild> velocityTypeChildren = new ArrayList<>();
        velocityTypeChildren.add(ExpandableListViewChild.VELOCITY_UNITS);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> velocityTypeEntry = new Tuple<>(ExpandableListViewGroup.VELOCITY_UNITS,velocityTypeChildren);
        listMap.add(velocityTypeEntry);

        List<ExpandableListViewChild> mapPointsChildren = new ArrayList<>();
        mapPointsChildren.add(ExpandableListViewChild.MAP_POINTS);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> mapPointsEntry = new Tuple<>(ExpandableListViewGroup.MAP_POINTS,mapPointsChildren);
        listMap.add(mapPointsEntry);

        List<ExpandableListViewChild> interpolationChildren = new ArrayList<>();
        interpolationChildren.add(ExpandableListViewChild.INTERPOLATION);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> interpolationEntry = new Tuple<>(ExpandableListViewGroup.INTERPOLATION,interpolationChildren);
        listMap.add(interpolationEntry);

        listAdapter = new SettingsExpandableListAdapter(this, listMap);
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                    if(i != groupPosition) {
                        expListView.collapseGroup(i);
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityHelper.savePropertiesState(Properties.stateFile);
    }
}
